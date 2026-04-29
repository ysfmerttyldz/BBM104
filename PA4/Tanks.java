import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.Random;

/**
 * Abstract base class representing a tank.
 * Provides common functionality such as movement, animation, and direction handling.
 * Intended to be extended by specific tank types like player or enemy tanks.
 */
abstract class TankBase implements GameObject, IMovable, ISize {

    /**
     * Size of the tank in pixels.
     */
    public static int tankbaseSize = GameSettings.tankbaseSize;

    /**
     * Duration of each animation frame in nanoseconds.
     */
    private static final long framedur = GameSettings.frameduration;

    /**
     * Movement speed of the tank.r
     */
    private static final double tankbaseSpeed = GameSettings.tankbaseSpeed;

    private double x, y;
    private double vx = 0, vy = 0;

    /**
     * Direction the tank is facing (x component).
     */
    protected int tanklooktowardstox = 1;

    /**
     * Direction the tank is facing (y component).
     */
    protected int tanklooktowardstoy = 0;

    /**
     * Animation frames of the tank.
     */
    protected final Image[] frames = new Image[2];

    /**
     * Visual representation of the tank.
     */
    private final ImageView view;

    private int currentFrame = 0;
    private long lastFrameTime = 0;

    /**
     * Constructs a new TankBase with given position.
     * Loads default white tank sprites.
     *
     * @param x Initial x-coordinate of the tank
     * @param y Initial y-coordinate of the tank
     */
    public TankBase(double x, double y) {
        this.x = x;
        this.y = y;

        InputStream i1 = getClass().getResourceAsStream("/whiteTank1.png");
        InputStream i2 = getClass().getResourceAsStream("/whiteTank2.png");
        frames[0] = new Image(i1);
        frames[1] = new Image(i2);

        view = new ImageView(frames[0]);
        view.setFitWidth(tankbaseSize);
        view.setFitHeight(tankbaseSize);
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    /**
     * Sets the horizontal velocity and adjusts tank's facing direction accordingly.
     *
     * @param vx Horizontal velocity
     */
    @Override
    public void setVx(double vx) {
        this.vx = vx;
        if      (vx > 0) { view.setRotate(0);   tanklooktowardstox = 1;  tanklooktowardstoy = 0; }
        else if (vx < 0) { view.setRotate(180); tanklooktowardstox = -1; tanklooktowardstoy = 0; }
    }

    /**
     * Sets the vertical velocity and adjusts tank's facing direction accordingly.
     *
     * @param vy Vertical velocity
     */
    @Override
    public void setVy(double vy) {
        this.vy = vy;
        if      (vy > 0) { view.setRotate(90);  tanklooktowardstox = 0;  tanklooktowardstoy = 1; }
        else if (vy < 0) { view.setRotate(-90); tanklooktowardstox = 0;  tanklooktowardstoy = -1; }
    }

    /**
     * Updates tank's animation and position.
     *
     * @param now Current timestamp in nanoseconds
     */
    @Override
    public void update(long now) {
        // animasyon
        if (vx != 0 || vy != 0) {
            if (lastFrameTime == 0) lastFrameTime = now;
            if (now - lastFrameTime >= framedur) {
                currentFrame = (currentFrame + 1) % frames.length;
                view.setImage(frames[currentFrame]);
                lastFrameTime = now;
            }
        } else {
            currentFrame = 0;
            view.setImage(frames[0]);
            lastFrameTime = 0;
        }

        // hareket
        x += vx * tankbaseSpeed;
        y += vy * tankbaseSpeed;
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    /**
     * Returns the Node representing the tank's image view.
     *
     * @return ImageView of the tank
     */
    @Override
    public Node getImageView() { return view; }

    @Override
    public double getX()  { return x; }

    @Override
    public double getY()  { return y; }

    @Override
    public void setX(double x) { this.x = x; view.setTranslateX(x); }

    @Override
    public void setY(double y) { this.y = y; view.setTranslateY(y); }

    @Override
    public double getWidth() { return this.tankbaseSize; }

    @Override
    public double getHeight() { return this.tankbaseSize; }

    /**
     * Gets the x-direction the tank is facing.
     *
     * @return -1, 0, or 1
     */
    public int getTankLookTowardstoX() { return tanklooktowardstox; }

    /**
     * Gets the y-direction the tank is facing.
     *
     * @return -1, 0, or 1
     */
    public int getTankLookTowardstoY() { return tanklooktowardstoy; }
}

/**
 * Represents the player-controlled tank.
 * Uses yellow-colored sprite images.
 */
class PlayerTankBase extends TankBase {

    /**
     * Constructs a PlayerTankBase at given coordinates and loads yellow tank sprites.
     *
     * @param x Initial x-coordinate
     * @param y Initial y-coordinate
     */
    public PlayerTankBase(double x, double y) {
        super(x, y);

        // yellow tank sprites
        InputStream i1 = getClass().getResourceAsStream("/yellowTank1.png");
        InputStream i2 = getClass().getResourceAsStream("/yellowTank2.png");
        frames[0] = new javafx.scene.image.Image(i1);
        frames[1] = new javafx.scene.image.Image(i2);
    }

    /**
     * Updates the player's tank state.
     *
     * @param now Current timestamp in nanoseconds
     */
    @Override
    public void update(long now) {
        super.update(now);
    }
}

/**
 * Represents an enemy tank with basic AI for movement and shooting.
 */
class EnemyTankBase extends TankBase {

    private static final long DIR_CHANGE_INTERVAL = GameSettings.directionChangeTime;
    private static final long FIRE_INTERVAL_MIN   = GameSettings.fireMinTime;
    private static final long FIRE_INTERVAL_MAX   = GameSettings.fireMaxTime;

    private long lastDirChange = 0;
    private long nextFireTime  = 0;
    private final Random rnd = new Random();

    /**
     * Constructs an EnemyTankBase at the specified coordinates.
     *
     * @param x Initial x-coordinate
     * @param y Initial y-coordinate
     */
    public EnemyTankBase(double x, double y) {
        super(x, y);
    }

    /**
     * Updates tank AI: random direction change and firing logic.
     * Ensures tank stays within map bounds.
     *
     * @param now Current timestamp in nanoseconds
     */
    @Override
    public void update(long now) {
        if (now - lastDirChange >= DIR_CHANGE_INTERVAL) {
            lastDirChange = now;
            int dir = rnd.nextInt(4);
            switch (dir) {
                case 0: setVx( 1); setVy( 0); break;
                case 1: setVx(-1); setVy( 0); break;
                case 2: setVx( 0); setVy( 1); break;
                case 3: setVx( 0); setVy(-1); break;
            }
        }

        super.update(now);

        // keep within map bounds
        double cx = Math.max(0, Math.min(getX(), Tank2025.mapwidth - tankbaseSize));
        double cy = Math.max(0, Math.min(getY(), Tank2025.mapheight - tankbaseSize));
        setX(cx); setY(cy);

        // randomly fire
        if (now >= nextFireTime) {
            long interval = FIRE_INTERVAL_MIN +
                    Math.abs(rnd.nextLong() % (FIRE_INTERVAL_MAX - FIRE_INTERVAL_MIN));
            nextFireTime = now + interval;
            Tank2025.controlEnemyFires(this);
        }
    }
}
