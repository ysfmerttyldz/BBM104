import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

/**
 * Abstract base class for explosion effects in the game.
 * Handles loading explosion images, positioning, and fade-out animation.
 */
abstract class ExplosionBase implements GameObject {

    private final ImageView explosionImageView;
    private boolean isDestroyed;
    private double x, y;

    /**
     * Constructs a new explosion effect with fade-out animation.
     *
     * @param x             Center X position of the explosion
     * @param y             Center Y position of the explosion
     * @param imagePath     Path to the explosion image resource
     * @param size          Size of the explosion in pixels
     * @param fadeDuration  Duration of the fade-out animation
     */
    protected ExplosionBase(double x, double y, String imagePath, double size, Duration fadeDuration) {
        this.x = x;
        this.y = y;

        // Load image and configure ImageView
        InputStream is = getClass().getResourceAsStream(imagePath);
        Image img = new Image(is);
        explosionImageView = new ImageView(img);
        explosionImageView.setFitWidth(size);
        explosionImageView.setFitHeight(size);
        explosionImageView.setTranslateX(x - size / 2);
        explosionImageView.setTranslateY(y - size / 2);

        // Apply fade-out animation
        FadeTransition ft = new FadeTransition(fadeDuration, explosionImageView);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> isDestroyed = true);
        ft.play();
    }

    /**
     * No update logic needed for explosions.
     *
     * @param now Current time in nanoseconds
     */
    @Override
    public void update(long now) {
    }

    /**
     * Gets the visual representation of the explosion.
     *
     * @return ImageView node of the explosion
     */
    @Override
    public Node getImageView() {
        return explosionImageView;
    }

    /**
     * Gets the X position of the explosion.
     *
     * @return X coordinate
     */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * Gets the Y position of the explosion.
     *
     * @return Y coordinate
     */
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Sets the X position of the explosion.
     *
     * @param x New X coordinate
     */
    @Override
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the Y position of the explosion.
     *
     * @param y New Y coordinate
     */
    @Override
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns whether the explosion has finished and should be removed.
     *
     * @return true if the explosion is complete, false otherwise
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
}

/**
 * Represents a small explosion effect used for light impacts.
 */
class SmallExplosion extends ExplosionBase {

    /**
     * Size of the small explosion.
     */
    public static final double smallExplosionSize = GameSettings.smallExplosionSize;

    /**
     * Path to the small explosion image.
     */
    private static final String imagePngFilePath = "/smallExplosion.png";

    /**
     * Duration of the small explosion fade-out.
     */
    private static final Duration DURATION = Duration.millis(GameSettings.smallExplosionDurationInMs);

    /**
     * Constructs a new SmallExplosion at the specified location.
     *
     * @param x X coordinate of the explosion center
     * @param y Y coordinate of the explosion center
     */
    public SmallExplosion(double x, double y) {
        super(x, y, imagePngFilePath, smallExplosionSize, DURATION);
    }
}

/**
 * Represents an explosion caused by the destruction of an enemy tank.
 */
class EnemyExplosion extends ExplosionBase {

    /**
     * Size of the enemy explosion.
     */
    public static final double enemyExplosionSize = GameSettings.enemyExplosionSize;

    /**
     * Path to the enemy explosion image.
     */
    private static final String enemyExplosionPngFilePath = "/explosion.png";

    /**
     * Duration of the enemy explosion fade-out.
     */
    private static final Duration explotionDuration = Duration.millis(GameSettings.enemyExplotionDurationInMs);

    /**
     * Constructs a new EnemyExplosion at the specified location.
     *
     * @param x X coordinate of the explosion center
     * @param y Y coordinate of the explosion center
     */
    public EnemyExplosion(double x, double y) {
        super(x, y, enemyExplosionPngFilePath, enemyExplosionSize, explotionDuration);
    }
}
