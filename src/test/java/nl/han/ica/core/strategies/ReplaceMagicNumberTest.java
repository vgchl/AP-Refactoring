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
public class ReplaceMagicNumberTest {
    
    public ReplaceMagicNumberTest() {
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
     * Test of getName method, of class ReplaceMagicNumber.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        ReplaceMagicNumber instance = new ReplaceMagicNumber();
        String expResult = "Replace Magic Number with Symbolic Constant";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
}
