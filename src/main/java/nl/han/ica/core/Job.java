package nl.han.ica.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetectionService;
import nl.han.ica.core.issue.IssueSolverLocator;
import nl.han.ica.core.issue.IssueSolvingService;
import nl.han.ica.core.parser.Parser;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.*;

/**
 * Lists the files and the rules to check them for.
 */
public class Job {

    private Set<SourceFile> sourceFiles;
    private Parser parser;
    private IssueDetectionService issueDetectionService;
    private IssueSolvingService issueSolvingService;
    private ObservableList<Issue> issues;
    private Logger logger;

    /**
     * Instantiate a new job.
     */
    public Job() {
        logger = Logger.getLogger(getClass().getName());

        sourceFiles = new HashSet<>();
        issues = FXCollections.observableArrayList();
        parser = new Parser();

        issueDetectionService = new IssueDetectionService();
        IssueSolverLocator locator = new IssueSolverLocator();
        issueSolvingService = new IssueSolvingService(locator);
    }

    public void process() {
        logger.debug("Processing...");
        Set<CompilationUnit> compilationUnits = parser.parse(sourceFiles);
        issues.clear();
        issues.addAll(new ArrayList<>(issueDetectionService.detectIssues(compilationUnits)));
        logger.debug("Done processing.");
    }

    public Solution solve(Issue issue) {
        return issueSolvingService.solveIssue(issue);
    }

    /**
     * Create a solution for an issue.
     *
     * @param issue The issue to solveIssue.
     * @return The created solution.
     */
    public Solution solve(Issue issue, Map<String, Parameter> parameters) {
        return issueSolvingService.solveIssue(issue, parameters);
    }

    /**
     * Checks whether the conditions are right so the job can be processed.
     *
     * @return Whether files and rules are present.
     */
    public boolean canProcess() {
        return sourceFiles.size() > 0 && issueDetectionService.getDetectors().size() > 0;
    }

    /**
     * Save a solution to file.
     *
     * @param solution The solution to apply.
     */
    public void applySolution(Solution solution) {
        issueSolvingService.applySolution(solution);
        process();
    }

    /**
     * Ignores a solution.
     *
     * @param issue The issue to ignore.
     */
    public void ignoreSolution(Issue issue) {
        issues.remove(issue);
    }

    /**
     * Get the detected issues.
     *
     * @return The job's issues.
     */
    public ObservableList<Issue> getIssues() {
        return issues;
    }

    public Set<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    public IssueDetectionService getIssueDetectionService() {
        return issueDetectionService;
    }

    public IssueSolvingService getIssueSolvingService() {
        return issueSolvingService;
    }
}
