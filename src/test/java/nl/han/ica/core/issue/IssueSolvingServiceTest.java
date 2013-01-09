package nl.han.ica.core.issue;

import nl.han.ica.core.solution.Parameter;
import nl.han.ica.core.solution.Solution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class IssueSolvingServiceTest {

    private IssueSolvingService issueSolvingService;

    @Mock
    private IssueSolverLocator issueSolverLocator;
    @Mock
    private IssueSolver issueSolver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatesDefaultLocator() {
        issueSolvingService = new IssueSolvingService();
        assertThat(issueSolvingService.getIssueSolverLocator(), instanceOf(IssueSolverLocator.class));
    }

    @Test
    public void testCreatesProvidedLocator() {
        issueSolvingService = new IssueSolvingService(issueSolverLocator);
        assertThat(issueSolvingService.getIssueSolverLocator(), is(issueSolverLocator));
    }

    @Test
    public void testCreateSolutionForIssue() {
        issueSolvingService = new IssueSolvingService(issueSolverLocator);

        Issue issue = mock(Issue.class);
        Solution solution = mock(Solution.class);
        Map<String, Parameter> parameters = null;

        when(issueSolverLocator.solverForIssue(issue)).thenReturn(issueSolver);
        when(issueSolver.createSolution(issue, parameters)).thenReturn(solution);

        assertThat(issueSolvingService.createSolution(issue, parameters), is(solution));
        verify(issueSolverLocator).solverForIssue(issue);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleNoSolverAvailable() {
        issueSolvingService = new IssueSolvingService();
        Issue issue = mock(Issue.class);

        issueSolvingService.createSolution(issue);
    }

    @Test
    public void testApplySolution() {
        issueSolvingService = new IssueSolvingService(issueSolverLocator);

        Solution solution = mock(Solution.class);
        when(solution.getIssueSolver()).thenReturn(issueSolver);

        issueSolvingService.applySolution(solution);
        verify(issueSolver).applySolution(solution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleApplyUnsolvedIssue() {
        issueSolvingService = new IssueSolvingService(issueSolverLocator);
        Solution solution = mock(Solution.class);

        issueSolvingService.applySolution(solution);
    }

    @Test
    public void testAddSolver() {
        issueSolverLocator = new IssueSolverLocator();

        issueSolvingService = new IssueSolvingService(issueSolverLocator);
        issueSolvingService.addSolver(issueSolver);
        assertTrue(issueSolverLocator.getSolvers().contains(issueSolver));
    }

    @Test
    public void testIssueSolverLocatorAccessors() {
        issueSolvingService = new IssueSolvingService(null);
        assertNull(issueSolvingService.getIssueSolverLocator());

        issueSolvingService.setIssueSolverLocator(issueSolverLocator);
        assertThat(issueSolvingService.getIssueSolverLocator(), is(issueSolverLocator));
    }

}
