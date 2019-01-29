package main;

import billiardBunnies.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import tankMaze.Tank;
import tankMaze.TankCollisionDelegate;
import tankMaze.TankMazeBoard;
import tankMaze.TankMazeController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BilliardBunnies implements BunnyCollisionDelegate, FireballCollisionDelegate {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double topPadding = 80.0;
    double height = 0;
    GridPane grid;
    private final Stage stage;
    private final MasterController presentingController;
    final int numCols = 10;
    final int numRows = 10;
    BunniesBoard gameboard;
//    Tank tank0;
    Bunny bunny;
    Timeline timer;
    int frameCount = 0;
    Fireball bunnyFireball;
    private BilliardBunnies delegate;
    double collisionIndex = 0;
    boolean reflect = false;
    double collideY;
    double collideX;
    int exitX, exitY;
    double B_OFFSET = 84;
    double x_OFFSET = 10;
    double y_OFFSET = 40;



    private HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();
    Scene scene;
    BorderPane boarderPane;

    public BilliardBunnies(Stage stage, MasterController presentingController) {

        this.stage = stage;
        this.presentingController = presentingController;
        gameboard = new BunniesBoard();

        double width = screenSize.width * 0.8;
        height = screenSize.height * 0.8;

        boarderPane = createBorderPane(width, height);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("tank.png"));
        } catch (IOException e) {
        }


        setupGrid();
        insertImageViews();


        boarderPane.setCenter(grid);
        this.scene = new Scene(boarderPane, width, height);
        scene.getStylesheets().add("mazeGame/ExitButton.css");
        //addKeyListenersToScene();

        this.stage.setScene(scene);
        this.stage.setResizable(false);

//        tank0 = new Tank(height - 32, height - 34, Tank.TankTeam.green, (TankCollisionDelegate) this);
//        grid.getChildren().add(tank0.getNode());

        bunny = new Bunny(height - 32, height -34, (BunnyCollisionDelegate) this);
        grid.getChildren().add(bunny.getNode());
        boarderPane.requestFocus();
        addKeyListenersToScene();
//        fireball = new Fireball(0,0,180, (FireballCollisionDelegate) this);
//        grid.getChildren().add(fireball.getNode());
        beginTimer(scene);
    }

    private BorderPane createBorderPane(double width, double height) {
        BorderPane b = new BorderPane();
        StackPane left = sidePane(width, height);
        StackPane right = sidePane(width, height);
        left.getChildren().add(createMenuButton());

        b.setLeft(left);
        b.setRight(right);
        return b;
    }

    private void insertImageViews() {
        float height = (float) grid.getHeight();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                BunniesBoard.Tile tile = this.gameboard.board[i][j];
                grid.add(tile.imageView(), j, i);
            }
        }
    }

    private void setupGrid() {
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

    private StackPane sidePane(double width, double height) {
        StackPane pane = new StackPane();
        pane.setStyle(Style.BirdStuffzBlueDark);
        pane.setMinWidth((width - height) / 2.0);
        pane.setMaxWidth((width - height) / 2.0);
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setPadding(new javafx.geometry.Insets(0, 0, 8, 20));
        return pane;
    }

    private void exit() {
        presentingController.showScene();
    }

    private void addKeyListenersToScene() {
        BilliardBunnies weakController = this;
        this.boarderPane.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        bunny.beginForwardMovement();
                        break;
                    case DOWN:
                        bunny.beginReverseMovement();
                        break;
                    case LEFT:
                        bunny.beginRotateLeft();
                        break;
                    case RIGHT:
                        bunny.beginRotateRight();
                        break;
                    default:
                        break;
                }
            }});

        this.boarderPane.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        bunny.endForwardMovement();
                        break;
                    case DOWN:
                        bunny.endReverseMovement();
                        break;
                    case LEFT:
                        bunny.endRotateLeft();
                        break;
                    case RIGHT:
                        bunny.endRotateRight();
                        break;
                    case SHIFT:
//                        if (tank0Carrot != null) {
//                            grid.getChildren().remove(tank0Carrot.getNode());
//                        }
//                        tank0Carrot = tank0.launchCarrot(weakController);
//                        player2Ammo.setText("Ammo: "+Integer.toString(tank0.getAmmo()));
//                        if (tank0Carrot == null) {
//                            break;
//                        }
//                        grid.getChildren().add(tank0Carrot.getNode());
//                        System.out.println("Tank 2 Ammo: " + tank0.getAmmo());
                        break;
                    case SPACE:
                        exitX = 0;
                        exitY = 0;
                        if (bunnyFireball != null) {
                            grid.getChildren().remove(bunnyFireball.getNode());
                        }
                        bunnyFireball = bunny.launchFireball(weakController);
//                        player1Ammo.setText("Ammo: "+Integer.toString(tank1.getAmmo()));
                        if (bunnyFireball == null) {
                            break;
                        }
                        grid.getChildren().add(bunnyFireball.getNode());
//                        System.out.println("Tank 1 Ammo: " + tank1.getAmmo());
                    default:
                        break;
                }
//                switch (event.getText()) {
//                    case "w":
//                        tank1.endForwardMovement();
//                        break;
//                    case "s":
//                        tank1.endReverseMovement();
//                        break;
//                    case "a":
//                        tank1.endRotateLeft();
//                        break;
//                    case "d":
//                        tank1.endRotateRight();
//                        break;
//                }
                boarderPane.requestFocus();
            }
        });
    }

    private void beginTimer(Scene scene) {

        timer = new Timeline(new KeyFrame(Duration.seconds(0.00666666667), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (frameCount%3 == 0) {
                    bunny.moveIfNeeded();
                    //tank1.moveIfNeeded();
                }
                if (frameCount == 12){
                    bunny.rotateIfNeeded();
                  //  tank1.rotateIfNeeded();
                }
                if(bunnyFireball != null){
                    bunnyFireball.moveIfNeed();
                }
//                if (tank0Carrot != null) {
//                    tank0Carrot.moveIfNeed();
//                }
//
//                if (tank1Carrot != null) {
//                    tank1Carrot.moveIfNeed();
//                }
                frameCount += 1;
                if (frameCount == 24) {
                    frameCount = 0;
                }
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

    }


    @Override
    public boolean canMoveToPosition(double x, double y) {
        double size = 30.0;
        Point tankCenter = new Point((int) (x+(size/2.0)), (int) (y+(size/2.0)+30));
        double halfSize = size/2.0;
        // check boarders
        if (tankCenter.getX() < 0-halfSize-1) {
            bunny.setxPos(840);
            return false;
        } else if (tankCenter.getX() > 840+halfSize) {
            bunny.setxPos(0-halfSize);
            return false;
        }
        if (tankCenter.getY() < 0-halfSize-1) {
            bunny.setyPos(840-halfSize-40.0);
            return false;
        } else if (tankCenter.getY() > 840+halfSize) {
            bunny.setyPos(0-halfSize);
            return false;
        }
        int xSpot = (int) Math.floor(tankCenter.getX()/84.0);
        int ySpot = (int) Math.floor(tankCenter.getY()/84.0);
        if (xSpot > 9 || ySpot > 9 || xSpot < 0 || ySpot < 0) {
            return true;
        }


        BunniesBoard.Tile tile = this.gameboard.board[ySpot][xSpot];
        switch (tile) {
            case Vertical:
                System.out.println("Horizontal");
                break;
            case Horizontal:
                System.out.println("Horizontal");
                break;
            case DiagonalUpLeft:
                System.out.println("DiagonalUpLeft");
                break;
            case DiagonalUpRight:
                System.out.println("DiagonalUpRight");
                break;
            case Empty:
                System.out.println("Empty");
                break;
        }

        return true;
    }

    @Override
    public boolean didMoveToPosistion(Fireball fireball, double x, double y, double rotation) {
        double size = 30.0;
        Point fireballCenter = new Point((int) (x+(size/2.0)), (int) (y+(size/2.0)+25));
        // check boarders
        double halfSize = size/2.0;
        if (fireballCenter.getX() < 0-halfSize-1 && bunnyFireball != null) {
            bunnyFireball.setxPos(840);
            return false;
        } else if (fireballCenter.getX() > 840+halfSize && bunnyFireball != null) {
            bunnyFireball.setxPos(0-halfSize);
            return false;
        }
        if (fireballCenter.getY() < 0-halfSize-1 && bunnyFireball != null) {
            bunnyFireball.setyPos(840-halfSize-40.0);
            return false;
        } else if (fireballCenter.getY() > 840+halfSize && bunnyFireball != null) {
            bunnyFireball.setyPos(0-halfSize);
            return false;
        }
        int xSpot2 = (int) Math.floor(fireballCenter.getX()/84.0);
        int ySpot2 = (int) Math.floor(fireballCenter.getY()/84.0);
        if (xSpot2 > 9 || ySpot2 > 9 || xSpot2 < 0 || ySpot2 < 0) {
            return true;
        }

        BunniesBoard.Tile tile = this.gameboard.board[ySpot2][xSpot2];

        // Check if the fireball has changed its tile
        if(exitX != xSpot2 || exitY != ySpot2){
            reflect = false;
        }
        System.out.println("Rotation ----->   " + positiveAngle(rotation) +" location: "+ (x+x_OFFSET) % B_OFFSET + " - "+ (y+y_OFFSET) % B_OFFSET);

        switch (tile) {
            case Vertical:
                System.out.println("Fireball enters: Vertical");
                if (positiveAngle(rotation) == 22.5 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 337.5, xSpot2, ySpot2);
                    break;
                } else if (positiveAngle(rotation) == 45 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 315, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 67.5 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 292.5, xSpot2, ySpot2);
                    break;
                }
                else if (positiveAngle(rotation) == 90 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 270, xSpot2, ySpot2);
                    break;
                }
                else if (positiveAngle(rotation) == 112.5 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 247.5, xSpot2, ySpot2);
                    break;
                } else if (positiveAngle(rotation) == 135 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 225, xSpot2, ySpot2);
                    break;
                } else if (positiveAngle(rotation) == 157.5 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 202.5, xSpot2, ySpot2);
                    break;

//                else if(positiveAngle(rotation) == 180 && reflect == false){
//                    collisionIndex += 2;
//                    collisionIndex = entersReflectionTile(collisionIndex, 0, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;

                }else if(positiveAngle(rotation) == 202.5 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 157.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 225 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 135, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 247.5 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 112.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 270 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 90, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 292.5 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 67.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 315 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 45, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 337.5 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 22.5, xSpot2, ySpot2);
                    break;
                }
                break;
            case Horizontal:
                System.out.println("Fireball enters: Horizontal");
                if(positiveAngle(rotation) == 0 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 180, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 22.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 157.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 45 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 135, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 67.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 112.5, xSpot2, ySpot2);
                    break;
                }
//                else if(positiveAngle(rotation) == 90 && reflect == false){
//                    collisionIndex += 1;
//                    collisionIndex = entersReflectionTile(collisionIndex, 270, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;
//                }
                else if(positiveAngle(rotation) == 112.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 67.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 135 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex,  45, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 157.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 22.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 180 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 0, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 202.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 337.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 225 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 315, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 247.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 292.5, xSpot2, ySpot2);
                    break;
                }
//                else if(positiveAngle(rotation) == 270 && reflect == false){
//                    collisionIndex += 50;
//                    collisionIndex = entersReflectionTile(collisionIndex, 90, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;
//                }
                else if(positiveAngle(rotation) == 292.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 247.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 315 && reflect == false){
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 225, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 337.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 202.5, xSpot2, ySpot2);
                    break;
                }

                break;
            case DiagonalUpLeft:
                System.out.println("Fireball enters: DiagonalUpLeft");
                if(positiveAngle(rotation) == 0 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 270, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 22.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 247.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 45 && reflect == false) {
                    collisionIndex += 1;
                    collisionIndex = entersReflectionTile(collisionIndex, 225, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 67.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 202.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 90 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 180, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 112.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 157.5, xSpot2, ySpot2);
                    break;
                }
//                else if(positiveAngle(rotation) == 135 && reflect == false){
//                    collisionIndex += 2;
//                    collisionIndex = entersReflectionTile(collisionIndex,  315, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;
//                }
                else if(positiveAngle(rotation) == 157.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 112.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 180 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 90, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 202.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 67.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 225 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 45, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 247.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 22.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 270 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 0, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 292.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 337.5, xSpot2, ySpot2);
                    break;
                }
//                else if(positiveAngle(rotation) == 315 && reflect == false){
//                    collisionIndex += 2;
//                    collisionIndex = entersReflectionTile(collisionIndex, 135, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;
//                }
                else if(positiveAngle(rotation) == 337.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 292.5, xSpot2, ySpot2);
                    break;
                }
                break;
            case DiagonalUpRight:
                System.out.println("Fireball enters: DiagonalUpRight");
                if(positiveAngle(rotation) == 0 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 90, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 22.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 67.5, xSpot2, ySpot2);
                    break;
                }
//                else if(positiveAngle(rotation) == 45 && reflect == false) {
//                    collisionIndex += 1;
//                    collisionIndex = entersReflectionTile(collisionIndex, 225, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;
//                }
                else if(positiveAngle(rotation) == 67.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 22.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 90 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 0, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 112.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 337.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 135 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex,  315, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 157.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 292.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 180 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 270, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 202.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 247.5, xSpot2, ySpot2);
                    break;
                }
//                else if(positiveAngle(rotation) == 225 && reflect == false){
//                    collisionIndex += 2;
//                    collisionIndex = entersReflectionTile(collisionIndex, 45, xSpot2, ySpot2);
////                    if(collisionIndex == 0) reflect = true;
//                    break;
//                }
                else if(positiveAngle(rotation) == 247.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 202.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 270 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 180, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 292.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 157.5, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 315 && reflect == false){
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 135, xSpot2, ySpot2);
                    break;
                }
                else if(positiveAngle(rotation) == 337.5 && reflect == false) {
                    collisionIndex += 2;
                    collisionIndex = entersReflectionTile(collisionIndex, 112.5, xSpot2, ySpot2);
                    break;
                }
                break;
            case Empty:
                System.out.println("Fireball enters: Empty");
                break;
        }


        return true;
    }

    // Return current collision index, if if executed, the collision index is reset
    private double entersReflectionTile(double collisionIndex, double targetAngle, int xSpot2, int ySpot2){
        if(collisionIndex >= 24){
            collideX = bunnyFireball.getxPos();
            collideY = bunnyFireball.getyPos();
            grid.getChildren().remove(bunnyFireball.getNode());
            exitX = xSpot2;
            exitY = ySpot2;
            reflect = true;
            bunnyFireball = new Fireball(collideX,collideY, targetAngle, (FireballCollisionDelegate) this);
            grid.getChildren().add(bunnyFireball.getNode());
            return  0;
        }
        return collisionIndex;
    }

    private double positiveAngle(double rotation){
        if(rotation <0){ // negative angle
            rotation = 360 + rotation;
            while(rotation >= 360 || rotation < 0){
                rotation = 360 + rotation;
            }
        }else if(rotation >= 360){ // positive angle
            rotation = 360 - rotation;
            while(rotation >= 360 || rotation < 0){
                rotation = 360 - rotation;
            }
        }
        return rotation;
    }



}