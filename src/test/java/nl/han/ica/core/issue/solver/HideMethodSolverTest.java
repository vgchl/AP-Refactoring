package nl.han.ica.core.issue.solver;

import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.detector.HideMethodDetector;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;

import static junit.framework.Assert.*;

/**
 * @author: Wouter Konecny
 * @created: 09-11-12
 */
public class HideMethodSolverTest {
    HideMethodSolver hideMethodSolver = null;
    private MethodDeclaration methodDeclaration = null;
    private AST ast;

    @Before
    public void setUp() throws Exception {
        hideMethodSolver = new HideMethodSolver();
        ast = AST.newAST(AST.JLS3);
        methodDeclaration = ast.newMethodDeclaration();
        methodDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL));
    }

    @Test
    public void testRewriteAST() throws Exception {
        Issue issue = new Issue(new HideMethodDetector());
        assertFalse(Modifier.isPrivate(methodDeclaration.getModifiers()));
        assertTrue(Modifier.isPublic(methodDeclaration.getModifiers()));
        hideMethodSolver.internalSolve(issue, null);
        assertFalse(Modifier.isPublic(methodDeclaration.getModifiers()));
        assertTrue(Modifier.isPrivate(methodDeclaration.getModifiers()));
    }

    @Test
    public void testGetDefaultParameters() throws Exception {

    }
}
