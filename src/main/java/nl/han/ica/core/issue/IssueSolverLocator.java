package nl.han.ica.core.issue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This locator finds a suitable {@link IssueSolver} for an {@link Issue}.
 */
public class IssueSolverLocator {

    private Set<IssueSolver> solvers;

    /**
     * Instantiate a new IssueSolverLocator.
     */
    public IssueSolverLocator() {
        solvers = new HashSet<>();
    }

    /**
     * Find a suitable {@link IssueSolver} for an Issue.
     *
     * @param issue The issue to find an IssueSolver for.
     * @return The IssueSolver, or null in case no suitable solver could be found.
     */
    public IssueSolver solverForIssue(Issue issue) {
        for (IssueSolver solver : solvers) {
            if (solver.canSolve(issue)) {
                return solver;
            }
        }
        return null;
    }

    /**
     * Add an {@link IssueSolver} to the set and make it available for locating.
     *
     * @param issueSolver The solver to make available.
     */
    public void addSolver(IssueSolver issueSolver) {
        solvers.add(issueSolver);
    }

    /**
     * Returns all solvers registered with this locator.
     *
     * @return The solvers registered with this locator.
     */
    public Set<IssueSolver> getSolvers() {
        return Collections.unmodifiableSet(solvers);
    }

}
