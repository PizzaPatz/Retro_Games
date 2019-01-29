package mazeGame;

import highScore.highScoreConcentration;
import highScore.highScoreMaze;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.awt.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import main.MasterController;
import main.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import static main.Style.BirdStuffzBlueDark;


/**
 * 
 * @author Krunal Shah
 * Runs the Maze game.
 *
 */
public class MazeGUI implements PlayerDelegate {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double topPadding = 80.0;
	Board mazeBoard;
	final Player player;
	private final Stage stage;
	private final MasterController presentingController;
	public static long startT;
	public static long endT;
	private double runTime;
	private highScoreMaze k = new highScoreMaze();


	/**
	 * Constructs the maze frame with a board and a player along with
	 * all the button listeners for movement.
	 * @param level
	 */
	public MazeGUI(int level, Stage stage, MasterController presentingController) {
		startT = System.nanoTime();
		this.presentingController = presentingController;
		Double boardSize = (screenSize.height*0.975);
		mazeBoard = new Board(10,10,level, boardSize.intValue());
		player = new Player(mazeBoard, this::didWin);
		BorderPane border = new BorderPane();
		Scene scene;
		scene = new Scene(border,  screenSize.width*0.8, screenSize.height*0.8);
		scene.getStylesheets().add("mazeGame/ExitButton.css");



		this.stage = stage;
		this.stage.setScene(scene);
		this.stage.setResizable(false);
		border.requestFocus();

		SwingNode gameNode = new SwingNode();
		gameNode.setStyle("-fx-background-color: #1C0000;");
		gameNode.setContent(mazeBoard);
		mazeBoard.setBackground(new Color(55, 72, 89));
		border.setCenter(gameNode);

		border.setLeft(leftPane());
		border.setRight(verticalPadding());
		border.setTop(horizontalPadding());
		border.setBottom(horizontalPadding());

		addKeyListenersToScene(scene);

		if (screenSize.height < 900) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Your screen may be too small to properly render this game at this time.");
			alert.show();
		}

	}

	private StackPane leftPane() {
		double gameHeight = (screenSize.height*0.8)-(topPadding*2.0);
		StackPane pane = new StackPane();
		pane.setStyle(BirdStuffzBlueDark);
		pane.setPrefWidth(((screenSize.width*0.8)-gameHeight)/2.0);
		pane.setAlignment(Pos.TOP_LEFT);
		pane.setPadding(new javafx.geometry.Insets(0, 0, 8, 20));
		pane.getChildren().add(createMenuButton());
		return pane;
	}

	private javafx.scene.control.Button createMenuButton() {
		Button button = new javafx.scene.control.Button();
		button.getStyleClass().add("exitButton");
		button.setMaxWidth(40);
		button.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				exit();
			}
		});
		return button;
	}

	private Pane verticalPadding() {
		double gameHeight = (screenSize.height*0.8)-(topPadding*2.0);
		StackPane p = new StackPane();
		p.setPrefWidth(((screenSize.width*0.8)-gameHeight)/2.0);
		p.setAlignment(Pos.TOP_LEFT);
		p.setStyle(BirdStuffzBlueDark);
		Label title = k.readScores();
		title.setTextFill(new javafx.scene.paint.Color(1.0,1.0,1.0,1));
		title.setStyle("-fx-font: 24 arial;");
		p.getChildren().add(title);
		p.setAlignment(title, Pos.TOP_CENTER);
		return p;
	}

	private Pane horizontalPadding() {
		Pane p = new Pane();
		p.setPrefHeight(topPadding);
		p.setStyle(BirdStuffzBlueDark);
		return  p;
	}

	private void addKeyListenersToScene(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case UP:
						player.moveUp(mazeBoard);
						break;
					case DOWN:
						player.moveDown(mazeBoard);
						break;
					case LEFT:
						player.moveLeft(mazeBoard);
						break;
					case RIGHT:
						player.moveRight(mazeBoard);
						break;
				}
				switch (event.getText()) {
					case "w":
						player.moveUp(mazeBoard);
						break;
					case "s":
						player.moveDown(mazeBoard);
						break;
					case "a":
						player.moveLeft(mazeBoard);
						break;
					case "d":
						player.moveRight(mazeBoard);
						break;
				}
			}
		});
	}

	// Player Delegate

	@Override
	public void didWin() {
		double temp = endT-startT;
		double nano = 1000000000.00;
		runTime = temp/nano;
		k.changeScore(runTime);
		presentingController.showScene();
	}

	public static void changeEndTime(long end){
		endT = end;
	}

	private void exit() {
		presentingController.showScene();
	}
}

