package main;

import banner.BannerView;
import concentration.ConcentrationGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.BilliardBunnies;

import java.awt.*;


public class MasterController {

    enum Game {
        Maze, Mancala, Concentration, MiniChess, Tanks, Racing, Bunnies
    }

    Button startGameButton = null;

    private Stage primaryStage;
    private Scene scene;

    public MasterController(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane border = new BorderPane();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        scene = new Scene(border, screenSize.width*0.8, screenSize.height*0.8);
        scene.getStylesheets().add("main/MenuButton.css");
        border.setLeft(addSideMenu());
        border.setCenter(addSelectionPreview());
        border.setBottom(addStatusBar());

        primaryStage.setScene(scene);

    }

    private Game lastFocusedGame = null;

    private VBox addSideMenu() {

        VBox vbox = new VBox();
        vbox.setPadding(new javafx.geometry.Insets(0)); // Set all sides to 10
        vbox.setSpacing(0.0);              // Gap between nodes
        vbox.setStyle("-fx-background-color: #1C3044;");
        vbox.setPrefWidth(360);// prefWidth

        BannerView banner = new BannerView();
        vbox.setMargin(banner, new javafx.geometry.Insets(20, 0, 0, 20));
        vbox.getChildren().add(banner);

        javafx.scene.control.Button header = new javafx.scene.control.Button("Select a game");
        header.getStyleClass().add("menuHeader");
        header.setAlignment(Pos.BASELINE_LEFT);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setPadding(new javafx.geometry.Insets(8, 0, 8, 20));
        vbox.setMargin(header, new javafx.geometry.Insets(20, 0, 0, 0));

        vbox.getChildren().add(header);

        javafx.scene.control.Button options[] = new javafx.scene.control.Button[]{
                buttonForGame(Game.Maze),
                buttonForGame(Game.Mancala),
                buttonForGame(Game.Concentration),
                buttonForGame(Game.MiniChess),
                buttonForGame(Game.Tanks),
                buttonForGame(Game.Racing),
                buttonForGame(Game.Bunnies)
        };


        for (int i=0; i<7; i++) {
            VBox.setMargin(options[i], new javafx.geometry.Insets(0, 0, 0, 0));
            vbox.getChildren().add(options[i]);
            vbox.setFillWidth(true);
        }
        return vbox;
    }

    private VBox addSelectionPreview() {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #233B54;");
        vbox.setMaxWidth(Double.MAX_VALUE);// prefWidth
        vbox.setMinWidth(400);
        vbox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();
        javafx.scene.image.Image image = new javafx.scene.image.Image("main/TransparentLogo.png");
        imageView.setImage(image);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        vbox.getChildren().add(imageView);


        startGameButton = createPlayButton("Play Now");
        startGameButton.setVisible(false);
        startGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                actionPlaySelectedGame();
            }
        });;
        vbox.getChildren().add(startGameButton);


        return vbox;
    }

    private HBox addStatusBar() {
        HBox statusBar = new HBox();

        // set up status bar attributes
        statusBar.setStyle("-fx-background-color: rgba(10, 10, 10, 0.7);");

        statusBar.setAlignment(Pos.CENTER_RIGHT);
        statusBar.setSpacing(8);
        statusBar.setPrefSize(60, 20);

        // add the tiny emoji style icon to the status bar
        ImageView imageView = new ImageView();
        javafx.scene.image.Image image = new Image("main/TinyIcon.png");
        imageView.setImage(image);
        imageView.setFitHeight(14);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // Add text label to status bar
        Text label = new Text("BIRDSTUFZ");
        label.setFont(Font.font("Roboto", FontWeight.BLACK, 14));
        label.setFill(Color.WHITE);


        statusBar.getChildren().add(imageView);
        statusBar.getChildren().add(label);

        statusBar.setMargin(label, new Insets(0, 16, 0, 0));

        return statusBar;
    }

    private Button createMenuButton(String title) {
        Button button = new javafx.scene.control.Button(title);
        button.getStyleClass().add("menuButton");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.BASELINE_LEFT);
        return button;
    }

    private Button createPlayButton(String title) {
        Button button = new javafx.scene.control.Button(title);
        button.getStyleClass().add("playButton");
        button.setMaxWidth(200);
        button.setMaxHeight(40);
        button.setAlignment(Pos.BASELINE_CENTER);
        return button;
    }


    private void setLastFocusedGame(Game game) {
        if (lastFocusedGame == game) {
            scene.getRoot().requestFocus();
            game = null;
        }
        //games is null if deselected/not null past this, this is where you find out what game youre using for high score
        startGameButton.setVisible(game != null);
        lastFocusedGame = game;
    }

    private void highlightGame(Game game) {

    }

    private void endHighlightGame() {

    }

    private void actionPlaySelectedGame() {
        Game game = lastFocusedGame;
        if (game == null) {
            return;
        }
        switch (game) {
            case Maze:
                actionSelectMazeGame();
                break;
            case Mancala:
                actionSelectMancalaGame();
                break;
            case Concentration:
                actionSelectConcentrationGame();
                break;
            case MiniChess:
                actionSelectMiniChessGame();
                break;
            case Tanks:
                actionSelectTankMazeGame();
                break;
            case Racing:
                actionSelectRacingGame();
                break;
            case Bunnies:
                actionSelectBilliardGame();
                break;
        }

    }

    private void actionSelectMazeGame() {
        mazeGame.MazeGUI m = new mazeGame.MazeGUI(10, primaryStage, this);
    }

    private void actionSelectMancalaGame() {
        showAlertWithText("Selected Mancala Game");
    }

    private void actionSelectConcentrationGame() {
        ConcentrationGUI conc = new ConcentrationGUI(primaryStage, this);    }

    private void actionSelectMiniChessGame(){
        chessGame.ChessGUI c = new chessGame.ChessGUI(primaryStage);
    }

    private void actionSelectTankMazeGame(){
        tankMaze.TankMazeController tm = new tankMaze.TankMazeController(primaryStage, this);
    }

    private void actionSelectRacingGame(){
        racingGame.RacingGameController rg = new racingGame.RacingGameController(primaryStage, this);
    }

    private void actionSelectBilliardGame() {
        BilliardBunnies bb = new BilliardBunnies(primaryStage, this);
    }

    private void showAlertWithText(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(text);
        alert.show();
    }

    public void showScene() {
        primaryStage.setScene(scene);
    }

    private Button buttonForGame(Game game) {
        String title = "";
        switch (game) {
            case Maze:
                title = "Maze Game";
                break;
            case Mancala:
                title = "Mancala";
                break;
            case Concentration:
                title = "Concentration";
                break;
            case MiniChess:
                title = "MiniChess";
                break;
            case Tanks:
                title = "Tank Maze";
                break;
            case Racing:
                title = "Spooky Racing";
                break;
            case Bunnies:
                title = "Billiard Bunnies";
                break;
        }
        Button button = createMenuButton(title);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                setLastFocusedGame(game);
            }
        });
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                highlightGame(game);
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                endHighlightGame();
            }
        });
        return  button;
    }


}
