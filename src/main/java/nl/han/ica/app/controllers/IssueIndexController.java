package nl.han.ica.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import nl.han.ica.app.models.JobProcessingService;
import nl.han.ica.core.Issue;
import nl.han.ica.core.Job;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles the list of detected issues in the main screen. Instructs the issue detail view to show the selected issue.
 * Reuses the detail view controller to increase performance.
 */
public class IssueIndexController extends BaseController {

    private Job job;
    private IssueResolveController issueResolveController;

    @FXML
    protected ListView<Issue> issues;
    @FXML
    protected Pane contentPane;
    @FXML
    protected Pane resolvePane;

    /**
     * Instantiate a new IssueIndexController.
     *
     * @param job The job that contains the issues to display.
     */
    public IssueIndexController(Job job) {
        this.job = job;
    }

    private void showProgressPopup(JobProcessingService service) {
        ProgressController progressController = new ProgressController(service);

        final Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Finding issues...");

        Scene progressScene = new Scene(progressController.getView());
        progressStage.setScene(progressScene);

        service.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                progressStage.show();
            }
        });
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                progressStage.hide();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeResolvePane();
        initializeIssueList();

        JobProcessingService jobProcessingService = new JobProcessingService(job);
        showProgressPopup(jobProcessingService);
        jobProcessingService.start();
    }

    /**
     * Initializes the resolve pane.
     */
    private void initializeResolvePane() {
        issueResolveController = new IssueResolveController(job);
        resolvePane = issueResolveController.getView();
        resolvePane.setVisible(false);
        contentPane.getChildren().add(resolvePane);
    }

    /**
     * Initializes the issue list.
     */
    private void initializeIssueList() {
        issues.setItems(job.getIssues());
        issues.setCellFactory(new Callback<ListView<Issue>, ListCell<Issue>>() {
            @Override
            public ListCell<Issue> call(ListView<Issue> list) {
                return new IssueCell();
            }
        });
        issues.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Issue>() {
            @Override
            public void changed(ObservableValue<? extends Issue> observable, Issue oldIssue, Issue newIssue) {
                if (null != newIssue) {
                    logger.info("Setting issue to detail view: " + newIssue);
                    resolvePane.setVisible(true);
                    issueResolveController.setIssue(newIssue);
                } else {
                    resolvePane.setVisible(false);
                }
            }
        });
        job.getIssues().addListener(new ListChangeListener<Issue>() {
            @Override
            public void onChanged(Change<? extends Issue> change) {
                if (job.getIssues().size() > 0) {
                    logger.info("Selecting first issue in issue list");
//                    issues.getSelectionModel().select(0);
                }
            }
        });
    }

    /**
     * Builds and gets the required view.
     *
     * @return The view.
     */
    @Override
    public Parent getView() {
        try {
            return buildView("/views/issue_index.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

    private static class IssueCell extends ListCell<Issue> {

        @Override
        protected void updateItem(Issue issue, boolean empty) {
            super.updateItem(issue, empty);
            if (null != issue) {
                setText(issue.getStrategy().getName());
            }
        }

    }

}