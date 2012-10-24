package nl.han.ica.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nl.han.ica.core.Issue;
import nl.han.ica.core.Job;
import nl.han.ica.core.Solution;
import nl.han.ica.core.strategies.solvers.Parameters;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IssueResolveController extends BaseController {

    private Job job;
    private Issue issue;
    private IssueResolveChangeController changeController;
    private Solution solution;

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

        showSolution(null);
    }

    private void showSolution(Parameters parameters) {
        solution = job.solve(issue, parameters);
        changeController.setSolution(solution);
        // TODO: Add listener to resolve issue with updated params
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
