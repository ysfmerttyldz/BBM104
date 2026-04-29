import javafx.scene.Node;

interface GameObject {
    void update(long now);
    Node getImageView();
    double getX();
    double getY();
    void setX(double x);
    void setY(double y);
}

interface IMovable {
    void setVx(double vx);
    void setVy(double vy);
}

interface ISize{
    double getWidth();
    double getHeight();
}
