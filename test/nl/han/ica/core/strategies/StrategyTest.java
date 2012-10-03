package nl.han.ica.core.strategies;

import net.sourceforge.pmd.*;
import net.sourceforge.pmd.rules.basic.AvoidUsingHardCodedIP;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class StrategyTest {

    Strategy strategy;
    RuleViolation testViolation;

    @Before
    public void setUp() throws Exception {

        RuleSetFactory factory = new RuleSetFactory();


    }

    @Test
    public void testBuildAST() throws Exception {
        strategy.buildAST(testViolation);
    }
}
