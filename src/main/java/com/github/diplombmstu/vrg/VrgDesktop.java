package com.github.diplombmstu.vrg;

import com.github.diplombmstu.vrg.communication.CommunicationManager;
import javafx.application.Application;
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
            communicationManager.start(10230);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
