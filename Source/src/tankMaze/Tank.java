package tankMaze;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import java.awt.*;



public class Tank {

    public enum TankTeam {
        green, brown;

        public void getImage() {
            // should return the tank sprite image based on team
            switch (this) {
                case green:
                    return;
                case brown:
                    return;
            }

        }
    }

    public enum MoveDirection {
        forward, reverse;
    }

    private int health;
    private double xPos;
    private double yPos;
    private double rotation;
    TankTeam team;
    boolean isMoving = false;
    private ImageView node = new ImageView();
    private boolean isMovingForward = false;
    private boolean isReversing = false;
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    private int numCarrots = 30;

    public TankCollisionDelegate delegate;

    public Tank(double xPos, double yPos, TankTeam team, TankCollisionDelegate delegate) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.health = 100;
        this.team = team;
        this.delegate = delegate;
        javafx.scene.image.Image image;
        if (team == TankTeam.green) {
            rotation = 0;
            image = new javafx.scene.image.Image("tankMaze/Green-Tank.png");
        } else {
            rotation = 180;
            image = new javafx.scene.image.Image("tankMaze/Brown-Tank.png");
        }
        node.setImage(image);
        node.setFitHeight(30);
        node.setFitWidth(30);
        node.setCache(true);
        node.setTranslateX(xPos);
        node.setTranslateY(yPos);
        updateRotation();
    }

    public Node getNode() {
        return node;
    }

    public void beginForwardMovement() {
        isReversing = false;
        isMovingForward = true;
    }

    public void endForwardMovement() {
        isMovingForward = false;
    }

    public void beginReverseMovement() {
        isMovingForward = false;
        isReversing = true;
    }

    public void endReverseMovement() {
        isReversing = false;
    }

    public void beginRotateLeft() {
        isRotatingRight = false;
        isRotatingLeft = true;
    }

    public void endRotateLeft() {
        isRotatingLeft = false;
    }

    public void beginRotateRight() {
        isRotatingLeft = false;
        isRotatingRight = true;    }

    public void endRotateRight() {
        isRotatingRight = false;
    }

    public void moveIfNeeded() {
        if (isMovingForward) {
            this.move(MoveDirection.forward);
        } else if (isReversing) {
            this.move(MoveDirection.reverse);
        }
    }

    public void rotateIfNeeded(){
        if (isRotatingLeft) {
            this.rotateLeft();
        } else if (isRotatingRight) {
            this.rotateRight();
        }
    }

    public int getAmmo()
    {
        return numCarrots;
    }

    public Carrot launchCarrot(CarrotCollisionDelegate delegate) {
        if (numCarrots < 1) {
            return  null;
        }
        numCarrots -= 1;
        return new Carrot(xPos, yPos, rotation, team, delegate);

    }

    private void move(MoveDirection direction) {
        double angleInRadian = Math.toRadians(rotation);
        double cos = Math.cos(angleInRadian);
        double sin = Math.sin(angleInRadian);
        double yMove = cos;
        double xMove = sin;

        double newX = xPos;
        double newY = yPos;
        if (direction == MoveDirection.forward) {
            newX += 2.0*xMove;
            newY -= 2.0*yMove;
        } else {
            newX -= 2.0*xMove;
            newY += 2.0*yMove;
        }

        if(delegate.canMoveToPosition(newX,newY)){
            xPos = newX;
            yPos = newY;
            updatePosition();
        }

    }

    private void rotateLeft() {
        rotation -= 22.5;
        updateRotation();
    }

    private void rotateRight() {
        rotation += 22.5;
        updateRotation();
    }

    private void updatePosition() {

        node.setTranslateX(xPos);
        node.setTranslateY(yPos);

    }

    private void updateRotation() {
        node.setRotate(rotation);
    }

    public double getxPos(){
        return this.xPos;
    }

    public double getyPos(){
        return this.yPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public int getHealth(){
        return this.health;
    }

    public void takeDamage() {
        this.health -= 10;
        if (this.health < 1) {
            delegate.tankHealthDepleted(this);
        }
    }

}
