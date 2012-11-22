package nl.han.ica.core.issue;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;

import java.util.Map;

public class IssueSolvingService {

    private IssueSolverLocator issueSolverLocator;

    public IssueSolvingService(IssueSolverLocator issueSolverLocator) {
        this.issueSolverLocator = issueSolverLocator;
    }

    public Solution solveIssue(Issue issue) {
        return solveIssue(issue, null);
    }

    public Solution solveIssue(Issue issue, Map<String, Parameter> parameters) {
        IssueSolver solver = issueSolverLocator.solverForIssue(issue);
        if (null == solver) {
            throw new IllegalStateException("No suitable solver available.");
        }
        return solver.createSolution(issue, parameters);
    }

    public void applySolution(Solution solution) {
        solution.getIssueSolver().applySolution(solution);
    }

    public void addSolver(IssueSolver issueSolver) {
        issueSolverLocator.addSolver(issueSolver);
    }

}
