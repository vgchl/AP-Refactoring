package nl.han.ica.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sourceforge.pmd.*;
import nl.han.ica.core.strategies.Strategy;
import nl.han.ica.core.strategies.solvers.Parameters;
import nl.han.ica.core.strategies.solvers.StrategySolver;
import nl.han.ica.core.strategies.solvers.StrategySolverFactory;
import nl.han.ica.core.util.FileUtil;
import org.apache.log4j.Logger;

/**
 * Lists the files and the rules to check them for.
 */
public class Job {

    private List<File> files;
    private List<Strategy> strategies;
    private ObservableList<Issue> issues;
    private Logger logger;
    private PMD pmd;

    /**
     * Instantiate a new job.
     */
    public Job() {
        logger = Logger.getLogger(getClass().getName());

        strategies = new ArrayList<>();
        files = new ArrayList<>();
        issues = FXCollections.observableArrayList();

        pmd = new PMD();
        pmd.setJavaVersion(SourceType.JAVA_17);
    }

    /**
     * Process the files in this job and check them against the selected rules. Results are stored in the job report.
     */
    public void process() {
        logger.info("Processing job…");
        issues.clear();
        for (Strategy strategy : strategies) {
            RuleContext ruleContext = new RuleContext();
            for (File file : files) {
                logger.info("Processing file: " + file.getAbsolutePath());
                ruleContext.setSourceCodeFilename(file.getName());
                ruleContext.setSourceCodeFile(file);
                try {
                    pmd.processFile(new FileInputStream(file), strategy.getRuleSet(), ruleContext);
                } catch (PMDException e) {
                    ruleContext.getReport().addError(new Report.ProcessingError(e.getMessage(), file.getName()));
                    logger.info("Error while processing file: " + file.getPath(), e.getCause());
                } catch (FileNotFoundException e) {
                    ruleContext.getReport().addError(new Report.ProcessingError(e.getMessage(), file.getName()));
                    logger.info("File not found: " + file.getPath(), e.getCause());
                }
                Iterator<IRuleViolation> iterator = ruleContext.getReport().getViolationTree().iterator();
                while (iterator.hasNext()) {
                    Issue issue = new Issue(strategy);
                    issue.setRuleViolation(iterator.next());
                    issue.setFile(file);
                    issues.add(issue);
                }
                ruleContext.setReport(new Report());
            }
        }
        logger.info("Job done processing.");
    }

    /**
     * Create a solution for an issue.
     * @param issue The issue to solve.
     * @return The created solution.
     */
    public Solution solve(Issue issue) {
        return solve(issue, null);
    }

    /**
     * Create a solution for an issue, using given parameters.
     * @param issue The issue to solve.
     * @param parameters The parameters to use when creating the solution.
     * @return The created solution.
     */
    public Solution solve(Issue issue, Parameters parameters) {
        StrategySolver strategySolver = StrategySolverFactory.createStrategySolver(issue.getRuleViolation());
        strategySolver.setRuleViolation(issue.getRuleViolation());
        parameters = null != parameters ? parameters : strategySolver.getDefaultParameters();
        strategySolver.setParameters(parameters);

        logger.info("Solving issue…");
        logger.info("…with solver: " + strategySolver.getClass().getName());
        logger.info("…with parameters: " + parameters.toString());

        strategySolver.buildAST(issue.getFile());
        strategySolver.rewriteAST();

        Solution solution = new Solution(strategySolver, parameters); // TODO: Move to StrategySolver
        try {
            String contents = FileUtil.getFileContents(issue.getFile());
            solution.setBefore(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        solution.setAfter(strategySolver.getCompilationUnit().toString());
        logger.info("Done solving issue.");
        return solution;
    }

    /**
     * Checks whether the conditions are right so the job can be processed.
     * @return Whether files and rules are present.
     */
    public boolean canProcess() {
        return files.size() > 0 && strategies.size() > 0;
    }

    /**
     * Save a solution to file.
     * @param issue The issue to solve.
     * @param solution The solution to apply.
     */
    public void applySolution(Issue issue, Solution solution) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(issue.getFile());
            byte[] content = solution.getAfter().getBytes();
            fileOutputStream.write(content);
            process();
        } catch (IOException e) {
            logger.fatal("Could not apply solution: error during file write.");
        }
    }

    /**
     * Returns the job's files.
     * @return The job's files.
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Sets the job's files.
     * @param files The job's files.
     */
    public void setFiles(List<File> files) {
        this.files = files;
    }

    /**
     * Returns the job's strategies.
     * @return The job's strategies.
     */
    public List<Strategy> getStrategies() {
        return strategies;
    }

    /**
     * Sets the job's strategies.
     * @param strategies The job's strategies.
     */
    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * Get the detected issues.
     * @return The job's issues.
     */
    public ObservableList<Issue> getIssues() {
        return issues;
    }

}
