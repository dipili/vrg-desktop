package com.github.diplombmstu.vrg;

import com.github.diplombmstu.vrg.common.ExceptionUtils;
import com.github.diplombmstu.vrg.streaming.DesktopStreamer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class VrgDesktop extends Application
{
    private static final Logger LOGGER = Logger.getLogger(VrgDesktop.class.getName());

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            LogManager.getLogManager().readConfiguration(VrgDesktop.class.getResourceAsStream("/logging.properties"));
        }
        catch (IOException e)
        {
            LOGGER.severe(String.format("Could not setup logger configuration: %s", ExceptionUtils.buildStackTrace(e)));
        }

        try
        {
            DesktopStreamer desktopStreamer = new DesktopStreamer(8080);
            desktopStreamer.start();
        }
        catch (AWTException e)
        {
            e.printStackTrace(); // TODO
        }

//        CommunicationManager communicationManager = new CommunicationManager();
//
//        try
//        {
//            communicationManager.start();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        VBox root = new VBox();
        Button button = new Button("Load image");
        button.setOnMouseClicked(event ->
                                 {
//                                     FileChooser fileChooser = new FileChooser();
//                                     fileChooser.setTitle("Send image");
//                                     File file = fileChooser.showOpenDialog(primaryStage);
//
//                                     if (!file.exists())
//                                         return;
//
//                                     try
//                                     {
//                                         communicationManager.getCommandSender()
//                                                 .send(new SetImageCommand(file.getAbsolutePath()));
//                                     }
//                                     catch (IOException e)
//                                     {
//                                         e.printStackTrace(); // TODO handle
//                                     }
                                 });

        root.getChildren().add(button);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
