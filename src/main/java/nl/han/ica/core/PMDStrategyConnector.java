package nl.han.ica.core;

import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.core.strategies.Strategy;
import nl.han.ica.core.strategies.StrategyFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public class PMDStrategyConnector {

    private PMD pmd = new PMD();
    private ReportTree reportTree = null;
    private File file = null;

    public PMDStrategyConnector(File file){
        this.file = file;
    }

    public void runPMD( RuleSet ruleSet){
        RuleContext ruleContext = new RuleContext();
        ruleContext.setSourceCodeFilename(file.getAbsolutePath());

        try {
            InputStream inputStream = new FileInputStream(file);
            //temp code for magicnumberrule
            InputStream rs = pmd.getClassLoader().getResourceAsStream("rulesets/controversial.xml");

            RuleSetFactory ruleSetFactory = new RuleSetFactory();
            ruleSet.addRuleSet(ruleSetFactory.createRuleSet(rs, pmd.getClassLoader()));

            pmd.processFile(inputStream, ruleSet, ruleContext);
            reportTree = ruleContext.getReport().getViolationTree();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PMDException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void processResults(){
        Iterator it = reportTree.iterator();

        //TODO atm taking first element of the tree, change to user selected
        RuleViolation ruleViolation = (RuleViolation) it.next();
        Strategy strategy = StrategyFactory.createStrategy(ruleViolation);
        strategy.buildAST(file);
        strategy.rewriteAST();

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(strategy.getCompilationUnit().toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ReportTree getReportTree() {
        return reportTree;
    }
}
