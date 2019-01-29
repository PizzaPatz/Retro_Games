package tankMaze;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Explosion {


    private double xPos;
    private double yPos;
    private double rotation;
    private ImageView node = new ImageView();
    private CarrotCollisionDelegate delegate;
    public Tank.TankTeam team;

    public Explosion(double xPos, double yPos, GridPane grid) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
        javafx.scene.image.Image image;
        image = new javafx.scene.image.Image("tankMaze/explosion.png");
        node.setImage(image);
        node.setFitHeight(30);
        node.setFitWidth(30);
        node.setCache(true);
        updatePosition();
        node.setRotate(rotation-180.0);
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(0.15), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                grid.getChildren().remove(node);
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

    }

    public Node getNode() {
        return node;
    }

    private void updatePosition() {
        node.setTranslateX(xPos);
        node.setTranslateY(yPos);
    }

    public double getxPos(){
        return this.xPos;
    }
    public double getyPos(){
        return this.yPos;
    }






}