/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies;

import net.sourceforge.pmd.RuleSet;
import static org.junit.Assert.assertEquals;

import org.junit.*;

/**
 *
 * @author Corne
 */
public class StrategyTest {
    
    public StrategyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class Strategy.
     */
    @Test
    public void testGetName() {
        Strategy instance = new StrategyImpl();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRuleSet method, of class Strategy.
     */
    @Test
    public void testGetRuleSet() {
        Strategy instance = new StrategyImpl();
        RuleSet expResult = null;
        RuleSet result = instance.getRuleSet();
        assertEquals(expResult, result);
    }

    public class StrategyImpl extends Strategy {

        @Override
        public String getName() {
            return "";
        }
    }
}
