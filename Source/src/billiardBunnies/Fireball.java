package billiardBunnies;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Fireball {


    private double xPos;
    private double yPos;
    private double rotation;
    private FireballCollisionDelegate delegate;
    private ImageView node = new ImageView();


    public Fireball(double xPos, double yPos, double rotation, FireballCollisionDelegate delegate) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
        this.delegate = delegate;
        javafx.scene.image.Image image;
        image = new javafx.scene.image.Image("billiardBunnies/fireball.gif");
        node.setImage(image);
        node.setFitHeight(50);
        node.setFitWidth(50);
        node.setCache(true);
        updatePosition();
        node.setRotate(rotation-90.0);
    }

    public Node getNode() {
        return node;
    }

    public double returnAngle(){ return rotation;}

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

    public double getRotation(){
        return rotation;
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

//    public void projectile(double x, double y){
//        xPos += x;
//        yPos += y;
//        updatePosition();
//    }
//
//    public void assignRotation(double delta){
//        node.setRotate(delta-90.0);
//    }



}