package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MasterController masterController = new MasterController(primaryStage);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Game Master");
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
