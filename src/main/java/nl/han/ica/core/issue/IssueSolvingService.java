package nl.han.ica.core.issue;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;

import java.util.Map;

/**
 * Provides {@link Issue} solving functionality. Contains a set of {@link IssueSolver}s that can createSolution the issues. The
 * {@link IssueSolverLocator} helps finding a solver that can createSolution the issue.
 */
public class IssueSolvingService {

    private IssueSolverLocator issueSolverLocator;

    /**
     * Instantiate a new IssueSolvingService.
     */
    public IssueSolvingService() {
        this.issueSolverLocator = new IssueSolverLocator();
    }

    /**
     * Create a solution for an issue.
     *
     * @param issue The issue to createSolution.
     * @return The solution to the issue.
     */
    public Solution createSolution(Issue issue) {
        return createSolution(issue, null);
    }

    /**
     * Create a solution for an issue, based on a set of parameters.
     *
     * @param issue The issue to createSolution.
     * @param parameters The parameters to use in the solving process.
     * @return The solution to the issue.
     */
    public Solution createSolution(Issue issue, Map<String, Parameter> parameters) {
        IssueSolver solver = issueSolverLocator.solverForIssue(issue);
        if (null == solver) {
            throw new IllegalStateException("No suitable solver available.");
        }
        return solver.createSolution(issue, parameters);
    }

    /**
     * Apply a solution and fix an issue.
     *
     * @param solution The solution to apply.
     */
    public void applySolution(Solution solution) {
        solution.getIssueSolver().applySolution(solution);
    }

    /**
     * Add an issue solver to the service.
     *
     * @param issueSolver The issue solver to add.
     */
    public void addSolver(IssueSolver issueSolver) {
        issueSolverLocator.addSolver(issueSolver);
    }

}
