package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;

import java.util.Map;

public class ReplacePublicFieldSolver extends IssueSolver {

    @Override
    public boolean canSolve(Issue issue) {
//        return issue.getDetector() instanceof PublicFieldDetector;
        return false;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        return null;
    }

}
