package nl.han.ica.app.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Handles all interaction on the source and strategy selection screen.
 */
public class StrategySelectionController extends BaseController {
    private static String FILES_SELECTION_TITLE = "Select source files";

    private Job job;
    private Scene scene;
    private List<Strategy> strategyList = new ArrayList<>();

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
    }

    @Override
    public Parent getView() {
        try {
            Parent p = buildView("/views/strategy_selection.fxml");

            fillStrategyCheckboxList();

            return p;
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

        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (null != files) {
            job.setFiles(files);
        } else {
            job.getFiles().clear();
        }

        onSourceFilesSelected();
    }

    @FXML
    private void selectSourceDirectory(ActionEvent event) {
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

    @FXML
    public void handleFilesDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }
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
                        job.getFiles().add(directoryFile);
                    }
                } else if (file.getName().endsWith(".java")) {
                    job.getFiles().add(file);
                }
            }
            onSourceFilesSelected();
        }
        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void analyze(ActionEvent event) {
        ObservableList<Node> checkboxes = strategyOptions.getChildren();

        for (int i = 0; i < checkboxes.size(); i++) {
            CheckBox c = (CheckBox) checkboxes.get(i);
            if( c.isSelected() ) {
                Strategy strategy = strategyList.get(i);
                job.getStrategies().add(strategy);
            }
        }

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

    private void fillStrategyCheckboxList() {
        strategyList.add( new ReplaceMagicNumber() );
        //strategyList.add( new ReplacePublicFieldSolver() );
        for (Strategy strategy : strategyList) {
            CheckBox cb = new CheckBox();

            cb.setText(strategy.getName());
            strategyOptions.getChildren().add(cb);
        }
    }

}