package com.github.diplombmstu.vrg;

import com.github.diplombmstu.vrg.common.ExceptionUtils;
import com.github.diplombmstu.vrg.communication.CommunicationManager;
import com.github.diplombmstu.vrg.communication.packaging.bodies.SetImageCommand;
import com.github.diplombmstu.vrg.streaming.DesktopStreamer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class VrgApplication extends Application
{
    private static final Logger LOGGER = Logger.getLogger(VrgApplication.class.getName());
    private DesktopStreamer desktopStreamer;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        initializeLogging();

//        try
//        {
//            desktopStreamer = new DesktopStreamer(8080);
//            desktopStreamer.start();
//        }
//        catch (AWTException | InterruptedException e)
//        {
//            LOGGER.severe(String.format("Failed to start desktop streaming. %s", e.getMessage()));
//            e.printStackTrace();
//        }

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

    public void stop() throws Exception
    {
        desktopStreamer.stop();
    }

    private void initializeLogging()
    {
        Path path = Paths.get(String.format("%s/.vrg/log", System.getProperty("user.home")));

        try
        {
            if (!Files.exists(path))
            {
                LOGGER.info("Application data directory doesn't exist. Creating folder tree...");
                Files.createDirectories(path);
            }
        }
        catch (IOException e)
        {
            LOGGER.severe(String.format("Cannot create application data directories. %s", e));
        }

        try
        {
            LogManager.getLogManager()
                    .readConfiguration(VrgApplication.class.getResourceAsStream("/logging.properties"));
        }
        catch (IOException e)
        {
            LOGGER.severe(String.format("Could not setup logger configuration: %s", ExceptionUtils.buildStackTrace(e)));
        }
    }
}
