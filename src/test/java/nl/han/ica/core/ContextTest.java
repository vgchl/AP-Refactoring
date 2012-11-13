/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Corne
 */
public class ContextTest {
    
    private Context context;
    
    @Mock
    private List<File> files;
    
    public ContextTest() {
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = new Context(files);
    }

    /**
     * Test of createCompilationUnits method, of class Context.
     */
    @Test
    public void testCreateCompilationUnits() {
        System.out.println("createCompilationUnits");
        context.createCompilationUnits();
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSourceHolders method, of class Context.
     */
    @Test
    public void testGetSourceHolders() {
        System.out.println("getSourceHolders");

        List expResult = new ArrayList();
        context.createCompilationUnits();
        List result = context.getSourceHolders();
        assertEquals(expResult, result);
    }
}
