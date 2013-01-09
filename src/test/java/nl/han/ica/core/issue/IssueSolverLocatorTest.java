package nl.han.ica.core.issue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class IssueSolverLocatorTest {

    @Mock
    private IssueSolver issueSolver;
    private IssueSolverLocator issueSolverLocator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        issueSolverLocator = new IssueSolverLocator();
    }

    @Test
    public void testConstruction() {
        assertNotNull(issueSolverLocator.getSolvers());
    }

    @Test
    public void testSolverForIssue() {
        Issue issue = mock(Issue.class);

        IssueSolver solverA = mock(IssueSolver.class);
        IssueSolver solverB = mock(IssueSolver.class);
        IssueSolver solverC = mock(IssueSolver.class);

        issueSolverLocator.addSolver(solverA);
        issueSolverLocator.addSolver(solverB);
        issueSolverLocator.addSolver(solverC);

        when(solverA.canSolve(issue)).thenReturn(false);
        when(solverB.canSolve(issue)).thenReturn(true);
        when(solverC.canSolve(issue)).thenReturn(false);

        IssueSolver foundSolver = issueSolverLocator.solverForIssue(issue);
        assertThat(foundSolver, is(solverB));

        verify(solverA, atMost(1)).canSolve(issue);
        verify(solverB).canSolve(issue);
        verify(solverC, atMost(1)).canSolve(issue);
    }

    @Test
    public void testAddSolver() {
        assertFalse(issueSolverLocator.getSolvers().contains(issueSolver));
        issueSolverLocator.addSolver(issueSolver);
        assertTrue(issueSolverLocator.getSolvers().contains(issueSolver));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSolversUnmodifiableAdd() {
        issueSolverLocator.getSolvers().add(issueSolver);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSolversUnmodifiableRemove() {
        issueSolverLocator.getSolvers().remove(issueSolver);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSolversUnmodifiableClear() {
        issueSolverLocator.getSolvers().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSolversUnmodifiableRemoveAll() {
        issueSolverLocator.getSolvers().removeAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSolversUnmodifiableAddAll() {
        issueSolverLocator.getSolvers().addAll(null);
    }

}
