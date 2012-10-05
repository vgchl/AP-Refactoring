package nl.han.ica.core;

import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Job {

    private List<File> files;
    private RuleSet ruleSet;
    private List<RuleViolation> ruleViolations;

    public Job() {
        setRuleViolations(new ArrayList<RuleViolation>());
    }

    public void processFiles() {
        PMD pmd = new PMD();

        RuleContext ruleContext = new RuleContext();

//        ruleContext.setSourceCodeFilename(file.getAbsolutePath());
//        pmd.processFile(file, ruleSet, ruleContext);
//        pmd.processFiles();

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

    public List<RuleViolation> getRuleViolations() {
        return ruleViolations;
    }

    public void setRuleViolations(ArrayList<RuleViolation> ruleViolations) {
        this.ruleViolations = ruleViolations;
    }

}
