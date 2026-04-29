import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

public class Bullet implements GameObject,ISize {

    public static final int bulletSize = GameSettings.bulletSize;
    private static double bulletspeed = GameSettings.bulletspeed;
    private boolean playerBullet = true;
    private double x, y;
    private final int dirX, dirY;
    private final ImageView bulletImageView;

    public Bullet(double startX, double startY, int directionx, int directiony) {
        this.x = startX;
        this.y = startY;
        this.dirX = directionx;
        this.dirY = directiony;
        InputStream inputStream = getClass().getResourceAsStream("/bullet.png");
        bulletImageView = new ImageView(new Image(inputStream));
        bulletImageView.setFitWidth(bulletSize);
        bulletImageView.setFitHeight(bulletSize);
        bulletImageView.setTranslateX(x);
        bulletImageView.setTranslateY(y);
    }

    public boolean isPlayersBullet() { return playerBullet; }

    public void setPlayerBullet(boolean playerBullet) { this.playerBullet = playerBullet; }

    @Override
    public void update(long now) {
        x += dirX * bulletspeed;
        y += dirY * bulletspeed;
        bulletImageView.setTranslateX(x);
        bulletImageView.setTranslateY(y);
    }

    @Override
    public Node getImageView()  { return bulletImageView; }

    @Override
    public double getX()   { return x; }

    @Override
    public double getY()   { return y; }

    @Override
    public void setX(double x) { this.x = x; bulletImageView.setTranslateX(x); }

    @Override
    public void setY(double y) { this.y = y; bulletImageView.setTranslateY(y); }

    @Override
    public double getWidth() {
        return this.bulletSize;
    }

    @Override
    public double getHeight() {
        return this.bulletSize;
    }
}
