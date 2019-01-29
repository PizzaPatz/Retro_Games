package racingGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.MasterController;
import main.Style;
import org.w3c.dom.css.Rect;
import tankMaze.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;

public class RacingGameController implements CartMoveDelegate {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double topPadding = 80.0;
    Pane trackPanel;
    private final Stage stage;
    private final MasterController presentingController;
    TankMazeBoard gameboard;
    private HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();
    Scene scene;
    BorderPane boarderPane;
    int[][] gameMap;
    int[][] boundryMap;

    RaceCart cart0;
    RaceCart cart1;
    RaceCart cart2;
    RaceCart cart3;


    double height = 0;
    int frameCount = 0;
    int eliminated = 0;
    Timeline timer;

    int[] carCheckpoints;

    Text p1, p2, p3;

    Button promptName1, promptName2;
    Button confirm = new Button("Confirm");
    TextField name1 = new TextField();

    int score1, score2, score3 = 0;
    Text player1Score = new Text("Score: 0");
    Text player2Score = new Text("Score: 0");
    Text player3Score = new Text("Score: 0");

    public RacingGameController(Stage stage, MasterController presentingController) {
        this.stage = stage;
        this.presentingController = presentingController;

        double width = screenSize.width * 0.8;
        height = screenSize.height * 0.8;

        boarderPane = createBorderPane(width, height);

        trackPanel = new Pane();
        trackPanel.setStyle("-fx-background-color: #233B54; -fx-background-image: url('racingGame/GameMap.png')");

        boarderPane.setCenter(trackPanel);
        this.scene = new Scene(boarderPane, width, height);
        scene.getStylesheets().add("mazeGame/ExitButton.css");

        this.stage.setScene(scene);
        this.stage.setResizable(false);
        boarderPane.requestFocus();

        cart0 = new RaceCart(0, 120, 190);
        cart0.setCartMoveDelegate((CartMoveDelegate) this);

        cart1 = new RaceCart(1, 120, 160);
        cart1.setCartMoveDelegate((CartMoveDelegate) this);

        cart2 = new RaceCart(2, 95, 190);
        cart2.setCartMoveDelegate((CartMoveDelegate) this);

        carCheckpoints = new int[]{0, 0, 0};

//        javafx.scene.shape.Rectangle rbox = new Rectangle(510,210,80,160);
//        rbox.setFill(Color.RED);
//        trackPanel.getChildren().add(rbox);

        trackPanel.getChildren().add(cart0.getNode());
        trackPanel.getChildren().add(cart1.getNode());
        trackPanel.getChildren().add(cart2.getNode());

        readMapText();
        addKeyListenersToScene();
        beginTimer(scene);

    }

    private void readMapText() {
        int[][] map = new int[840][1092];
        int[][] mapB = new int[840][1092];

        try {
            System.out.println(new File(".").getAbsoluteFile());
            Scanner scanner = new Scanner(new File("./src/racingGame/BinaryMap.txt"));
            Scanner scannerB = new Scanner(new File("./src/racingGame/BoundryMap.txt"));

            for (int i = 0; i < 839; i++) {
                String next = scanner.next();
                String nextB = scannerB.next();

                for (int j = 0; j < 1092; j++) {
                    map[i][j] = (int) next.charAt(j);
                    mapB[i][j] = (int) nextB.charAt(j);
                }
            }
            this.gameMap = map;
            this.boundryMap = mapB;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private BorderPane createBorderPane(double width, double height) {
        BorderPane b = new BorderPane();
        StackPane left = sidePane(width, height);
        left.getChildren().add(createMenuButton());


        // Player name
        p1 = new Text("Player 1");
        p1.setFill(javafx.scene.paint.Color.WHITE);
        p1.setStyle("-fx-font: 20 arial;");
        p1.setTranslateY(-75);
        left.getChildren().add(p1);
        left.setAlignment(p1, Pos.CENTER_LEFT);

        p2 = new Text("Player 2");
        p2.setFill(javafx.scene.paint.Color.WHITE);
        p2.setStyle("-fx-font: 20 arial;");
        p2.setTranslateY(-50);
        left.getChildren().add(p2);
        left.setAlignment(p2, Pos.CENTER_LEFT);

        b.setLeft(left);


        player1Score.setFill(javafx.scene.paint.Color.WHITE);
        player1Score.setStyle("-fx-font: 20 arial;");
        player1Score.setTranslateY(-75);
        player1Score.setTranslateX(150);
        left.getChildren().add(player1Score);
        left.setAlignment(player1Score, Pos.CENTER_LEFT);

        player2Score.setFill(javafx.scene.paint.Color.WHITE);
        player2Score.setStyle("-fx-font: 20 arial;");
        player2Score.setTranslateY(-50);
        player2Score.setTranslateX(150);
        left.getChildren().add(player2Score);
        left.setAlignment(player2Score, Pos.CENTER_LEFT);

        promptName1 = new javafx.scene.control.Button("Player 1");
        promptName1.getStyleClass().add("Player 1");
        promptName1.setMaxWidth(200);
        promptName1.setMaxHeight(40);
        promptName1.setAlignment(Pos.BASELINE_CENTER);
        promptName1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final Stage dialog = new Stage();
                System.out.println("test");
                dialog.initModality(Modality.APPLICATION_MODAL);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(name1);
                confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        p1.setText(name1.getText());
                    }
                });
                dialogVbox.getChildren().add(confirm);
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
        left.getChildren().add(promptName1);
        left.setAlignment(promptName1, Pos.BOTTOM_LEFT);

        return b;
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
        // pane.getChildren().add(createMenuButton());
        return pane;
    }

    private void exit() {
        presentingController.showScene();
    }

    private void beginTimer(Scene scene) {

        timer = new Timeline(new KeyFrame(Duration.seconds(0.00666666667), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (frameCount % 3 == 0) {
                    cart0.moveIfNeeded();
                    cart1.moveIfNeeded();
                    cart2.moveIfNeeded();
                }
                if (frameCount == 12){
                    cart0.rotateIfNeeded();
                    cart1.rotateIfNeeded();
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
        this.boarderPane.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        cart0.endForwardMovement();
                        break;
                    case DOWN:
                        cart0.endReverseMovement();
                        break;
                    case LEFT:
                        cart0.endRotateLeft();
                        break;
                    case RIGHT:
                        cart0.endRotateRight();
                        break;
                    default:
                        break;
                }
                switch (event.getText()) {
                    case "w":
                        cart1.endForwardMovement();
                        break;
                    case "s":
                        cart1.endReverseMovement();
                        break;
                    case "a":
                        cart1.endRotateLeft();
                        break;
                    case "d":
                        cart1.endRotateRight();
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
                        cart0.beginForwardMovement();
                        break;
                    case DOWN:
                        cart0.beginReverseMovement();
                        break;
                    case LEFT:
                        cart0.beginRotateLeft();
                        break;
                    case RIGHT:
                        cart0.beginRotateRight();
                        break;
                    default:
                        break;
                }
                switch (event.getText()) {
                    case "w":
                        cart1.beginForwardMovement();
                        break;
                    case "s":
                        cart1.beginReverseMovement();
                        break;
                    case "a":
                        cart1.beginRotateLeft();
                        break;
                    case "d":
                        cart1.beginRotateRight();
                        break;
                }
                boarderPane.requestFocus();
            }
        });

    }

    @Override
    public boolean isInTrack(double x, double y) {
        int mapVal = gameMap[(int) y][(int) x];
        if (mapVal == 48) {
            return true;
        } else if (mapVal == 49) {
            return false;
        }
        System.out.println("Found other!!!");
        System.out.println(mapVal);
        return false;
    }


    private java.awt.Rectangle check0 = new java.awt.Rectangle(40,190,160,80);
    private java.awt.Rectangle check1 = new java.awt.Rectangle(500,650,80,160);
    private java.awt.Rectangle check2 = new java.awt.Rectangle(910,360,160,80);
    private java.awt.Rectangle check3 = new java.awt.Rectangle(510,210,80,160);

    @Override
    public boolean canMoveToPosition(int cartNumber, double x, double y) {
        checkCarCheckpoint(cartNumber, x, y);
        if (x < 0 || y < 0) {
            return false;
        }
        if (x > (trackPanel.getWidth()-20) || y > (trackPanel.getHeight()-20)) {
            return false;
        }
        int mapVal = boundryMap[(int) y][(int) x];
        if (mapVal == 48) {
            return true;
        } else if (mapVal == 49) {
            return false;

        }
        return false;
    }

    private void checkCarCheckpoint(int number, double x, double y) {
        if (carCheckpoints[number]%4 == 0 && check1.contains(x, y)) {
            System.out.println("Check 1 on cart "+ number);
            carCheckpoints[number] += 1;
            if(number == 0) {
                score1 += 100;
                player1Score.setText("Score "+score1);
            } else if(number == 1){
                score2 += 100;
                player2Score.setText("Score "+score2);
            }
        } else if (carCheckpoints[number]%4 == 1 && check2.contains(x, y)) {
            System.out.println("Check 2 on cart "+ number);
            carCheckpoints[number] += 1;
            if(number == 0) {
                score1 += 200;
                player1Score.setText("Score "+score1);
            } else if(number == 1){
                score2 += 200;
                player2Score.setText("Score "+score2);
            }
        } else if (carCheckpoints[number]%4 == 2 && check3.contains(x, y)) {
           System.out.println("Check 3 on cart "+ number);
            carCheckpoints[number] += 1;
            if(number == 0) {
                score1 += 300;
                player1Score.setText("Score "+score1);
            } else if(number == 1){
                score2 += 300;
                player2Score.setText("Score "+score2);
            }
        } else if (carCheckpoints[number]%4 == 3 && check0.contains(x, y)) {
            System.out.println("Finished");
            carCheckpoints[number] += 1;
            if(number == 0) {
                score1 += 500;
                player1Score.setText("Score "+score1);
            } else if (number == 1) {
                score2 += 500;
                player2Score.setText("Score " + score2);
            }
            checkForElimination();;
            if(((carCheckpoints[number]/3) == carCheckpoints.length-1)){
                carDidFinish();
            }
//            carDidFinish();
            }

    }

    private void checkForElimination() {
        int maxPosition = 0;
        for (int i = 0; i < carCheckpoints.length; i++) {
            if (carCheckpoints[i] > maxPosition) {
                maxPosition = carCheckpoints[i];
            }
        }
        int lap = (maxPosition/4);
        int playersForLap = carCheckpoints.length-lap;
        int passedPlayers = 0;
        for (int i = 0; i < carCheckpoints.length; i++) {
            if (((int)carCheckpoints[i]/4) == lap) {
                passedPlayers += 1;
            }
        }
        if (playersForLap == passedPlayers) {
            for (int i = 0; i < carCheckpoints.length; i++) {
                if (((int) carCheckpoints[i] / 4) != lap) {
                    disableCar(i);
                }
            }
        }
    }

    private void disableCar(int carNumber) {
        switch (carNumber) {
            case 0:
                cart0.disabled = true;
                break;
            case 1:
                cart1.disabled = true;
                break;
            case 2:
                cart2.disabled = true;
                break;
                default:
                    break;
        }
    }

    public void carDidFinish() {
        timer.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        int finishCheckPoints = (carCheckpoints.length-1)*4;
        if (carCheckpoints[0] == 8) {
            System.out.println("Red Wins");
            alert.setHeaderText("Red Bat Wins!");
            delayRecord();
        } else if (carCheckpoints[1] == 8) {
            System.out.println("Green Wins");
            alert.setHeaderText("Green Bat Wins!");
        } else if (carCheckpoints[2] == 8) {
            System.out.println("Yellow Wins");
            alert.setHeaderText("Yellow Bat Wins!");
        }
//        cart2.scannerx.close();
//        cart2.scannery.close();
//        cart2.scannerr.close();

        alert.setOnHidden(evt -> this.exit());
        alert.show();
    }

    private void delayRecord() {
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(2.0), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cart0.print_linex.close();
                cart0.print_liney.close();
                cart0.print_liner.close();
            }
        }));
        delay.setCycleCount(1);
        delay.play();
    }

}