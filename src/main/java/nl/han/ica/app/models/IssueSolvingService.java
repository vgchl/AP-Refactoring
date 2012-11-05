package nl.han.ica.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.han.ica.core.Issue;
import nl.han.ica.core.Job;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class IssueSolvingService extends Service<Solution> {

    private ObjectProperty<Job> jobProperty;
    private ObjectProperty<Issue> issueProperty;
    private ObjectProperty<Map<String, Parameter>> parametersProperty;
    private final Logger logger;

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
                Solution solution = jobProperty.get().solve(issueProperty.get());
                parametersProperty.set(solution.getParameters());
                return solution;
            }
        };
    }

    @Override
    protected void failed() {
        logger.fatal("Task execution failed.", getException());
    }

    public void setIssue(Issue issue) {
        issueProperty.set(issue);
    }

    public Issue getIssue() {
        return issueProperty.get();
    }

    public void setParameters(Map<String, Parameter> parameters) {
        parametersProperty.set(parameters);
    }

    public ObjectProperty<Issue> getIssueProperty() {
        return issueProperty;
    }

    public ObjectProperty<Job> getJobProperty() {
        return jobProperty;
    }

    public Map<String, Parameter> getParameters() {
        return parametersProperty.get();
    }

}
