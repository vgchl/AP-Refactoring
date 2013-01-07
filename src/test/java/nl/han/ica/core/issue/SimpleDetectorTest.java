package nl.han.ica.core.issue;

import nl.han.ica.core.issue.detector.SimpleDetector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SimpleDetectorTest {
    private SimpleDetector simpleDetector;

    @Mock
    private ASTNode astNode;

    @Mock
    private CompilationUnit compilationUnit;
    @Mock
    private Issue issue;

    private Set astNodeSet;
    private Set compilationUnits;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        simpleDetector = new SimpleDetector();
        astNodeSet = new HashSet();
        compilationUnits = new HashSet();
    }

    @Test
    public void testClearedIssueListAfterReset() {
        simpleDetector.reset();
        assertTrue(simpleDetector.getIssues().isEmpty());
    }

    @Test
    public void testGetIssues() {
        assertNotNull(simpleDetector.getIssues());
    }

    @Test
    public void testThatIssueIsCreatedWithNoParameters() {
        Issue issue = simpleDetector.createIssue();
        assertTrue(simpleDetector.getIssues().contains(issue));
    }

    @Test
    public void testThatIssueIsCreatedWithParameters() {
        Issue issue = simpleDetector.createIssue(astNode);
        assertTrue(simpleDetector.getIssues().contains(issue));
    }

    @Test @SuppressWarnings("unchecked")
    public void testThatIssuesAreCreated() {
        assertTrue(simpleDetector.getIssues().isEmpty());
        astNodeSet.add(astNode);
        simpleDetector.createIssues(astNodeSet);
        assertEquals(1, simpleDetector.getIssues().size());
    }

    @Test @SuppressWarnings("unchecked")
    public void testThatCompilationUnitsAreSet() {
        assertTrue(simpleDetector.getCompilationUnits().isEmpty());
        compilationUnits.add(compilationUnit);
        simpleDetector.setCompilationUnits(compilationUnits);
        assertEquals(1, simpleDetector.getCompilationUnits().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThatIssueListIsUnmodifiableWhenRemoving() {
        Issue issue = simpleDetector.createIssue(astNode);
        simpleDetector.getIssues().remove(issue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThatIssueListIsUnmodifiableWhenAdding() {
        simpleDetector.getIssues().add(issue);
    }
    
}
