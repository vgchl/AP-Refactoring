package nl.han.ica.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
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
     * @param job The job that contains the issues to display.
     */
    public IssueIndexController(Job job) {
        this.job = job;
        job.process();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeResolvePane();
        initializeIssueList();
    }

    private void initializeResolvePane() {
        issueResolveController = new IssueResolveController(job);
        resolvePane = issueResolveController.getView();
        resolvePane.setVisible(false);
        contentPane.getChildren().add(resolvePane);
    }

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
                    issueResolveController.setIssue(newIssue);
                    resolvePane.setVisible(true);
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
                    issues.getSelectionModel().select(0);
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

    private class IssueCell extends ListCell<Issue> {

        @Override
        protected void updateItem(Issue issue, boolean empty) {
            super.updateItem(issue, empty);
            if (null != issue) {
                setText(issue.getStrategy().getName());
            }
        }

    }

}