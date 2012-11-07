/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies.solvers;

import java.util.HashMap;
import java.util.Map;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.SourceHolder;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.IDocument;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Corne
 */
public class StrategySolverTest {
    
    private StrategySolver strategySolver;
    @Mock
    private SourceHolder sourceHolder;
    @Mock
    private ASTRewrite rewrite;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        strategySolver = new StrategySolverImpl();
    }

    /**
     * Test of rewriteAST method, of class StrategySolver.
     */
    @Test
    public void testRewriteAST() {
        System.out.println("rewriteAST");
        StrategySolver instance = new StrategySolverImpl();
        instance.rewriteAST();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of applyChanges method, of class StrategySolver.
     */
    @Test
    public void testApplyChanges() {
        System.out.println("applyChanges");
        
        strategySolver.applyChanges(rewrite);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSourceHolder method, of class StrategySolver.
     */
    @Test
    public void testSetSourceHolder() {
        System.out.println("setSourceHolder");

        strategySolver.setSourceHolder(sourceHolder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setViolationNodes method, of class StrategySolver.
     */
    @Test
    public void testSetViolationNodes() {
        System.out.println("setViolationNodes");
        ASTNode violationNode = null;
        strategySolver.setViolationNodes(violationNode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class StrategySolver.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        Map expResult = new HashMap<>();
        Map result = strategySolver.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameters method, of class StrategySolver.
     */
    @Test
    public void testSetParameters() {
        System.out.println("setParameters");
        Map<String, Parameter> parameters = new HashMap<>();
        strategySolver.setParameters(parameters);
        assertEquals(parameters, strategySolver.getParameters());
    }

    /**
     * Test of getDefaultParameters method, of class StrategySolver.
     */
    @Test
    public void testGetDefaultParameters() {
        System.out.println("getDefaultParameters");
        Map expResult = new HashMap<>();
        Map result = strategySolver.getDefaultParameters();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDocument method, of class StrategySolver.
     */
    @Test
    public void testGetDocument() {
        System.out.println("getDocument");
        IDocument expResult = null;
        IDocument result = strategySolver.getDocument();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class StrategySolverImpl extends StrategySolver {

        @Override
        public void rewriteAST() {
        }
    }
}
