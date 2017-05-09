package com.github.diplombmstu.converter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConverterForm.fxml"));
        Parent root = loader.load();
        ConverterFormController controller = loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root, 601, 466);

        primaryStage.setTitle("Image converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
