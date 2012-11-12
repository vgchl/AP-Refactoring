/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector.visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Corne
 */
public class MagicNumberVisitorTest {
    
    public MagicNumberVisitorTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of visit method, of class MagicNumberVisitor.
     */
    @Test
    public void testVisit() {
        System.out.println("visit");
        AST ast = AST.newAST(AST.JLS3);
        CompilationUnit compilationUnit = ast.newCompilationUnit();
        NumberLiteral node = ast.newNumberLiteral();

        MagicNumberVisitor instance = new MagicNumberVisitor();
        boolean expResult = true;
        boolean result = instance.visit(compilationUnit);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMagicNumbers method, of class MagicNumberVisitor.
     */
    @Test
    public void testGetMagicNumbers() {
        System.out.println("getMagicNumbers");
        MagicNumberVisitor instance = new MagicNumberVisitor();
        List expResult = new ArrayList();
        List result = instance.getMagicNumbers();
        assertEquals(expResult, result);
    }
}
