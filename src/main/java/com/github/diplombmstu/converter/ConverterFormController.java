package com.github.diplombmstu.converter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

public class ConverterFormController
{
    private static final Logger LOGGER = Logger.getLogger(ConverterFormController.class.getName());

    @FXML
    public TextField outputTextField;
    public TextField screenWidthSmTextField;
    public TextField screenWidthPxTextField;
    public TextField baseTextField;

    @FXML
    private ImageView inputImageView;

    @FXML
    private ImageView depthMapImageView;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField depthMapTextField;

    @FXML
    private Button resultButton;

    @FXML
    private TextField inputTextField;

    @FXML
    private Label statusLabel;

    private Stage stage;

    public ConverterFormController()
    {
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    void browseInput()
    {
        File file = getFile("Выбирите изображение для конвертации");
        if (file == null)
            return;

        inputTextField.setText(file.getAbsolutePath());
        loadInputPreview();
    }

    @FXML
    void browseDepthMap()
    {
        File file = getFile("Выбирите файл карты глубины");
        if (file == null)
            return;

        depthMapTextField.setText(file.getAbsolutePath());
        loadDepthMapPreview();
    }

    @FXML
    void loadInputPreview()
    {
        Path path = Paths.get(inputTextField.getText());
        if (!Files.exists(path))
        {
            depthMapImageView.setImage(null);
            return;
        }

        updateStatus("Loading input preview");

        Image image = new Image("file:///" + path.toFile().getAbsolutePath());
        inputImageView.setImage(image);

        cleanStatus();
    }

    @FXML
    void loadDepthMapPreview()
    {
        Path path = Paths.get(depthMapTextField.getText());
        if (!Files.exists(path))
        {
            depthMapImageView.setImage(null);
            return;
        }

        updateStatus("Loading depth map preview");

        Image image = new Image("file:///" + path.toFile().getAbsolutePath());
        depthMapImageView.setImage(image);

        cleanStatus();
    }

    @FXML
    void browseOutput()
    {
        File file = getFile("Выбирите куда сохранить результат конвертации");
        if (file == null)
            return;

        outputTextField.setText(file.getAbsolutePath());
    }

    @FXML
    void convert()
    {
        LOGGER.info("Starting converting");

        Path inputPath = Paths.get(inputTextField.getText());
        Path depthMap = Paths.get(depthMapTextField.getText());

        Path output = Paths.get(outputTextField.getText().endsWith(".png") ?
                                outputTextField.getText() :
                                String.format("%s.png", outputTextField.getText()));

        File outputFile = output.toFile();
        if (outputFile.exists() && !confirmRewriting())
            return;
        else
        {
            if (outputFile.exists())
                outputFile.delete();
        }

        try
        {
            Converter3d converter = new Converter3d(Double.valueOf(baseTextField.getText()),
                                                    Double.valueOf(screenWidthSmTextField.getText()),
                                                    Double.valueOf(screenWidthPxTextField.getText()));

            converter.convert(inputPath, depthMap, output);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.severe(e.getMessage());

            showError(e.getMessage());

            return;
        }

        LOGGER.info("Conversion has been completed.");
    }

    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean confirmRewriting()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Вы уверены, что хотите перезаписать существующие файлы?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private void cleanStatus()
    {
        statusLabel.setText("");
    }

    private void updateStatus(String message)
    {
        LOGGER.info(message);
        statusLabel.setText(message);
    }

    private File getFile(String title)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showOpenDialog(stage);
    }
}
