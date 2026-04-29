import javafx.scene.input.KeyCode;

public final class GameSettings {

    /**
     * The nominal duration between frames, in nanoseconds.
     * <p>
     * This value can be used to regulate a fixed timestep if desired.
     * Here, 100,000,000 nanoseconds correspond to 0.1 seconds (or 10 frames per second).
     * Usually, an AnimationTimer drives the loop, but this constant might be referenced
     * in other timing calculations or debugging routines.
     * </p>
     */
    public static final long frameduration = 100_000_000;

    /**
     * The size (width and height) of a tank base in pixels.
     * <p>
     * This value is used both for player tanks and enemy tanks.
     * Changing this value uniformly scales tanks in the game world.
     * The layout grid (walls, empty spaces) is also based on multiples of this size.
     * </p>
     */
    public static int tankbaseSize = 30;

    /**
     * The movement speed of the tank base per update call.
     * <p>
     * This is a "units per frame" or "units per tick" value.
     * Since AnimationTimer frames may vary in delta time, this constant could be
     * multiplied by a dt factor. As defined, it suggests that each update, the tank
     * moves by 1 pixel (or 1 unit) along its current velocity direction.
     * </p>
     */
    public static final double tankbaseSpeed = 1;

    /**
     * The minimum time interval between enemy direction changes, in nanoseconds.
     * <p>
     * AI-controlled tanks decide a new movement direction every time they exceed
     * this duration. The value 2,000,000,000L is 2 seconds. This value can be adjusted
     * to make enemies turn more or less frequently.
     * </p>
     */
    public static final long directionChangeTime = 2_000_000_000L;

    /**
     * The minimum time interval between enemy firing attempts, in nanoseconds.
     * <p>
     * When an enemy tank decides to attempt firing, it picks a random time between
     * fireMinTime and fireMaxTime. Here, 500,000,000L is 0.5 seconds.
     * </p>
     */
    public static final long fireMinTime = 500_000_000L;

    /**
     * The maximum time interval between enemy firing attempts, in nanoseconds.
     * <p>
     * Together with fireMinTime, this defines a uniform random range [0.5s, 2s]
     * from which the enemy's next shot delay is chosen. 2,000,000,000L is 2 seconds.
     * </p>
     */
    public static final long fireMaxTime = 2_000_000_000L;

    /**
     * The size (width and height) of each wall tile, in pixels.
     * <p>
     * Walls are square blocks in the grid that fill up the map according to layout[][].
     * This should match tankbaseSize if both tank and wall units are meant to align
     * perfectly on a grid. Here, both are 30 pixels.
     * </p>
     */
    public static final int wallSize = 30;

    /**
     * The speed of bullets, in pixels per update call.
     * <p>
     * This value indicates how far a bullet travels each frame.
     * A higher number results in faster-moving bullets. Here, bullets move 10 pixels
     * per invocation of their update() method, which can be quite fast relative to the tank speed.
     * </p>
     */
    public static double bulletspeed = 10;

    /**
     * The size (width and height) of each bullet in pixels.
     * <p>
     * Bullets are small squares of this pixel dimension. A value of 8 means each bullet sprite
     * is an 8×8 square. This influences collision checks and rendering boundaries.
     * </p>
     */
    public static final int bulletSize = 8;

    /**
     * The width, in pixels, of the visible game window (viewport).
     * <p>
     * This value determines how large the JavaFX Scene is.
     * It also is used to calculate camera boundaries relative to the larger map.
     * Here, an 800×800 pixel window is used for a square game area.
     * </p>
     */
    public static final int viewportwidth = 800, viewportheight = 800;

    /**
     * The full width, in pixels, of the entire game map.
     * <p>
     * Tanks, walls, bullets—everything lives on a 1000×1000 pixel map.
     * Since the viewport is only 800×800, the world will scroll (camera movement)
     * to follow the player so the player stays near the center when possible.
     * </p>
     */
    public static final int mapwidth = 1000, mapheight = 1000;

    /**
     * The number of lives the player starts with.
     * <p>
     * Each time the player's tank is destroyed, this count is decremented.
     * When it reaches zero, it's game over. Starting at 2 means the player has
     * two chance(s) beyond the initial tank (i.e., 2 extra respawns).
     * </p>
     */
    public static int startLivesAmount = 3;

    /**
     * The score reward for destroying one enemy tank.
     * <p>
     * When a player bullet hits an enemy tank, the player's score is increased by
     * this constant (10 points). Modify to reward more or fewer points per kill.
     * </p>
     */
    public static int scorePerEnemyDestroy = 10;

    /**
     * The key used to pause or resume the game.
     * <p>
     * Configured here as KeyCode.P. When the user presses 'P', the game toggles
     * isGamePaused and displays/hides the pause overlay.
     * </p>
     */
    public static KeyCode pauseKey = KeyCode.P;

    /**
     * The key used to restart the game when in a paused or game-over state.
     * <p>
     * Configured here as KeyCode.R. Pressing 'R' calls the restartGame() method
     * to reinitialize everything.
     * </p>
     */
    public static KeyCode restartKey = KeyCode.R;

    /**
     * The key used to exit the game immediately.
     * <p>
     * Configured here as KeyCode.ESCAPE. When pressed, Platform.exit() is invoked
     * to close the application window.
     * </p>
     */
    public static KeyCode exitKey = KeyCode.ESCAPE;

    /**
     * The key used to move the player's tank upward.
     * <p>
     * Configured here as KeyCode.W. When held, the player's vy = -1 (unit/s)
     * along the vertical axis in the movement logic.
     * </p>
     */
    public static KeyCode moveTowardToUp = KeyCode.W;

    /**
     * The key used to move the player's tank downward.
     * <p>
     * Configured here as KeyCode.S. When held, the player's vy = +1 (unit/s).
     * </p>
     */
    public static KeyCode moveTowardToDown = KeyCode.S;

    /**
     * The key used to move the player's tank to the right.
     * <p>
     * Configured here as KeyCode.D. When held, the player's vx = +1 (unit/s).
     * </p>
     */
    public static KeyCode moveTowardToRight = KeyCode.D;

    /**
     * The key used to move the player's tank to the left.
     * <p>
     * Configured here as KeyCode.A. When held, the player's vx = -1 (unit/s).
     * </p>
     */
    public static KeyCode moveTowardToLeft = KeyCode.A;

    /**
     * The key used to fire a bullet from the player's tank.
     * <p>
     * Configured here as KeyCode.X. Pressing X triggers the fireBullet() method
     * if the cooldown period has elapsed.
     * </p>
     */
    public static KeyCode fireKey = KeyCode.X;

    /**
     * The 2D layout array for walls on the map.
     * <p>
     * Each row in this array corresponds to one "cell row" of the map.
     * Each integer value has the following meaning:
     *   0 = empty space (no wall)
     *   1 = indestructible wall
     *   2 = destructible wall
     *
     * The indices i, j in layout[i][j] map to pixel coordinates
     * (x = j × wallSize, y = i × wallSize).
     * This array is 24 rows × 32 columns, so the total map size
     * is 32 × 30 = 960 pixels horizontally (plus 40px margin for mapwidth=1000),
     * and 24 × 30 = 720 pixels vertically (plus 280px margin for mapheight=1000).
     *
     * The outer border of 1's creates a frame of indestructible walls.
     * Inside, there are gaps (0's) for passage, clusters of 2's for destructible blocks,
     * and clumps of 1's representing fixed obstacles.
     * </p>
     */
    public static int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    /**
     * The pixel size of a small explosion effect (e.g., bullet impact).
     * <p>
     * Used when creating a SmallExplosion object at a bullet collision point.
     * A value of 40 px yields a modest explosion sprite. Adjust to make
     * explosions larger or smaller visually.
     * </p>
     */
    public static final double smallExplosionSize = 40;

    /**
     * The duration of a small explosion effect, in milliseconds.
     * <p>
     * Controls how long the small explosion animation runs before marking it destroyed.
     * Here, 250ms (0.25 seconds) is typical for a quick bullet impact.
     * </p>
     */
    public static double smallExplosionDurationInMs = 250;

    /**
     * The pixel size of an enemy explosion effect (e.g., when a tank is destroyed).
     * <p>
     * Used when creating an EnemyExplosion object. A value of 80 px produces
     * a bigger, more dramatic blast than the small explosion.
     * </p>
     */
    public static final double enemyExplosionSize = 80;

    /**
     * The duration of an enemy explosion effect, in milliseconds.
     * <p>
     * Controls how long the enemy explosion lasts before deleting that object.
     * A 500ms (0.5 seconds) duration yields a half-second blast animation.
     * </p>
     */
    public static double enemyExplotionDurationInMs = 500;



}