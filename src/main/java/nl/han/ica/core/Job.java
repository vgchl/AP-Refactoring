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


package nl.han.ica.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetectionService;
import nl.han.ica.core.issue.IssueSolvingService;
import nl.han.ica.core.parser.Parser;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Lists the files and the rules to check them for.
 *
 * @author Teun van Vegchel
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
        issueSolvingService = new IssueSolvingService();
    }

    /**
     * Process the job. Parses the {@link SourceFile}s and lets the {@link IssueDetectionService} find all the issues.
     */
    public void process() {
        logger.debug("Processing...");

        Set<CompilationUnit> compilationUnits = parser.parse(sourceFiles);
        issues.clear();
        issues.addAll(new ArrayList<>(issueDetectionService.detectIssues(compilationUnits)));

        logger.debug("Done processing.");
    }

    /**
     * Create a solution for an issue. Uses the {@link IssueSolvingService} to find a suitable solver.
     *
     * @param issue The issue to solve.
     * @return The solution that solves the issue.
     */
    public Solution createSolution(Issue issue) {
        return issueSolvingService.createSolution(issue);
    }

    /**
     * Create a solution for an issue. Uses the {@link IssueSolvingService} to find a suitable solver.
     *
     * @param issue      The issue to solve.
     * @param parameters The parameters to use while solving the issue.
     * @return The solution that solves the issue.
     */
    public Solution createSolution(Issue issue, Map<String, Parameter> parameters) {
        return issueSolvingService.createSolution(issue, parameters);
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

    /**
     * Get the {@link SourceFile}s the job will process.
     *
     * @return The SourceFiles to process.
     */
    public Set<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    /**
     * Returns the {@link IssueDetectionService} this job uses to detect issues in the source files.
     *
     * @return The job's IssueDetectionService instance.
     */
    public IssueDetectionService getIssueDetectionService() {
        return issueDetectionService;
    }

    /**
     * Returns the {@link IssueSolvingService} this job uses to solve the detected issues.
     *
     * @return The job's IssueSolvingService instance.
     */
    public IssueSolvingService getIssueSolvingService() {
        return issueSolvingService;
    }

}
