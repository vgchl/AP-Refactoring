package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class PullUpFieldSolver extends IssueSolver {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof PullUpFieldDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {

        Solution solution = new Solution(issue, this, parameters);
        List<ASTNode> duplicateFields = issue.getNodes();

        getSuperClass(duplicateFields);


        // get fields to remove from Issue
        // get containing class from fields
        // get superclass from containing classes

        // add a field to the superclass
        // remove fields from subclasses

        return solution;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private ASTNode getSuperClass(List<ASTNode> duplicateFields) {

        ASTNode classWithDuplicateField = duplicateFields.get(0).getParent();

        /* Get parent to get the class. Getparent on class to get superclass.*/
        // TODO: get superclass type
        ASTNode superclass = classWithDuplicateField;

        log.debug(superclass.toString());


        return null;
    }
}
