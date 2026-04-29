import javafx.animation.AnimationTimer; // AnimationTimer import for game loop timing
import javafx.application.Application;   // Application import for JavaFX
import javafx.application.Platform;     // Platform import for exiting application
import javafx.animation.PauseTransition; // PauseTransition import for delays
import javafx.scene.Group;               // Group import to hold game objects
import javafx.scene.Scene;               // Scene import for JavaFX scene
import javafx.scene.control.Label;       // Label import for UI text
import javafx.scene.input.KeyCode;       // KeyCode import for keyboard input
import javafx.scene.layout.Pane;         // Pane import for UI container
import javafx.scene.layout.StackPane;    // StackPane import for layering panes
import javafx.scene.paint.Color;         // Color import for styling
import javafx.scene.shape.Rectangle;     // Rectangle import for viewport clipping
import javafx.stage.Stage;               // Stage import for JavaFX window
import javafx.util.Duration;             // Duration import for timing
import java.util.ArrayList;              // ArrayList import for dynamic lists
import java.util.List;                   // List import for interface
import java.util.Random;                 // Random import for enemy spawn randomness

/**
 * Main application class for the Tank2025 game.
 * <p>
 * This class extends Application to provide a JavaFX application.
 * It sets up the stage, scene, UI layers, game loop, input handling,
 * spawning logic, collision detection, and more. This comment block
 * gives a high-level overview, but the code below includes many
 * more comments to obscure the logic intentionally.
 * </p>
 */
public class Tank2025 extends Application {

    /**
     * GameSettings holds all the configurable constants and layout for the Tank2025 game.
     * <p>
     * This class is final and contains only public static fields and a layout array.
     * It centralizes values such as speeds, sizes, timings, key bindings, and the map layout.
     * Extensive comments have been added to obscure and explain each parameter in detail.
     * </p>
     */


    /** Width and height of the visible game viewport. */
    public static int viewportwidth = GameSettings.viewportwidth, viewportheight = GameSettings.viewportheight;
    // viewportwidth: width of the visible portion of the game world
    // viewportheight: height of the visible portion of the game world

    /** Total dimensions of the entire game map. */
    public static int mapwidth = GameSettings.mapwidth, mapheight = GameSettings.mapheight;
    // mapwidth: total width of the entire map, beyond the visible viewport
    // mapheight: total height of the entire map, beyond the visible viewport

    private StackPane mainStackPane; // mainStackPane holds the viewport and UI layers stacked
    private Pane viewport, pausePane, gameOverPane; // viewport: the game world view; pausePane: overlay when paused; gameOverPane: overlay when game over
    private Label goLabel; // Label for "Game Over" message and instructions
    private boolean isGamePaused = false, gameOver = false; // Flags to track paused state and game over state
    private AnimationTimer mainLoop; // mainLoop is the game loop timer
    private Group world; // world Group holds all game objects, which can be translated for camera movement
    private PlayerTankBase player; // player holds the player tank instance
    private final double playerInitialX = mapwidth / 2.0 - TankBase.tankbaseSize / 2.0;
    // playerInitialX: X coordinate to center player at start (map center minus half tank size)
    private final double playerInitialY = mapheight / 2.0 - TankBase.tankbaseSize / 2.0;
    // playerInitialY: Y coordinate to center player at start (map center minus half tank size)
    private final List<GameObject> gameObjects = new ArrayList<>(); // gameObjects list tracks all active game objects
    private final List<EnemyTankBase> pendingEnemyFires = new ArrayList<>();
    // pendingEnemyFires: list of enemies that have requested to fire bullets this frame
    private Label scoreLabel, livesLabel; // scoreLabel: displays current score; livesLabel: displays remaining lives
    private int score = 0, lives = GameSettings.startLivesAmount;
    // score: player's current score; lives: how many lives the player has left
    private PauseTransition respawnTransition; // respawnTransition: used for delayed respawn timing
    private Random random = new Random(); // random: random number generator for spawning enemies
    private static Tank2025 tank2025; // tank2025: static reference to this instance for callbacks
    private int[][] layout = GameSettings.map; // layout: 2D array defining wall positions (0 = empty, 1 = indestructible, 2 = destructible)

    // Cooldown-related fields
    private long lastFireTime = 0; // lastFireTime: timestamp of last player fire to enforce cooldown
    private static final long FIRE_COOLDOWN = 500_000_000L; // 0.5 seconds in nanoseconds for firing cooldown

    /**
     * Static callback for enemy tanks to request firing a bullet.
     * <p>
     * If the game is not over and the player is present, queue the enemy to fire.
     * This method gets called from within EnemyTankBase when it decides to shoot.
     * </p>
     *
     * @param e The EnemyTankBase instance requesting to fire.
     */
    public static void controlEnemyFires(EnemyTankBase e) {
        Tank2025 m = tank2025; // get the single instance of Tank2025
        if (!m.gameOver) { // if the game hasn't ended
            // check that the player's tank is currently in the world
            if (m.world.getChildren().contains(m.player.getImageView())) {
                m.pendingEnemyFires.add(e); // queue this enemy for bullet creation next update
            }
        }
    }

    /**
     * JavaFX start method: initializes stage, scene, and UI.
     * <p>
     * Called when the application is launched. Sets up the game view,
     * UI overlays, pause and game-over panes, initializes game objects,
     * input handling, spawns initial enemies, and starts the main loop.
     * </p>
     *
     * @param stage Primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        tank2025 = this; // assign static reference to this instance
        world = new Group(); // world group will contain all game object nodes
        viewport = new Pane(world); // viewport pane contains the world group for camera movement
        viewport.setPrefSize(viewportwidth, viewportheight); // set preferred size to viewport dimensions
        viewport.setClip(new Rectangle(viewportwidth, viewportheight));
        // clip is used to restrict rendering to the viewport area (no drawing outside)
        viewport.setStyle("-fx-background-color: black;"); // set background color of viewport to black

        // Initialize score and lives labels for UI
        scoreLabel = new Label("Score: " + score);
        scoreLabel.setTextFill(Color.WHITE); // white text color
        scoreLabel.setTranslateX(10); // position at X=10 in the UI layer
        scoreLabel.setTranslateY(10); // position at Y=10 in the UI layer
        livesLabel = new Label("Lives: " + lives);
        livesLabel.setTextFill(Color.WHITE); // white text color
        livesLabel.setTranslateX(10); // position at X=10 in the UI layer
        livesLabel.setTranslateY(30); // position at Y=30 for small vertical offset under the score

        Pane uiLayer = new Pane(scoreLabel, livesLabel);
        // uiLayer holds score and lives labels, separate from world
        uiLayer.setPickOnBounds(false);
        // allow clicks to pass through transparent areas of the UI layer if needed

        // Game Over label and pane setup
        goLabel = new Label("GAME OVER\nScore: 0\nPress R to Restart\nPress Esc to Exit");
        goLabel.setTextFill(Color.RED); // red text color for Game Over message
        goLabel.setStyle("-fx-font-size: 36px; -fx-text-alignment: center;");
        // font size and alignment styling
        goLabel.setTranslateX(viewportwidth / 2.0 - 200);
        // center horizontally by shifting left half width of label area
        goLabel.setTranslateY(viewportheight / 2.0 - 100);
        // center vertically by shifting up half height of label area

        gameOverPane = new Pane(goLabel);
        // gameOverPane overlays the entire viewport when shown
        gameOverPane.setPrefSize(viewportwidth, viewportheight);
        gameOverPane.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
        // semi-transparent black background for game over overlay
        gameOverPane.setVisible(false); // initially hidden

        // Pause label and pane setup
        Label paLabel = new Label(
                "PAUSED\nPress P to Resume\nPress R to Restart\nPress Esc to Exit"
        );
        paLabel.setTextFill(Color.YELLOW); // yellow text for paused message
        paLabel.setStyle("-fx-font-size: 36px; -fx-text-alignment: center;");
        paLabel.setTranslateX(viewportwidth / 2.0 - 200);
        paLabel.setTranslateY(viewportheight / 2.0 - 100);

        pausePane = new Pane(paLabel);
        // pausePane overlays on top when game is paused
        pausePane.setPrefSize(viewportwidth, viewportheight);
        pausePane.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
        pausePane.setVisible(false); // initially hidden

        // mainStackPane stacks viewport, UI, gameOverPane, pausePane in that order
        mainStackPane = new StackPane(viewport, uiLayer, gameOverPane, pausePane);
        Scene scene = new Scene(mainStackPane, viewportwidth, viewportheight, Color.BLACK);
        // create scene with black background (not strictly necessary since viewport covers it)

        stage.setScene(scene); // set scene to stage
        stage.setTitle("Tank2025"); // window title
        stage.show(); // show the application window
        mainStackPane.requestFocus();
        // request focus on mainStackPane so it receives key events

        // Initialize game objects (player and walls)
        initGameObjects();

        // Set up input controls (movement, firing, pause, etc.)
        initInput(scene);

        // Spawn 5 enemies at game start in the upper half at non-wall positions
        for (int i = 0; i < 5; i++) {
            spawnEnemy();
        }

        // Start the main game loop
        mainUpdate();
    }

    /**
     * Initializes the player tank and static walls based on layout.
     * <p>
     * Reads the layout matrix from GameSettings, creates Wall objects
     * (destructible and indestructible), and adds them with the player.
     * This sets up the initial map for the game.
     * </p>
     */
    private void initGameObjects() {
        player = new PlayerTankBase(playerInitialX, playerInitialY);
        // Create player tank at initial coordinates (center of map)
        gameObjects.add(player);
        // Add player to gameObjects list so it gets updated in loop
        world.getChildren().add(player.getImageView());
        // Add player node to world group for rendering

        int rows = layout.length, cols = layout[0].length;
        // Determine number of rows and columns in layout matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (layout[i][j] == 1) {
                    // If layout cell is 1, create indestructible wall
                    Wall w = new Wall(j * Wall.wallSize, i * Wall.wallSize, false);
                    // position is column * wallSize for X, row * wallSize for Y
                    gameObjects.add(w); // add wall object to list
                    world.getChildren().add(w.getImageView());
                    // add wall node to world group
                } else if (layout[i][j] == 2) {
                    // If layout cell is 2, create destructible wall
                    Wall w = new Wall(j * Wall.wallSize, i * Wall.wallSize, true);
                    gameObjects.add(w); // add destructible wall to list
                    world.getChildren().add(w.getImageView());
                }
                // If layout cell is 0, leave it empty
            }
        }
    }

    /**
     * Sets up keyboard input handlers for gameplay and UI control.
     * <p>
     * Handles movement (WASD or arrow keys), firing (X key), pausing (P key),
     * restarting (R key), and exiting (Esc key) based on game state.
     * </p>
     *
     * @param scene The scene to bind key events to.
     */
    private void initInput(Scene scene) {
        // When a key is pressed...
        scene.setOnKeyPressed(e -> {
            KeyCode c = e.getCode(); // get the key code
            if (c == GameSettings.pauseKey && !gameOver) {
                // If pause key pressed and game is not over, toggle pause state
                isGamePaused = !isGamePaused;
                pausePane.setVisible(isGamePaused);
                // show or hide pause overlay
                return;
            }
            if (gameOver) {
                // If game is over, only allow restart or exit
                if (c == GameSettings.restartKey) restartGame();
                else if (c == GameSettings.exitKey) Platform.exit();
                return;
            }
            if (isGamePaused) {
                // If game is paused, allow restart or exit
                if (c == GameSettings.restartKey) restartGame();
                else if (c == GameSettings.exitKey) Platform.exit();
                return;
            }
            if (!world.getChildren().contains(player.getImageView())) return;
            // If player not in world (e.g., during respawn), ignore input

            if (c == GameSettings.moveTowardToUp) {
                // Up movement key pressed
                player.setVx(0); // stop horizontal movement
                player.setVy(-1); // move upward (negative Y)
            } else if (c == GameSettings.moveTowardToDown) {
                // Down movement key pressed
                player.setVx(0);
                player.setVy(1); // move downward (positive Y)
            } else if (c == GameSettings.moveTowardToLeft) {
                // Left movement key pressed
                player.setVy(0);
                player.setVx(-1); // move left (negative X)
            } else if (c == GameSettings.moveTowardToRight) {
                // Right movement key pressed
                player.setVy(0);
                player.setVx(1); // move right (positive X)
            } else if (c == KeyCode.X) {
                // Fire bullet key pressed
                long now = System.nanoTime();
                // get current time in nanoseconds
                if (now - lastFireTime >= FIRE_COOLDOWN) {
                    // only fire if cooldown period has passed
                    fireBullet(); // call method to spawn bullet
                    lastFireTime = now;
                    // update last fire time
                }
            }
        });

        // When a key is released...
        scene.setOnKeyReleased(e -> {
            if (isGamePaused || gameOver) return;
            // ignore releases if paused or game over
            if (!world.getChildren().contains(player.getImageView())) return;
            // ignore if player not in world
            KeyCode code = e.getCode(); // get the released key code
            if (code == KeyCode.W || code == KeyCode.S) {
                // If vertical movement keys released, stop vertical velocity
                player.setVy(0);
            } else if (code == KeyCode.A || code == KeyCode.D) {
                // If horizontal movement keys released, stop horizontal velocity
                player.setVx(0);
            }
        });
    }

    /**
     * Fires a bullet from the player's current position and direction.
     * <p>
     * Creates a new Bullet object, sets it as the player's bullet,
     * adds it to gameObjects and the world for rendering and updating.
     * </p>
     */
    private void fireBullet() {
        // Calculate spawn position centered on tank turret
        Bullet b = new Bullet(
                player.getX() + TankBase.tankbaseSize / 2.0 - Bullet.bulletSize / 2.0,
                player.getY() + TankBase.tankbaseSize / 2.0 - Bullet.bulletSize / 2.0,
                player.getTankLookTowardstoX(), player.getTankLookTowardstoY()
        );
        // Set this bullet as belonging to player so collisions are handled correctly
        b.setPlayerBullet(true);
        gameObjects.add(b);
        // Add bullet to game object tracking
        world.getChildren().add(b.getImageView());
        // Add bullet node to world group for rendering
    }

    /**
     * Spawns an enemy tank at a random non-wall cell on the upper half of the map.
     * <p>
     * Chooses random row/column in the top half until an empty location (layout == 0) is found,
     * then creates and adds the EnemyTankBase to the world.
     * </p>
     */
    private void spawnEnemy() {
        int rows = layout.length;
        int cols = layout[0].length;
        // Pick random row in top half and random column until an empty cell found
        int r = random.nextInt(rows / 2);
        // random row index between 0 (inclusive) and rows/2 (exclusive)
        int c = random.nextInt(cols);
        // random column index between 0 and cols (exclusive)
        while (layout[r][c] != 0) {
            // If chosen cell is not empty (wall), pick another
            r = random.nextInt(rows / 2);
            c = random.nextInt(cols);
        }
        EnemyTankBase e = new EnemyTankBase(c * Wall.wallSize, r * Wall.wallSize);
        // Create new enemy tank at pixel position (column * wallSize, row * wallSize)
        gameObjects.add(e); // add to list for updates
        world.getChildren().add(e.getImageView());
        // add visual node to world group
    }

    /**
     * Initializes and starts the main game loop via AnimationTimer.
     * <p>
     * On each frame, processes movement, firing, collisions,
     * spawns new enemies over time, and updates the camera to follow the player.
     * </p>
     */
    private void mainUpdate() {
        mainLoop = new AnimationTimer() {
            long spawnClock = 0;
            // spawnClock tracks time since last enemy spawn

            @Override
            public void handle(long time) {
                if (gameOver || isGamePaused) return;
                // skip updates if game over or paused

                ArrayList<GameObject> newObjects = new ArrayList<>();
                // newObjects: temporary list to hold newly created objects this frame
                ArrayList<GameObject> deadObjects = new ArrayList<>();
                // deadObjects: temporary list to hold objects that should be removed

                // Iterate over a snapshot of gameObjects to avoid concurrent modifications
                for (GameObject obj : new ArrayList<>(gameObjects)) {
                    if (obj instanceof PlayerTankBase) {
                        movePlayerTank((PlayerTankBase) obj);
                        // Handle movement and collision of player tank
                    } else if (obj instanceof EnemyTankBase) {
                        moveEnemyTank((EnemyTankBase) obj);
                        // Handle movement and collision of enemy tank
                    } else if (obj instanceof Bullet) {
                        updateBullet((Bullet) obj, newObjects, deadObjects);
                        // Handle bullet movement, collisions, and spawning explosions
                    } else {
                        checkExplosionEnd(obj, deadObjects);
                        // Remove explosion objects when their animation ends
                    }
                }

                addEnemyBullets(newObjects);
                // Add bullets for enemies that requested firing this frame
                spawnNewEnemy(time);
                // Possibly spawn a new enemy based on spawnClock
                removeObjects(deadObjects);
                // Remove objects scheduled for destruction
                addObjects(newObjects);
                // Add newly spawned objects (bullets, explosions, etc.)
                followPlayer();
                // Update camera to center on player if possible
            }

            /**
             * Moves the player tank, handling collisions with walls and map boundaries.
             *
             * @param tank The PlayerTankBase instance to move.
             */
            void movePlayerTank(PlayerTankBase tank) {
                double oldPosX = tank.getX();
                // Save old X to revert if collision
                double oldPosY = tank.getY();
                // Save old Y to revert if collision
                tank.update(System.nanoTime());
                // Update position based on velocity and delta time
                // Check collisions against a snapshot of walls to avoid modifying list during iteration
                for (GameObject go : new ArrayList<>(gameObjects)) {
                    if (go instanceof Wall) {
                        Wall wall = (Wall) go;
                        // If player's bounding box intersects wall's bounding box, revert position
                        if (tank.getImageView().getBoundsInParent()
                                .intersects(wall.getImageView().getBoundsInParent())) {
                            tank.setX(oldPosX); // revert X
                            tank.setY(oldPosY); // revert Y
                            break; // break on first collision
                        }
                    }
                }
                // Ensure tank does not move out of map bounds
                if (tank.getX() < 0) tank.setX(0);
                if (tank.getX() > mapwidth - TankBase.tankbaseSize)
                    tank.setX(mapwidth - TankBase.tankbaseSize);
                if (tank.getY() < 0) tank.setY(0);
                if (tank.getY() > mapheight - TankBase.tankbaseSize)
                    tank.setY(mapheight - TankBase.tankbaseSize);
            }

            /**
             * Moves an enemy tank, reversing on wall collision and clamping to map edges.
             *
             * @param tank The EnemyTankBase instance to move.
             */
            void moveEnemyTank(EnemyTankBase tank) {
                double oldPosX = tank.getX();
                // Save old X to revert on collision
                double oldPosY = tank.getY();
                // Save old Y to revert on collision
                tank.update(System.nanoTime());
                // Update position based on AI velocity/direction
                // Check collisions against walls
                for (GameObject go : new ArrayList<>(gameObjects)) {
                    if (go instanceof Wall) {
                        Wall wall = (Wall) go;
                        // If enemy collides with wall, revert position
                        if (tank.getImageView().getBoundsInParent()
                                .intersects(wall.getImageView().getBoundsInParent())) {
                            tank.setX(oldPosX);
                            tank.setY(oldPosY);
                            break;
                        }
                    }
                }
                // Clamp enemy tank within map bounds
                tank.setX(TankGameUtility.limitValueWithBounds(
                        tank.getX(), 0, mapwidth - TankBase.tankbaseSize));
                tank.setY(TankGameUtility.limitValueWithBounds(
                        tank.getY(), 0, mapheight - TankBase.tankbaseSize));
            }

            /**
             * Updates a bullet: moves it, handles wall collisions, and delegates hits.
             *
             * @param bullet    The bullet to update.
             * @param spawn     List to add spawned effects/objects.
             * @param destroy   List to collect objects to remove.
             */
            void updateBullet(Bullet bullet, ArrayList<GameObject> spawn, ArrayList<GameObject> destroy) {
                bullet.update(System.nanoTime());
                // Move bullet along its trajectory
                boolean bulletDestroyed = false;
                // Flag to indicate if bullet hit a wall or object and should be removed
                // Check collisions against walls
                for (GameObject go : new ArrayList<>(gameObjects)) {
                    if (go instanceof Wall) {
                        Wall w = (Wall) go;
                        // If bullet intersects wall
                        if (bullet.getImageView().getBoundsInParent()
                                .intersects(w.getImageView().getBoundsInParent())) {
                            spawn.add(new SmallExplosion(bullet.getX(), bullet.getY()));
                            // Add small explosion effect at bullet position
                            if (w.isDestructible()) {
                                destroy.add(w);
                                // Remove destructible wall
                                world.getChildren().remove(w.getImageView());
                            }
                            destroy.add(bullet);
                            // Remove bullet after impact
                            bulletDestroyed = true;
                            break;
                        }
                    }
                }
                if (bulletDestroyed) return;
                // If bullet was destroyed by hitting wall, skip further checks
                // If bullet is out of map bounds, remove it
                if (bullet.getX() < -Bullet.bulletSize || bullet.getX() > mapwidth
                        || bullet.getY() < -Bullet.bulletSize || bullet.getY() > mapheight) {
                    destroy.add(bullet);
                    return;
                }
                // Delegate collision handling based on bullet owner
                if (bullet.isPlayersBullet()) {
                    checkPlayerBulletHits(bullet, spawn, destroy);
                } else {
                    checkEnemyBulletHits(bullet, spawn, destroy);
                }
            }

            /**
             * Checks and handles collisions between a player bullet and enemies.
             *
             * @param bullet  The player's bullet.
             * @param spawn   List to add explosion effects.
             * @param destroy List to remove bullet and enemy.
             */
            void checkPlayerBulletHits(Bullet bullet, ArrayList<GameObject> spawn, ArrayList<GameObject> destroy) {
                for (GameObject go : new ArrayList<>(gameObjects)) {
                    if (go instanceof EnemyTankBase) {
                        EnemyTankBase enemy = (EnemyTankBase) go;
                        // If bullet intersects enemy
                        if (bullet.getImageView().getBoundsInParent()
                                .intersects(enemy.getImageView().getBoundsInParent())) {
                            double centerX = enemy.getX() + TankBase.tankbaseSize / 2.0;
                            double centerY = enemy.getY() + TankBase.tankbaseSize / 2.0;
                            spawn.add(new EnemyExplosion(centerX, centerY));
                            // Add explosion animation at enemy center
                            destroy.add(bullet); // remove bullet
                            destroy.add(enemy); // remove enemy
                            score += GameSettings.scorePerEnemyDestroy;
                            // Increase score by defined amount
                            scoreLabel.setText("Score: " + score);
                            // Update UI label
                            break; // exit loop after first hit
                        }
                    }
                }
            }

            /**
             * Checks and handles collisions between an enemy bullet and the player.
             *
             * @param bullet  The enemy's bullet.
             * @param spawn   List to add explosion effects.
             * @param destroy List to remove the bullet.
             */
            void checkEnemyBulletHits(Bullet bullet, ArrayList<GameObject> spawn, ArrayList<GameObject> destroy) {
                if (bullet.getImageView().getBoundsInParent()
                        .intersects(player.getImageView().getBoundsInParent())) {
                    // If bullet hits player
                    double centerX = player.getX() + TankBase.tankbaseSize / 2.0;
                    double centerY = player.getY() + TankBase.tankbaseSize / 2.0;
                    spawn.add(new EnemyExplosion(centerX, centerY));
                    // Add explosion effect at player center
                    destroy.add(bullet); // remove bullet
                    handlePlayerHit(); // process player being hit
                }
            }

            /**
             * Handles logic when the player is hit: reduce lives, respawn or end game.
             */
            void handlePlayerHit() {
                lives--; // decrement lives
                livesLabel.setText("Lives: " + lives);
                // update UI label for remaining lives
                world.getChildren().remove(player.getImageView());
                // remove player tank from world group
                player.setVx(0); // stop tank movement
                player.setVy(0); // stop tank movement
                if (respawnTransition != null) {
                    respawnTransition.stop();
                    // stop any existing respawn timer (shouldn't usually be one)
                    respawnTransition = null;
                }
                if (lives > 0) {
                    startRespawnTimer();
                    // if player has lives left, start timer to respawn
                } else {
                    showGameOver();
                    // if no lives left, show game over
                }
            }

            /**
             * Starts a delay before respawning the player tank.
             * <p>
             * Clears bullets, waits a random 1-2 seconds, then restores the player.
             * </p>
             */
            void startRespawnTimer() {
                clearAllBullets();
                // remove all bullets from world and gameObjects
                int seconds = (int) (Math.random() * 2) + 1;
                // random delay between 1 and 2 seconds
                respawnTransition = new PauseTransition(Duration.seconds(seconds));
                // create PauseTransition for respawn delay
                respawnTransition.setOnFinished(event -> {
                    if (!world.getChildren().contains(player.getImageView())) {
                        world.getChildren().add(player.getImageView());
                        // add player back into world
                    }
                    player.setX(playerInitialX);
                    // reset player X to initial spawn position
                    player.setY(playerInitialY);
                    // reset player Y to initial spawn position
                    pendingEnemyFires.clear();
                    // clear any pending enemy fire requests
                    spawnEnemy();
                    // spawn a new enemy to keep gameplay going
                    world.setTranslateX(0);
                    // reset camera X to default
                    world.setTranslateY(0);
                    // reset camera Y to default
                    respawnTransition = null;
                    // clear reference to transition
                });
                respawnTransition.play();
                // start the pause transition (countdown)
            }

            /**
             * Displays the game-over UI and stops further updates.
             */
            void showGameOver() {
                gameOver = true; // set gameOver flag
                String msg = "GAME OVER\nScore: " + score + "\nPress R to Restart\nPress Esc to Exit";
                goLabel.setText(msg);
                // update game over text with final score
                gameOverPane.setVisible(true);
                // show game over overlay
            }

            /**
             * Checks if an explosion effect has finished and should be removed.
             *
             * @param obj     The GameObject to check.
             * @param destroy The list to add it to if destroyed.
             */
            void checkExplosionEnd(GameObject obj, ArrayList<GameObject> destroy) {
                if (obj instanceof ExplosionBase) {
                    ExplosionBase exp = (ExplosionBase) obj;
                    if (exp.isDestroyed()) destroy.add(obj);
                    // if explosion finished its animation, schedule removal
                } else if (obj instanceof EnemyExplosion) {
                    EnemyExplosion exp = (EnemyExplosion) obj;
                    if (exp.isDestroyed()) destroy.add(obj);
                    // if enemy-specific explosion finished, schedule removal
                }
            }

            /**
             * Adds pending enemy-fired bullets into the newObjects list.
             *
             * @param newObjects List to which new bullets are added.
             */
            void addEnemyBullets(ArrayList<GameObject> newObjects) {
                if (world.getChildren().contains(player.getImageView())) {
                    // only spawn enemy bullets if player exists
                    for (EnemyTankBase enemy : pendingEnemyFires) {
                        double bulletX = enemy.getX() + TankBase.tankbaseSize / 2.0 - Bullet.bulletSize / 2.0;
                        double bulletY = enemy.getY() + TankBase.tankbaseSize / 2.0 - Bullet.bulletSize / 2.0;
                        // calculate bullet spawn position at enemy's center
                        Bullet newBullet = new Bullet(
                                bulletX, bulletY,
                                enemy.getTankLookTowardstoX(), enemy.getTankLookTowardstoY()
                        );
                        newBullet.setPlayerBullet(false);
                        // mark bullet as enemy bullet
                        newObjects.add(newBullet);
                        // add to newObjects; actual addition to world happens after loop
                    }
                }
                pendingEnemyFires.clear();
                // clear the list of pending requests once processed
            }

            /**
             * Spawns a new enemy at intervals of 5 seconds.
             *
             * @param currentTime The current timestamp in nanoseconds.
             */
            void spawnNewEnemy(long currentTime) {
                if (currentTime - spawnClock >= 5_000_000_000L) {
                    // if 5 seconds (in nanoseconds) have passed since last spawn
                    spawnClock = currentTime;
                    // reset spawn clock to now
                    spawnEnemy();
                    // call spawnEnemy to add a new enemy to the game
                }
            }

            /**
             * Removes all objects in the toRemove list from game and scene.
             *
             * @param toRemove List of GameObjects to remove.
             */
            void removeObjects(ArrayList<GameObject> toRemove) {
                for (GameObject obj : toRemove) {
                    gameObjects.remove(obj);
                    // remove from gameObjects list so it's not updated next frame
                    world.getChildren().remove(obj.getImageView());
                    // remove visual node from world group so it's not rendered
                }
            }

            /**
             * Adds all objects in the toAdd list to game and scene.
             *
             * @param toAdd List of GameObjects to add.
             */
            void addObjects(ArrayList<GameObject> toAdd) {
                for (GameObject obj : toAdd) {
                    gameObjects.add(obj);
                    // add to gameObjects so it's updated next frame
                    world.getChildren().add(obj.getImageView());
                    // add to world group so it's rendered
                }
            }

            /**
             * Centers the camera on the player, clamping within map bounds.
             */
            void followPlayer() {
                double camX = -player.getX() + viewportwidth / 2.0 - TankBase.tankbaseSize / 2.0;
                double camY = -player.getY() + viewportheight / 2.0 - TankBase.tankbaseSize / 2.0;
                // Calculate camera translation so player is centered in viewport
                camX = TankGameUtility.limitValueWithBounds(camX, -(mapwidth - viewportwidth), 0);
                camY = TankGameUtility.limitValueWithBounds(camY, -(mapheight - viewportheight), 0);
                // Clamp camera translation so it doesn't show area outside map
                world.setTranslateX(camX); // apply X translation to world group
                world.setTranslateY(camY); // apply Y translation to world group
            }
        };
        mainLoop.start(); // start the animation timer (game loop)
    }

    /**
     * Clears all bullets from the game, removing them from objects and scene.
     * <p>
     * Iterates over gameObjects, collects bullets, and removes each.
     * </p>
     */
    private void clearAllBullets() {
        List<GameObject> remove = new ArrayList<>();
        // Temporary list to hold bullets to remove
        for (GameObject obj : gameObjects) {
            if (obj instanceof Bullet) {
                remove.add(obj);
                // Add bullet to remove list
            }
        }
        remove.forEach(b -> {
            gameObjects.remove(b);
            // Remove from gameObjects list
            world.getChildren().remove(b.getImageView());
            // Remove visual representation from world
        });
    }

    /**
     * Restarts the game state: resets score, lives, clears objects,
     * and respawns the player and initial enemies and walls.
     * <p>
     * Hides overlays, stops respawn timers, and rebuilds entire layout
     * based on the initial GameSettings.layout.
     * </p>
     */
    private void restartGame() {
        gameOver = false; // clear game over flag
        isGamePaused = false; // clear pause flag
        score = 0; // reset score
        scoreLabel.setText("Score: 0"); // update UI label
        lives = GameSettings.startLivesAmount; // reset lives
        livesLabel.setText("Lives: " + GameSettings.startLivesAmount);
        // update UI label
        gameOverPane.setVisible(false); // hide game over overlay
        pausePane.setVisible(false); // hide pause overlay

        if (respawnTransition != null) {
            respawnTransition.stop(); // stop any ongoing respawn timer
            respawnTransition = null;
        }

        // Clear all bullets and pending fires
        clearAllBullets();
        pendingEnemyFires.clear();

        // Reset world and gameObjects to initial state
        world.getChildren().clear();
        // remove all nodes from world
        gameObjects.clear();
        // clear all references to game objects

        // Re-initialize player and walls
        initGameObjects();
        // spawn initial walls and player

        // Spawn 5 enemies at game start in the upper half at non-wall positions
        for (int i = 0; i < 5; i++) {
            spawnEnemy();
        }

        // Ensure camera resets
        world.setTranslateX(0);
        world.setTranslateY(0);

        mainStackPane.requestFocus();
        // ensure focus is  back on the game for input
        }

}
