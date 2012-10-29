package nl.han.ica.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.han.ica.core.Issue;
import nl.han.ica.core.Job;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;

import java.util.Map;

public class IssueSolvingService extends Service<Solution> {

    private ObjectProperty<Job> jobProperty;
    private ObjectProperty<Issue> issueProperty;
    private ObjectProperty<Map<String, Parameter>> parametersProperty;

    public IssueSolvingService(Job job, Issue issue) {
        jobProperty = new SimpleObjectProperty<>(job);
        issueProperty = new SimpleObjectProperty<>(issue);
        parametersProperty = new SimpleObjectProperty<>();
    }

    @Override
    protected Task createTask() {
        return new Task<Solution>() {
            @Override
            protected Solution call() throws Exception {
                System.out.println("START SOLVING");
                Solution solution = jobProperty.get().solve(issueProperty.get(), parametersProperty.get());
                System.out.println("STOP SOLVING");
                return solution;
            }
        };
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

    public Map<String,Parameter> getParameters() {
        return parametersProperty.get();
    }
}
