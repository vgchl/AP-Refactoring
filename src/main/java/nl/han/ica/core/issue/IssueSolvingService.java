package nl.han.ica.core.issue;

import nl.han.ica.core.solution.Parameter;
import nl.han.ica.core.solution.Solution;

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
     * Instantiate a new IssueSolvingService.
     */
    public IssueSolvingService(final IssueSolverLocator issueSolverLocator) {
        this.issueSolverLocator = issueSolverLocator;
    }

    /**
     * Create a solution for an issue.
     *
     * @param issue The issue to createSolution.
     * @return The solution to the issue.
     */
    public Solution createSolution(final Issue issue) {
        return createSolution(issue, null);
    }

    /**
     * Create a solution for an issue, based on a set of parameters.
     *
     * @param issue      The issue to createSolution.
     * @param parameters The parameters to use in the solving process.
     * @return The solution to the issue.
     */
    public Solution createSolution(final Issue issue, final Map<String, Parameter> parameters) {
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
    public void applySolution(final Solution solution) {
        IssueSolver solver = solution.getIssueSolver();
        if (null == solver) {
            throw new IllegalArgumentException("Solution has not been solved by a solver.");
        }
        solver.applySolution(solution);
    }

    /**
     * Add an issue solver to the service.
     *
     * @param issueSolver The issue solver to add.
     */
    public void addSolver(final IssueSolver issueSolver) {
        issueSolverLocator.addSolver(issueSolver);
    }

    /**
     * Returns the locator used to find suitable solvers for issues.
     *
     * @return The locator used to find suitable solvers for issues.
     */
    public IssueSolverLocator getIssueSolverLocator() {
        return issueSolverLocator;
    }

    /**
     * Set the locator used to find suitable solvers for issues.
     *
     * @param issueSolverLocator the locator used to find suitable solvers for issues.
     */
    public void setIssueSolverLocator(final IssueSolverLocator issueSolverLocator) {
        this.issueSolverLocator = issueSolverLocator;
    }

}
