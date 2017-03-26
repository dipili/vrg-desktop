package com.github.diplombmstu.converter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * TODO add comment
 */
public class MainWindowController
{
    private final static Logger LOGGER = Logger.getLogger(MainWindowController.class.getName());
    @FXML
    public TextField inputImageTextField;
    @FXML
    public TextField outputImageTextField;
    private Stage stage;

    public void reset(MouseEvent mouseEvent)
    {
        inputImageTextField.clear();
        outputImageTextField.clear();
    }

    public void convert(MouseEvent mouseEvent) throws URISyntaxException
    {
        Path inputPath = Paths.get(inputImageTextField.getText());
        Path outputPath = Paths.get(outputImageTextField.getText());

        try
        {
            Converter converter = new ConverterToStereo();
            converter.convert(inputPath, outputPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.severe(e.getMessage());
        }

        LOGGER.info("Conversion has been completed.");
    }

    public void selectInputImage(MouseEvent mouseEvent)
    {
        File file = getFile("Open an input image file");
        if (file == null)
            return;

        inputImageTextField.setText(file.getAbsolutePath());
    }

    public void selectOutputImage(MouseEvent mouseEvent)
    {
        File file = getFile("Open an output image file");
        if (file == null)
            return;

        outputImageTextField.setText(file.getAbsolutePath());
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    private File getFile(String title)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(stage);
    }
}
