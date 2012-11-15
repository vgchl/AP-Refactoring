package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class PullUpFieldSolver extends IssueSolver {

    private static final String FIELD_NAME = "Field name";

    private List<SourceFile> subclasses;

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof PullUpFieldDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {

        // get fields to remove from Issue
        // get containing class from fields
        // get superclass from containing classes

        // add a field to the superclass
        // remove fields from subclasses

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
