package main;

import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

public class Maze {

    StackPane root = new StackPane();
    Stage stage;


    private int n; //Dimension of the maze, width and height
    private boolean[][] visited; // Check if the tile has been visited
    private boolean[][] up; // Detect wall on north direction
    private boolean[][] down; // Detect wall on south direction
    private boolean[][] left; // Detect wall on west direction
    private boolean[][] right; // Detect wall on east direction

    public Maze(Stage stage) {
        BorderPane border = new BorderPane();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(border, screenSize.width * 0.8, screenSize.height * 0.8);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        System.out.print("Up");
                        break;
                    case DOWN:
                        System.out.print("Down");
                        break;
                    case LEFT:
                        System.out.print("Left");
                        break;
                    case RIGHT:
                        System.out.print("Right");
                        break;
                }
            }
        }); /*This isnt going to much but add the key listener to the maze game, we need to implement the game
        and then assign jobs for the keys to move the character around the maze*/
        stage.setScene(scene);
        stage.show();

    }
}