///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package nl.han.ica.core.strategies;
//
//import net.sourceforge.pmd.RuleSet;
//import static org.junit.Assert.assertEquals;
//
//import org.junit.*;
//
///**
// *
// * @author Corne
// */
//public class StrategyTest {
//
//    private Strategy instance = null;
//
//    @Before
//    public void setUp() {
//        instance = new StrategyImpl();
//    }
//
//    @After
//    public void tearDown() {
//        instance = null;
//    }
//
//    /**
//     * Test of getName method, of class Strategy.
//     */
//    @Test
//    public void testGetName() {
//        String expResult = "";
//        String result = instance.getName();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of getRuleSet method, of class Strategy.
//     */
//    @Test
//    public void testGetRuleSet() {
//        RuleSet expResult = null;
//        RuleSet result = instance.getRuleSet();
//        assertEquals(expResult, result);
//    }
//
//    public class StrategyImpl extends Strategy {
//
//        @Override
//        public String getName() {
//            return "";
//        }
//    }
//}
