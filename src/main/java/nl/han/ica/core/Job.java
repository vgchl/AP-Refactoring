package nl.han.ica.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sourceforge.pmd.*;
import nl.han.ica.core.strategies.Strategy;
import nl.han.ica.core.strategies.solvers.StrategySolver;
import nl.han.ica.core.strategies.solvers.StrategySolverFactory;
import nl.han.ica.core.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.han.ica.core.issues.IssueFinder;
import org.eclipse.core.internal.resources.Workspace;

/**
 * Lists the files and the rules to check them for.
 */
public class Job {

    private List<File> files;
    //private List<SourceHolder> sourceHolders;
    
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
        //sourceHolders = new ArrayList<>();
        issues = FXCollections.observableArrayList();

        pmd = new PMD();
        pmd.setJavaVersion(SourceType.JAVA_17);
    }

    /**
     * Process the files in this job and check them against the selected rules. Results are stored in the job report.
     */
    /*public void process() {
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
    }*/
    
    public void process(){
        Context context = new Context(files);
        context.createCompilationUnits();
        
        IssueFinder issueFinder = new IssueFinder(context);
        issues.addAll(issueFinder.findIssues());
    }

    /**
     * Create a solution for an issue.
     * @param issue The issue to solve.
     * @return The created solution.
     */
    public Solution solve(Issue issue) {
        
        return solve(issue, null);
    }

    public Solution solve(Issue issue, Map<String, Parameter> parameters) {
        StrategySolver strategySolver = StrategySolverFactory.createStrategySolver(issue.getStrategy());
        strategySolver.setParameters(parameters);
        
        Solution solution = new Solution(strategySolver);     
        
               
        strategySolver.setSourceHolder(issue.getSourceHolder());
        strategySolver.setViolationNodes(issue.getViolatedNodes());
        
        solution.setBefore(strategySolver.getDocument().get()); 
        strategySolver.rewriteAST();
        solution.setAfter(strategySolver.getDocument().get());
        
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
     *
     * @param issue The issue to solve.
     * @param solution The solution to apply.
     */
    public void applySolution(Issue issue, Solution solution) {
       /* try {
            FileUtil.setFileContent(issue.getFile(), solution.getAfter());
            process();
        } catch (IOException e) {
            logger.fatal("Could not apply solution: error during file write.");
        }*/
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
     * Returns the job's files.
     * @return The job's files.
     */
    public List<File> getFiles() {
        return files;
    }
    
    public void addFile(File file){
        files.add(file);
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
