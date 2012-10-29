package nl.han.ica.app.controllers;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nl.han.ica.app.models.IssueSolvingService;
import nl.han.ica.app.models.parameter.ParameterChangeListener;
import nl.han.ica.app.models.parameter.ParameterEvent;
import nl.han.ica.core.Issue;
import nl.han.ica.core.Job;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class IssueResolveController extends BaseController {

    private Job job;
    private Issue issue;
    private IssueResolveChangeController changeController;
    private Solution solution;
    private IssueSolvingService issueSolvingService;

    @FXML
    protected Label issueTitle;
    @FXML
    protected Label issueDescription;
    @FXML
    protected VBox issueChangesContainer;

    /**
     * Creates a Issue Resolve Controller
     *
     * @param job The job for the controller.
     */
    public IssueResolveController(Job job) {
        this.job = job;
        changeController = new IssueResolveChangeController();
        issueSolvingService = new IssueSolvingService(job, issue);

        issueSolvingService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                solution = issueSolvingService.getValue();
                showSolution(issueSolvingService.getParameters());
            }
        });
        issueSolvingService.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.fatal("FAILED SOLVING", issueSolvingService.getException());
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        issueChangesContainer.getChildren().add(changeController.getView());
    }

    private void updateView() {
        logger.info("Updating issue view");

        issueTitle.setText(issue.getStrategy().getName());
        issueDescription.setText(issue.getRuleViolation().getRule().getDescription().replaceAll("\n", " ").replaceAll("  ", ""));

        final Map<String, Parameter> parameters = new HashMap<>();
        changeController.addParameterChangeListener(new ParameterChangeListener() {
            @Override
            public void changed(ParameterEvent event) {
                issueSolvingService.setParameters(parameters);
                issueSolvingService.start();
//                showSolution(parameters);
            }
        });
        issueSolvingService.setParameters(parameters);
        issueSolvingService.start();
//        showSolution(parameters);
    }

    private void showSolution(Map<String, Parameter> parameters) {
//        issueSolvingService.start();
//        solution = job.solve(issue, parameters);
        changeController.setSolution(solution);
    }

    /**
     * Executed when the Apply button is clicked in the interface.
     */
    @FXML
    protected void applySolution() {
        job.applySolution(issue, solution);
    }

    /**
     * Ignores a solution. Removes it from the issues list.
     */
    @FXML
    protected void ignoreSolution() {
        job.ignoreSolution(issue);
    }

    @Override
    public Pane getView() {
        try {
            return (Pane) buildView("/views/issue_resolve.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

    /**
     * Gets the current issue.
     *
     * @return The current issue.
     */
    public Issue getIssue() {
        return issue;
    }

    /**
     * Sets the current issue.
     *
     * @param issue The issue to set.
     */
    public void setIssue(Issue issue) {
        if (this.issue != issue) {
            this.issue = issue;
            updateView();
        }
    }

}
