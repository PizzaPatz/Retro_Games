package concentration;

import chessGame.Board;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.MasterController;
import javafx.scene.paint.Color;
import highScore.*;

import javax.swing.*;
import java.awt.*;

public class ConcentrationGUI extends JPanel{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double topPadding = 80.0;
    concentration.Board concBoard;
    private final MasterController presentingController;


    public ConcentrationGUI(Stage stage, MasterController presentingController){
        this.presentingController = presentingController;
        Double boardSize = (screenSize.height*0.975);
        BorderPane border = new BorderPane();
        Scene scene = new Scene(border, screenSize.width*0.8,screenSize.height*0.8);
        scene.getStylesheets().add("mazeGame/ExitButton.css");

        stage.setScene(scene);
        concBoard = new concentration.Board();
        stage.setResizable(false);
        border.requestFocus();

        SwingNode gameNode = new SwingNode();
        gameNode.setStyle("-fx-background-color: #1C0000;");
        gameNode.setContent(concBoard);
        border.setCenter(gameNode);

        border.setLeft(leftPane());
        border.setRight(rightPane());
        border.setTop(horizontalPadding());
        border.setBottom(horizontalPadding());



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

    private Pane leftPane(){
        double gameHeight = (screenSize.height*0.8)-(topPadding*2.0);
        StackPane p = new StackPane();
        p.setPrefWidth(((screenSize.width*0.8)-gameHeight)/2.0);
        p.setAlignment(Pos.TOP_LEFT);
        p.setStyle("-fx-background-color: #1C3044;");
        p.getChildren().add(createMenuButton());
        return p;
    }

    private Pane rightPane(){
        double gameHeight = (screenSize.height*0.8)-(topPadding*2.0);
        StackPane p = new StackPane();
        p.setPrefWidth(((screenSize.width*0.8)-gameHeight)/2.0);
        p.setAlignment(Pos.TOP_LEFT);
        p.setStyle("-fx-background-color: #1C3044;");
        highScoreConcentration k = new highScoreConcentration();
        Label title = k.readScores();
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font: 24 arial;");
        p.getChildren().add(title);
        p.setAlignment(title, Pos.TOP_CENTER);
        return p;
    }


    private Pane verticalPadding() {
        double gameHeight = (screenSize.height*0.8)-(topPadding*2.0);
        Pane p = new Pane();
        p.setPrefWidth(((screenSize.width*0.8)-gameHeight)/2.0);
        p.setStyle("-fx-background-color: #1C3044;");
        return  p;
    }

    private Pane horizontalPadding() {
        Pane p = new Pane();
        p.setPrefHeight(topPadding);
        p.setStyle("-fx-background-color: #1C3044;");
        return  p;
    }

    private void exit() {
        presentingController.showScene();
    }
}
