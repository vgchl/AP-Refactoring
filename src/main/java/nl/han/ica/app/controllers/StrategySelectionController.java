package nl.han.ica.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import nl.han.ica.core.Job;
import nl.han.ica.core.strategies.ReplaceMagicNumber;
import nl.han.ica.core.strategies.Strategy;
import nl.han.ica.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all interaction on the source and strategy selection screen.
 */
public class StrategySelectionController extends BaseController {
    private static String FILES_SELECTION_TITLE = "Select source files";

    private Job job;
    private Scene scene;

    private ArrayList<Strategy> strategyList = new ArrayList<>();

    @FXML
    protected VBox strategyOptions;

    @FXML
    private Label selectedFile;

    @FXML
    private Label selectedFilePath;

    @FXML
    public Button analyzeButton;


    /**
     * Initialize a new StrategySelectionController.
     *
     * @param scene The scene in which the controller's view is located.
     * @param job   The job to select source files and strategies for.
     */
    public StrategySelectionController(Scene scene, Job job) {
        this.scene = scene;
        this.job = job;

        fillStrategyList();
    }

    @Override
    public Parent getView() {
        try {
            return buildView("/views/strategy_selection.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void selectSourceFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FILES_SELECTION_TITLE);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));

        job.setFiles(fileChooser.showOpenMultipleDialog(null));

        onSourceFilesSelected();
    }

    @FXML
    private void selectSourceDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(FILES_SELECTION_TITLE);

        File directory = directoryChooser.showDialog(null);
        job.setFiles(FileUtil.listFilesRecursively(directory, ".java"));

        onSourceFilesSelected();
    }

    @FXML
    private void analyze(ActionEvent event) {
        ResolveIssuesController resolveIssuesController = new ResolveIssuesController(job);
        scene.setRoot(resolveIssuesController.getView());
    }

    private void onSourceFilesSelected() {
        if (job.getFiles().size() > 0) {
            selectedFilePath.setText(formatFileList(job.getFiles()));
            selectedFilePath.setVisible(true);
            selectedFile.setVisible(true);
            analyzeButton.setDisable(false);
        } else {
            selectedFilePath.setVisible(false);
            selectedFile.setVisible(false);
            analyzeButton.setDisable(true);
        }
    }

    private String formatFileList(List<File> files) {
        StringBuilder fileList = new StringBuilder();
        for (File file : files) {
            fileList.append(file.getName()).append("\n");
        }
        return fileList.toString();
    }

    private void fillStrategyList() {
        strategyList.add( new ReplaceMagicNumber( null  ) );

        for (Strategy strategy : strategyList) {
            CheckBox cb = new CheckBox();

            cb.setId(strategy.getName());
            cb.setText(strategy.getName());

            strategyOptions.getChildren().add(cb);
        }

    }

}