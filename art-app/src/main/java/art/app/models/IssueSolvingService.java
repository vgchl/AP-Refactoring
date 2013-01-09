package art.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import art.core.Job;
import art.core.issue.Issue;
import art.core.solution.Parameter;
import art.core.solution.Solution;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Service that enables solving issues on background worker threads.
 *
 * @author Teun van Vegchel
 */
public class IssueSolvingService extends Service<Solution> {

    private ObjectProperty<Job> jobProperty;
    private ObjectProperty<Issue> issueProperty;
    private ObjectProperty<Map<String, Parameter>> parametersProperty;
    private final Logger logger;

    /**
     * Instantiate a new IssueSolvingService;
     *
     * @param job The job to which the issues being solved belong.
     */
    public IssueSolvingService(Job job) {
        logger = Logger.getLogger(getClass().getName());

        jobProperty = new SimpleObjectProperty<>(job);
        issueProperty = new SimpleObjectProperty<>();
        parametersProperty = new SimpleObjectProperty<Map<String, Parameter>>(new HashMap<String, Parameter>());
    }

    @Override
    protected Task createTask() {
        return new Task<Solution>() {
            @Override
            protected Solution call() throws Exception {
                Solution solution = jobProperty.get().createSolution(issueProperty.get(), parametersProperty.get());
                parametersProperty.set(solution.getParameters());
                return solution;
            }
        };
    }

    @Override
    protected void failed() {
        logger.fatal("Task execution failed.", getException());
    }

    /**
     * Sets the issue that will be solved.
     *
     * @param issue The issue to solve
     */
    public void setIssue(Issue issue) {
        issueProperty.set(issue);
    }

    /**
     * Returns the issue that will be (or has been) solved.
     *
     * @return The issue to solve
     */
    public Issue getIssue() {
        return issueProperty.get();
    }

    /**
     * Returns the parameters that will be used in solving the issue.
     *
     * @return Issue solving parameters
     */
    public Map<String, Parameter> getParameters() {
        return parametersProperty.get();
    }

    /**
     * Set the parameters that will be used in solving the issue.
     *
     * @param parameters Issue solving parameters
     */
    public void setParameters(Map<String, Parameter> parameters) {
        parametersProperty.set(parameters);
    }

    /**
     * Get the issue property.
     *
     * @return Issue wrapping property
     */
    public ObjectProperty<Issue> getIssueProperty() {
        return issueProperty;
    }

    /**
     * Get the job property.
     *
     * @return Job wrapping property
     */
    public ObjectProperty<Job> getJobProperty() {
        return jobProperty;
    }

}
