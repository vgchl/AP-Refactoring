package nl.han.ica.app.controllers;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.dfa.report.ReportTree;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public class PMDConnector {

    private PMD pmd = new PMD();
    private ReportTree reportTree = null;

    public void runPMD(File file){

    }

    public void processResults(){

    }

    public ReportTree getReportTree() {
        return reportTree;
    }
}
