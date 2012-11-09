/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies;

import org.junit.*;
import static org.junit.Assert.*;

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
        System.out.println("getName");
        Strategy instance = new StrategyImpl();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        Strategy instance = new StrategyImpl();
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    public class StrategyImpl implements Strategy {

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getDescription() {
            return "";
        }
    }
}
