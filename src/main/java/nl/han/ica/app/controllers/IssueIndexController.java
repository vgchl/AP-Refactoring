/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


package nl.han.ica.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import nl.han.ica.app.models.JobProcessingService;
import nl.han.ica.core.Job;
import nl.han.ica.core.issue.Issue;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;

/**
 * Handles the list of detected issues in the main screen. Instructs the issue detail view to show the selected issue.
 */
public class IssueIndexController extends BaseController {

    private Job job;
    private IssueSolveController issueResolveController;
    private JobProcessingService jobProcessingService;

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
        jobProcessingService = new JobProcessingService(job);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeResolvePane();
        initializeIssueList();

        jobProcessingService.start();
    }

    /**
     * Initializes the resolve pane.
     */
    private void initializeResolvePane() {
        issueResolveController = new IssueSolveController(job);
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
        protected void updateItem(final Issue issue, boolean empty) {
            super.updateItem(issue, empty);
            if (null != issue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setText(issue.getDetector().getTitle());
                    }
                });
            }
        }

    }

}
