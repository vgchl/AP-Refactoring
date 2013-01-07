package nl.han.ica.core;

import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.solution.Delta;
import nl.han.ica.core.solution.Solution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;

import static org.junit.Assert.*;


public class SolutionTest {

    private Solution solution = null;

    @Mock
    private IssueSolver issueSolver;
    private SourceFile sourceFile;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        solution = new Solution(null, issueSolver, null);
    }

    @Test
    public void testGetIssueSolver() throws Exception {
        assertSame(issueSolver, solution.getIssueSolver());
    }

    @Test
    public void testGetIssue() {
        assertNull(solution.getIssue());
    }

    @Test
    public void testGetDeltas() {
        assertNotNull(solution.getDeltas());
    }

    @Test
    public void testSetDeltas() {
        ArrayList<Delta> deltas = new ArrayList<>();
        assertNotSame(solution.getDeltas(), deltas);
        solution.setDeltas(deltas);
        assertSame(deltas, solution.getDeltas());
    }

    @Test
    public void testGetParameters() {
        assertNull(solution.getParameters());
    }

    @Test
    public void testCreateDelta() {
        assertEquals(solution.createDelta(sourceFile), solution.getDeltas().get(0) );
    }




}
