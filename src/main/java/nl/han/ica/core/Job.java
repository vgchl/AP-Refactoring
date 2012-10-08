package nl.han.ica.core;

import net.sourceforge.pmd.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists the files and the rules to check them for.
 */
public class Job {
    private List<File> files;
    private PMD pmd;
    private RuleSet ruleSet;
    private RuleContext ruleContext;
    private Logger logger;

    /**
     * Instantiate a new job.
     */
    public Job() {
        files = new ArrayList<>();
        logger = Logger.getLogger(getClass().getName());

        pmd = new PMD();
        pmd.setJavaVersion(SourceType.JAVA_17);

        ruleContext = new RuleContext();
        setRuleSet(new RuleSet());
    }

    /**
     * Process the files in this job and check them against the selected rules. Results are stored in the job report.
     */
    public void process() {
        for (File file : files) {
            ruleContext.setSourceCodeFilename(file.getName());
            ruleContext.setSourceCodeFile(file);
            try {
                pmd.processFile(new FileInputStream(file), ruleSet, ruleContext);
            } catch (PMDException e) {
                ruleContext.getReport().addError(new Report.ProcessingError(e.getMessage(), file.getName()));
                logger.info("Error while processing file: " + file.getPath(), e.getCause());
            } catch (FileNotFoundException e) {
                ruleContext.getReport().addError(new Report.ProcessingError(e.getMessage(), file.getName()));
                logger.info("File not found: " + file.getPath(), e.getCause());
            }
        }
    }

    /**
     * Checks whether the conditions are right so the job can be processed.
     * @return Whether files and rules are present.
     */
    public boolean canProcess() {
        return files.size() > 0 && ruleSet.size() > 0;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public Report getReport() {
        return ruleContext.getReport();
    }

}
