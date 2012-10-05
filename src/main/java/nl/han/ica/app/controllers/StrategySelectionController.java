package nl.han.ica.app.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import nl.han.ica.core.Job;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Handles all interaction on the source and strategy selection screen.
 */
public class StrategySelectionController extends BaseController {

    private Job job;
    private Scene scene;
    @FXML
    private Label selectedFile;
    @FXML
    private Label selectedFilePath;
    @FXML
    private Button analyzeButton;

    /**
     * Initialize a new StrategySelectionController.
     *
     * @param scene The scene in which the controller's view is located.
     */
    public StrategySelectionController(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Parent getView() {
        return buildView("/views/strategy_selection.fxml");
    }

    @FXML
    private void selectSourceFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select source files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Source Files", "*.java"));

        job.setFiles(fileChooser.showOpenMultipleDialog(null));

        onSourceFilesSelected();
    }

    @FXML
    private void selectSourceDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select source directory");

        File directory = directoryChooser.showDialog(null);
        job.setFiles(findSourceFilesInDirectory(directory));

        onSourceFilesSelected();
    }

    @FXML
    private void analyze(ActionEvent event) throws IOException {
        ResolveIssuesController resolveIssuesController = new ResolveIssuesController(job);
        scene.setRoot(resolveIssuesController.getView());
    }

    private void onSourceFilesSelected() {
        if (job.getFiles().size() > 0) {
            StringBuilder selectedFiles = new StringBuilder();
            for (File file : job.getFiles()) {
                selectedFiles.append(file.getName() + "\n");
            }
            selectedFilePath.setText(selectedFiles.toString());
            selectedFilePath.setVisible(true);
            selectedFile.setVisible(true);
            analyzeButton.setDisable(false);
        } else {
            selectedFilePath.setVisible(false);
            selectedFile.setVisible(false);
            analyzeButton.setDisable(true);
        }
    }

    private List<File> findSourceFilesInDirectory(File directory) {
        File[] files = directory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".java");
            }
        });
        return Arrays.asList(files);
    }

}