/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.strategies.Strategy;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Corne
 */
public class StrategySolverFactoryTest {
    
    /**
     * Test of createStrategySolver method, of class StrategySolverFactory.
     */
    @Test
    public void testCreateStrategySolver() {
        System.out.println("createStrategySolver");
        Strategy strategy = null;
        StrategySolver expResult = null;
        StrategySolver result = StrategySolverFactory.createStrategySolver(strategy);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
