package tankMaze;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Carrot {


    private double xPos;
    private double yPos;
    private double rotation;
    private ImageView node = new ImageView();
    private CarrotCollisionDelegate delegate;
    public Tank.TankTeam team;

    public Carrot(double xPos, double yPos, double rotation, Tank.TankTeam team, CarrotCollisionDelegate delegate) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
        this.delegate = delegate;
        this.team = team;
        javafx.scene.image.Image image;
        image = new javafx.scene.image.Image("tankMaze/Carrot.png");
        node.setImage(image);
        node.setFitHeight(30);
        node.setFitWidth(30);
        node.setCache(true);
        updatePosition();
        node.setRotate(rotation-180.0);
    }

    public Node getNode() {
        return node;
    }

    public void moveIfNeed() {
        move();
    }

    private void move() {
        double angleInRadian = Math.toRadians(rotation);
        double cos = Math.cos(angleInRadian);
        double sin = Math.sin(angleInRadian);
        double yMove = cos;
        double xMove = sin;

        xPos += 2.0*xMove;
        yPos -= 2.0*yMove;

        updatePosition();
    }


    private void updatePosition() {
        node.setTranslateX(xPos);
        node.setTranslateY(yPos);
        delegate.didMoveToPosistion(this, xPos, yPos, rotation);
    }

    public double getxPos(){
        return this.xPos;
    }
    public double getyPos(){
        return this.yPos;
    }


}