package tankMaze;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.MasterController;
import main.Style;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class TankMazeController implements TankCollisionDelegate, CarrotCollisionDelegate {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double topPadding = 80.0;
    GridPane grid;
    private final Stage stage;
    private final MasterController presentingController;
    final int numCols = 19 ;
    final int numRows = 19 ;
    TankMazeBoard gameboard;
    private HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();
    Scene scene;
    BorderPane boarderPane;

    Tank tank0;
    Tank tank1;
    Carrot tank0Carrot;
    Carrot tank1Carrot;

    double height = 0;

    int frameCount = 0;

    Rectangle health1;
    Rectangle health2;

    Text player1Ammo = new Text("Ammo: 30");
    Text player2Ammo = new Text("Ammo: 30");
    Timeline timer;


    public TankMazeController(Stage stage, MasterController presentingController) {
        this.stage = stage;
        this.presentingController = presentingController;
        gameboard = new TankMazeBoard(numRows, numCols);

        double width = screenSize.width*0.8;
        height = screenSize.height*0.8;

        boarderPane = createBorderPane(width, height);

        grid = new GridPane();
        grid.setStyle("-fx-background-color: #233B54;");
        grid.setGridLinesVisible(true);

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            grid.getRowConstraints().add(rowConst);
        }

        boarderPane.setCenter(grid);
        this.scene = new Scene(boarderPane,  width, height);
        scene.getStylesheets().add("mazeGame/ExitButton.css");
        addKeyListenersToScene();

        this.stage.setScene(scene);
        this.stage.setResizable(false);
        boarderPane.requestFocus();
        insertImageViews();


        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("tank.png"));
        } catch (IOException e) {
        }

        BufferedImage buffImg = null;
        try {
            buffImg = ImageIO.read(new File("tank.png"));
        }catch(IOException e){}

        // Template
        tank0 = new Tank(height-32, height-34, Tank.TankTeam.green, (TankCollisionDelegate) this);
        tank1 = new Tank(10, 10, Tank.TankTeam.brown, (TankCollisionDelegate) this);

        grid.getChildren().add(tank0.getNode());
        grid.getChildren().add(tank1.getNode());
        beginTimer(scene);
    }

    private BorderPane createBorderPane(double width, double height) {
        BorderPane b = new BorderPane();
        StackPane left = sidePane(width, height);
        StackPane right = sidePane(width, height);
        left.getChildren().add(createMenuButton());

        health1 = healthBar();
        left.getChildren().add(health1);
        left.setAlignment(health1, Pos.CENTER_LEFT);

        health2 = healthBar();
        right.getChildren().add(health2);
        right.setAlignment(health2, Pos.CENTER_LEFT);

        b.setLeft(left);
        b.setRight(right);




        Text p1 = new Text("Player 1");
        p1.setFill(javafx.scene.paint.Color.WHITE);
        p1.setStyle("-fx-font: 20 arial;");
        p1.setTranslateY(-50);
        left.getChildren().add(p1);
        left.setAlignment(p1, Pos.CENTER_LEFT);

        player1Ammo.setFill(javafx.scene.paint.Color.WHITE);
        player1Ammo.setStyle("-fx-font: 16 arial;");
        left.getChildren().add(player1Ammo);
        player1Ammo.setTranslateY(-25);
        left.setAlignment(player1Ammo, Pos.CENTER_LEFT);

        Text p2 = new Text("Player 2");
        p2.setFill(javafx.scene.paint.Color.WHITE);
        p2.setStyle("-fx-font: 20 arial;");
        p2.setTranslateY(-50);
        right.getChildren().add(p2);
        right.setAlignment(p2, Pos.CENTER_LEFT);

        player2Ammo.setFill(javafx.scene.paint.Color.WHITE);
        player2Ammo.setStyle("-fx-font: 16 arial;");
        right.getChildren().add(player2Ammo);
        player2Ammo.setTranslateY(-25);
        right.setAlignment(player2Ammo, Pos.CENTER_LEFT);


        return b;
    }

    private StackPane sidePane(double width, double height) {
        StackPane pane = new StackPane();
        pane.setStyle(Style.BirdStuffzBlueDark);
        pane.setMinWidth((width-height)/2.0);
        pane.setMaxWidth((width-height)/2.0);
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setPadding(new javafx.geometry.Insets(0, 0, 8, 20));
       // pane.getChildren().add(createMenuButton());
        return pane;
    }

    private void insertImageViews() {
        float height = (float) grid.getHeight();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                switch (this.gameboard.board[i][j]) {
                    case path:
                        grid.add(getPathTile(height), j, i);
                        break;
                    case wall:
                        grid.add(getTile(height), j, i);
                        break;
                }
            }
        }
    }

    private Node getTile(float height) {
        VBox v = new VBox();
        return v;
    }

    private Node getPathTile(float height) {
        Node v = getTile(height);
        v.setStyle("-fx-background-color: #374859;");
        return v;
    }

    private javafx.scene.control.Button createMenuButton() {
        Button button = new javafx.scene.control.Button();
        button.getStyleClass().add("exitButton");
        button.setMaxWidth(40);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                exit();
            }
        });
        return button;
    }

    public Rectangle healthBar(){
        Rectangle amount = new Rectangle(200, 25);
        amount.setFill((javafx.scene.paint.Color.GREEN));
        return amount;
    }

    private void exit() {
        presentingController.showScene();
    }

    private void beginTimer(Scene scene) {

        timer = new Timeline(new KeyFrame(Duration.seconds(0.00666666667), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (frameCount%3 == 0) {
                    tank0.moveIfNeeded();
                    tank1.moveIfNeeded();
                }
                if (frameCount == 12){
                    tank0.rotateIfNeeded();
                    tank1.rotateIfNeeded();
                }
                if (tank0Carrot != null) {
                    tank0Carrot.moveIfNeed();
                }

                if (tank1Carrot != null) {
                    tank1Carrot.moveIfNeed();
                }
                frameCount += 1;
                if (frameCount == 24) {
                    frameCount = 0;
                }


            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

    }


    private void addKeyListenersToScene() {
        TankMazeController weakController = this;
        this.boarderPane.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        tank0.endForwardMovement();
                        break;
                    case DOWN:
                        tank0.endReverseMovement();
                        break;
                    case LEFT:
                        tank0.endRotateLeft();
                        break;
                    case RIGHT:
                        tank0.endRotateRight();
                        break;
                    case SHIFT:
                        if (tank0Carrot != null) {
                            grid.getChildren().remove(tank0Carrot.getNode());
                        }
                        tank0Carrot = tank0.launchCarrot(weakController);
                        player2Ammo.setText("Ammo: "+Integer.toString(tank0.getAmmo()));
                        if (tank0Carrot == null) {
                            break;
                        }
                        grid.getChildren().add(tank0Carrot.getNode());
                        System.out.println("Tank 2 Ammo: " + tank0.getAmmo());
                        break;
                    case SPACE:
                        if (tank1Carrot != null) {
                            grid.getChildren().remove(tank1Carrot.getNode());
                        }
                        tank1Carrot = tank1.launchCarrot(weakController);
                        player1Ammo.setText("Ammo: "+Integer.toString(tank1.getAmmo()));
                        if (tank1Carrot == null) {
                            break;
                        }
                        grid.getChildren().add(tank1Carrot.getNode());
                        System.out.println("Tank 1 Ammo: " + tank1.getAmmo());
                    default:
                        break;
                }
                switch (event.getText()) {
                    case "w":
                        tank1.endForwardMovement();
                        break;
                    case "s":
                        tank1.endReverseMovement();
                        break;
                    case "a":
                        tank1.endRotateLeft();
                        break;
                    case "d":
                        tank1.endRotateRight();
                        break;
                }
                boarderPane.requestFocus();
            }
        });

        this.boarderPane.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        tank0.beginForwardMovement();
                        break;
                    case DOWN:
                        tank0.beginReverseMovement();
                        break;
                    case LEFT:
                        tank0.beginRotateLeft();
                        break;
                    case RIGHT:
                        tank0.beginRotateRight();
                        break;
                        default:
                            break;
                }
                switch (event.getText()) {
                    case "w":
                        tank1.beginForwardMovement();
                        break;
                    case "s":
                        tank1.beginReverseMovement();
                        break;
                    case "a":
                        tank1.beginRotateLeft();
                        break;
                    case "d":
                        tank1.beginRotateRight();
                        break;
                }
                boarderPane.requestFocus();
            }
        });
    }

    @Override
    public boolean canMoveToPosition(double x, double y) {
        x+=15;
        y+=15;
        if(x < 8){
            return false;
        }
        if(x > height-8){
            return false;
        }
        if(y < 8){
            return false;
        }
        if(y > height-12){
            return false;
        }
        return !gameboard.isPointInsideWall(new Point2D[]{
                new Point2D(x, y-8),
                new Point2D(x, y+8),
                new Point2D(x-8, y),
                new Point2D(x+8, y)
        }, height/numRows);
    }

    @Override
    public void didMoveToPosistion(Carrot carrot, double x, double y, double rotation) {
        CarrotCollisionType collision = CarrotCollisionType.none;
        CarrotCollisionType collision2 = CarrotCollisionType.none;
        CarrotCollisionType collision3 = CarrotCollisionType.none;

        // Adjust x and y point of the carrot based on the direction the carrot is traveling so we are looking at the tip
        rotation = rotation%360;
        if (rotation<0) {
            rotation = 360+rotation;
        }
        if (rotation == 180) {
            x += 15;
            y += 30;
        } else if (rotation == 0) {
            x += 15;
        } else if (rotation == 90) {
            x += 30;
            y += 8;
        } else if (rotation == 270) {
            y += 8;
        } else if (rotation > 90 && rotation < 180) {
            x += 24;
            y += 24;
        } else if (rotation > 270 && rotation < 180) {
            x += 24;
            y += 8;
        } else if (rotation < 360 && rotation > 270) {
            x += 8;
            y += 8;
        } else {
            x += 8;
            y += 24;
        }

        // Check it it hit a wall
        if (x < 0) {
            collision = CarrotCollisionType.wall;
        } else if (x > height) {
            collision = CarrotCollisionType.wall;
        } else if (y < 0) {
            collision = CarrotCollisionType.wall;
        } else if (y > height) {
            collision = CarrotCollisionType.wall;
        } else if (gameboard.isPointInsideWall(new Point2D[]{
                    new Point2D(x+4, y+15)}, height/numRows)) {
                collision = CarrotCollisionType.wall;
        }
        boolean tank0Hit = false;
        boolean tank1Hit = false;
        // firgure out which carrot it is so we can handle it correctly
        if (collision == CarrotCollisionType.none) {
            if (carrot.team == Tank.TankTeam.green) {
                double tankX = tank1.getxPos();
                double tankY = tank1.getyPos();
                if (x > tankX && x < tankX+30 && y > tankY && y < tankY+30) {
                    System.out.println("Green hit Brown!!!");
                    tank1Hit = true;
                    collision = CarrotCollisionType.tank;
                }
            } else {
                double tankX = tank0.getxPos();
                double tankY = tank0.getyPos();
                if (x >= tankX && x <= tankX+30 && y >= tankY && y <= tankY+30) {
                    System.out.println("Brown hit Green!!!");
                    tank0Hit = true;
                    collision = CarrotCollisionType.tank;
                }
            }
        }

        // if it did not hit anything, don't do anything
        if (collision == CarrotCollisionType.none) {
            return;
        }


        // Remove the carrot since there was a collision
        if (carrot.team == Tank.TankTeam.green) {
            Explosion explosion = new Explosion(tank0Carrot.getxPos(), tank0Carrot.getyPos(), grid);
            grid.getChildren().add(explosion.getNode());
            grid.getChildren().remove(tank0Carrot.getNode());
            tank0Carrot = null;
        } else {
            Explosion explosion = new Explosion(tank1Carrot.getxPos(), tank1Carrot.getyPos(), grid);
            grid.getChildren().add(explosion.getNode());
            grid.getChildren().remove(tank1Carrot.getNode());
            tank1Carrot = null;
        }
        if (tank0Hit) {
            tank0.takeDamage();
            double remaining = ((double)tank0.getHealth())/100.0;
            health2.setWidth(200.0*remaining);
        } else if (tank1Hit) {
            tank1.takeDamage();
            double remaining = ((double)tank1.getHealth())/100.0;
            health1.setWidth(200.0*remaining);
        }

    }

    @Override
    public void tankHealthDepleted(Tank tank) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (tank.team == Tank.TankTeam.green) {
            System.out.println("Green machine broke");
            alert.setHeaderText("Brown tank wins!!");
        } else {
            System.out.println("Brown tank broke");
            alert.setHeaderText("Green tank wins!!");
        }
        alert.setOnHidden(evt -> this.exit());
        alert.show();
    }
}


enum CarrotCollisionType {
    wall, tank, none
}