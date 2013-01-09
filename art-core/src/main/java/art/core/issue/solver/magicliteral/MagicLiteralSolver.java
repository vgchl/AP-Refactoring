package art.core.issue.solver.magicliteral;

import art.core.issue.Issue;
import art.core.issue.IssueSolver;
import art.core.issue.detector.MagicLiteralDetector;
import art.core.solution.Parameter;
import art.core.solution.Solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class MagicLiteralSolver extends IssueSolver {

    private static final String PARAMETER_CONSTANT_NAME = "Constant name";

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof MagicLiteralDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        LiteralSolutionBuilder solutionBuilder = LiteralSolutionBuilderFactory.getSolutionBuilder(issue, this, parameters, PARAMETER_CONSTANT_NAME);
        if (solutionBuilder == null) {
            throw new UnsupportedOperationException("No suitable solution builder available.");
        }
        return solutionBuilder.build();
    }

    @Override
    protected Map<String, Parameter> defaultParameters() {
        Map<String, Parameter> parameters = new HashMap<>();
        Parameter constantNameParameter = new Parameter(PARAMETER_CONSTANT_NAME, "CONSTANT_NAME");
        constantNameParameter.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        });
        parameters.put(PARAMETER_CONSTANT_NAME, constantNameParameter);
        return parameters;
    }

}
