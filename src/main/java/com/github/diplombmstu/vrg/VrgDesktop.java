package com.github.diplombmstu.vrg;

import com.github.diplombmstu.vrg.communication.CommunicationManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VrgDesktop extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            CommunicationManager communicationManager = new CommunicationManager();
            communicationManager.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        VBox root = new VBox();
        Button button = new Button();
        button.setOnMouseClicked(event ->
                                 {

                                 });

        root.getChildren().add(button);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
