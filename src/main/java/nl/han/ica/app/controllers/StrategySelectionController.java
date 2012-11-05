package nl.han.ica.app.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import nl.han.ica.core.Job;
import nl.han.ica.core.strategies.ReplaceMagicNumber;
import nl.han.ica.core.strategies.Strategy;
import nl.han.ica.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import nl.han.ica.core.SourceHolder;

/**
 * Handles all interaction on the source and strategy selection screen.
 */
public class StrategySelectionController extends BaseController {

    private final static String FILES_SELECTION_TITLE = "Select source files";

    private Job job;
    private Scene scene;
    private List<Strategy> strategyList;

    @FXML
    protected VBox strategyOptions;
    @FXML
    protected Label selectedFile;
    @FXML
    protected Label selectedFilePath;
    @FXML
    protected Button analyzeButton;

    /**
     * Initialize a new StrategySelectionController.
     *
     * @param scene The scene in which the controller's view is located.
     * @param job   The job to select source files and strategies for.
     */
    public StrategySelectionController(Scene scene, Job job) {
        this.scene = scene;
        this.job = job;
        strategyList = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeStrategyCheckboxList();
    }

    @Override
    public Parent getView() {
        try {
            return buildView("/views/strategy_selection.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

    /**
     * Opens a file browser where (multiple) files could be selected.
     *
     * @param event The event that is passed by the view.
     */
    @FXML
    public void selectSourceFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FILES_SELECTION_TITLE);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));

        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (null != files) {
            job.setFiles(files);
        } else {
            job.getFiles().clear();
        }

        onSourceFilesSelected();
    }

    /**
     * Opens a file browser where (multiple) folders could be selected.
     *
     * @param event The event that is passed by the view.
     */
    @FXML
    public void selectSourceDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(FILES_SELECTION_TITLE);

        File directory = directoryChooser.showDialog(null);
        if (null != directory) {
            job.setFiles(FileUtil.listFilesRecursively(directory, ".java"));
        }
        else {
            job.getFiles().clear();
        }

        onSourceFilesSelected();
    }

    /**
     * This event gets executed when files or folders are hovered on the application.
     *
     * @param event The event that is passed by the view.
     */
    @FXML
    public void handleFilesDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    /**
     * This event gets executed when files or folders are dropped on the application.
     *
     * @param event The event that is passed by the view.
     */
    @FXML
    public void handleFilesDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            job.getFiles().clear();
            for (File file : db.getFiles()) {
                if (file.isDirectory()) {
                    for (File directoryFile : FileUtil.listFilesRecursively(file, ".java")) {
                        job.addFile(directoryFile);
                    }
                } else if (file.getName().endsWith(".java")) {
                    job.addFile(file);
                }
            }
            onSourceFilesSelected();
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     * This action is executed when the Analyze button is clicked.
     *
     * @param event The event that is passed by the view.
     */
    @FXML
    public void analyze(ActionEvent event) {
        ObservableList<Node> checkboxes = strategyOptions.getChildren();

        for (int i = 0; i < checkboxes.size(); i++) {
            CheckBox c = (CheckBox) checkboxes.get(i);
            if( c.isSelected() ) {
                Strategy strategy = strategyList.get(i);
                job.getStrategies().add(strategy);
            }
        }

        IssueIndexController issueIndexController = new IssueIndexController(job);
        scene.setRoot(issueIndexController.getView());
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

    private void initializeStrategyCheckboxList() {
        strategyList.add(new ReplaceMagicNumber());

        for (Strategy strategy : strategyList) {
            CheckBox checkBox = new CheckBox();
            checkBox.setText(strategy.getName());
            checkBox.setSelected(true);
            strategyOptions.getChildren().add(checkBox);
        }
    }

}