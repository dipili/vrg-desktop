package com.github.diplombmstu.vrg;

import com.github.diplombmstu.vrg.communication.CommunicationManager;
import com.github.diplombmstu.vrg.communication.packaging.bodies.SetImageCommand;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class VrgDesktop extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        CommunicationManager communicationManager = new CommunicationManager();

        try
        {
            communicationManager.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        VBox root = new VBox();
        Button button = new Button("Load image");
        button.setOnMouseClicked(event ->
                                 {
                                     FileChooser fileChooser = new FileChooser();
                                     fileChooser.setTitle("Send image");
                                     File file = fileChooser.showOpenDialog(primaryStage);

                                     if (!file.exists())
                                         return;

                                     try
                                     {
                                         communicationManager.getCommandSender()
                                                 .send(new SetImageCommand(file.getAbsolutePath()));
                                     }
                                     catch (IOException e)
                                     {
                                         e.printStackTrace(); // TODO handle
                                     }
                                 });

        root.getChildren().add(button);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
