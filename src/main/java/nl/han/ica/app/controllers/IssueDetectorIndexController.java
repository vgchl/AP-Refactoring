package nl.han.ica.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.HideMethodDetector;
import nl.han.ica.core.issue.detector.MagicNumberDetector;
import nl.han.ica.core.issue.solver.HideMethodSolver;
import nl.han.ica.core.issue.solver.MagicNumberSolver;
import nl.han.ica.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Handles all interaction on the source and strategy selection screen.
 */
public class IssueDetectorIndexController extends BaseController {

    private final static String FILES_SELECTION_TITLE = "Select source files";

    private Job job;
    private Scene scene;

    @FXML
    protected VBox strategyOptions;
    @FXML
    protected Label selectedFile;
    @FXML
    protected Label selectedFilePath;
    @FXML
    protected Button analyzeButton;

    /**
     * Initialize a new IssueDetectorIndexController.
     *
     * @param scene The scene in which the controller's view is located.
     * @param job   The job to select source files and strategies for.
     */
    public IssueDetectorIndexController(Scene scene, Job job) {
        this.scene = scene;
        this.job = job;
        initializeIssueDetectors();
    }

    private void initializeIssueDetectors() {
        Set<IssueDetector> issueDetectors = job.getIssueDetectionService().getDetectors();
        issueDetectors.add(new MagicNumberDetector());
        issueDetectors.add(new HideMethodDetector());

        Set<IssueSolver> solvers = job.getIssueSolvingService().getIssueSolverLocator().getSolvers();
        solvers.add(new MagicNumberSolver());
        solvers.add(new HideMethodSolver());
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
    private void selectSourceFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FILES_SELECTION_TITLE);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));

        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (null != files) {
            for (File file : files) {
                job.getSourceFiles().add(new SourceFile(file));
            }
        } else {
            job.getSourceFiles().clear();
        }

        onSourceFilesSelected();
    }

    /**
     * Opens a file browser where (multiple) folders could be selected.
     *
     * @param event The event that is passed by the view.
     */
    @FXML
    private void selectSourceDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(FILES_SELECTION_TITLE);

        File directory = directoryChooser.showDialog(null);
        if (null != directory) {
            List<File> files = FileUtil.listFilesRecursively(directory, ".java");
            for (File file : files) {
                job.getSourceFiles().add(new SourceFile(file));
            }
        } else {
            job.getSourceFiles().clear();
        }
        System.out.println("xx");
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
            job.getSourceFiles().clear();
            for (File file : db.getFiles()) {
                if (file.isDirectory()) {
                    for (File directoryFile : FileUtil.listFilesRecursively(file, ".java")) {
                        job.getSourceFiles().add(new SourceFile(directoryFile));
                    }
                } else if (file.getName().endsWith(".java")) {
                    job.getSourceFiles().add(new SourceFile(file));
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
        IssueIndexController issueIndexController = new IssueIndexController(job);
        scene.setRoot(issueIndexController.getView());
    }

    private void onSourceFilesSelected() {
        if (job.getSourceFiles().size() > 0) {
            selectedFilePath.setText(formatFileSet(job.getSourceFiles()));
            selectedFilePath.setVisible(true);
            selectedFile.setVisible(true);
        } else {
            selectedFilePath.setVisible(false);
            selectedFile.setVisible(false);
        }
        updateCanAnalyze();
    }

    private void updateCanAnalyze() {
        analyzeButton.setDisable(!job.canProcess());
    }

    private String formatFileSet(Set<SourceFile> sourceFiles) {
        StringBuilder fileList = new StringBuilder();
        for (SourceFile sourceFile : sourceFiles) {
            fileList.append(sourceFile.getFile().getName()).append("\n");
        }
        return fileList.toString();
    }

    private void initializeStrategyCheckboxList() {
        for (final IssueDetector issueDetector : job.getIssueDetectionService().getDetectors()) {
            final CheckBox checkBox = new CheckBox();
            checkBox.setText(issueDetector.getTitle());
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldSelected, Boolean newSelected) {
                    if (newSelected) {
                        job.getIssueDetectionService().getDetectors().add(issueDetector);
                    } else {
                        job.getIssueDetectionService().getDetectors().remove(issueDetector);
                    }
                    updateCanAnalyze();
                }
            });
            checkBox.setSelected(true);
            strategyOptions.getChildren().add(checkBox);
        }
    }

}