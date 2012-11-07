package nl.han.ica.core.issue;

import java.util.HashSet;
import java.util.Set;

public class IssueSolverLocator {

    private Set<IssueSolver> solvers;

    public IssueSolverLocator() {
        solvers = new HashSet<>();
    }

    public IssueSolver solverForIssue(Issue issue) {
        for (IssueSolver solver : solvers) {
            if (solver.canSolve(issue)) {
                return solver;
            }
        }
        return null;
    }

    public Set<IssueSolver> getSolvers() {
        return solvers;
    }

}

