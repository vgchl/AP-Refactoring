package nl.han.ica.app.controllers;

import junit.framework.Assert;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import nl.han.ica.core.PMDStrategyConnector;
import nl.han.ica.core.strategies.TestInputFile;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class PMDStrategyConnectorTest {

    private PMDStrategyConnector pmdStrategyConnector;
    private File file;
    private RuleSet ruleSet;

    @Before
    public void setUp() throws Exception {
        file = TestInputFile.createTempFile();
        pmdStrategyConnector = new PMDStrategyConnector(file);
        PMD pmd = new PMD();//pmd needed for classpath
        InputStream rs = pmd.getClassLoader().getResourceAsStream("rulesets/controversial.xml");
        ruleSet = new RuleSet();
        RuleSetFactory ruleSetFactory = new RuleSetFactory();
        ruleSet.addRuleSet(ruleSetFactory.createRuleSet(rs, pmd.getClassLoader()));
    }

    @Test
    public void testRunPMD() throws Exception {
        pmdStrategyConnector.runPMD(ruleSet);
        Assert.assertNotNull(pmdStrategyConnector.getReportTree());
    }

    @Test
    public void testProcessResults() throws Exception {
        pmdStrategyConnector.runPMD(ruleSet);
        pmdStrategyConnector.processResults();
        pmdStrategyConnector.runPMD(ruleSet);
        Assert.assertNull(pmdStrategyConnector.getReportTree());
    }
}