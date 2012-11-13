/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies.solvers;

import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Corne
 */
public class ReplaceMagicNumberSolverTest {
    
    /**
     * Test of rewriteAST method, of class ReplaceMagicNumberSolver.
     */
    @Test
    public void testRewriteAST() {
        System.out.println("rewriteAST");
        ReplaceMagicNumberSolver instance = new ReplaceMagicNumberSolver();
        instance.rewriteAST();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultParameters method, of class ReplaceMagicNumberSolver.
     */
    @Test
    public void testGetDefaultParameters() {
        System.out.println("getDefaultParameters");
        ReplaceMagicNumberSolver instance = new ReplaceMagicNumberSolver();
        Map expResult = null;
        Map result = instance.getDefaultParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
