import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

/**
 * Represents a wall object in the game environment.
 * A wall can be either destructible or indestructible and has a fixed size and appearance.
 */
public class Wall implements GameObject {

    /**
     * Constant size of the wall in pixels.
     */
    static final int wallSize = GameSettings.wallSize;

    /**
     * Visual representation of the wall.
     */
    private final ImageView wallImageView;

    private double x, y;

    /**
     * Indicates whether the wall can be destroyed.
     */
    private final boolean isDestructible;

    /**
     * Constructs a new wall object at the specified coordinates and sets its destructibility.
     * Loads the wall image and sets its visual properties.
     *
     * @param x              Initial x-coordinate
     * @param y              Initial y-coordinate
     * @param isDestructible Whether the wall can be destroyed
     */
    public Wall(double x, double y, boolean isDestructible) {
        InputStream is = getClass().getResourceAsStream("/wall.png");
        Image img = new Image(is);
        wallImageView = new ImageView(img);
        wallImageView.setFitWidth(wallSize);
        wallImageView.setFitHeight(wallSize);
        wallImageView.setTranslateX(x);
        wallImageView.setTranslateY(y);

        this.isDestructible = isDestructible;
    }

    /**
     * Update method required by the GameObject interface.
     * Currently, walls are static and do not change over time.
     *
     * @param now Current time in nanoseconds
     */
    @Override
    public void update(long now) { }

    /**
     * Returns the visual representation (ImageView) of the wall.
     *
     * @return Node representing the wall
     */
    @Override
    public Node getImageView() {
        return wallImageView;
    }

    /**
     * Gets the x-coordinate of the wall.
     *
     * @return X position
     */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of the wall.
     *
     * @return Y position
     */
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Sets the x-coordinate of the wall.
     *
     * @param x New x position
     */
    @Override
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the wall.
     *
     * @param y New y position
     */
    @Override
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Indicates whether the wall is destructible.
     *
     * @return true if destructible, false otherwise
     */
    public boolean isDestructible() {
        return isDestructible;
    }
}
