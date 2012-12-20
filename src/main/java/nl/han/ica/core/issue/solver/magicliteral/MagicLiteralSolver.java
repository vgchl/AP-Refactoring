package nl.han.ica.core.issue.solver.magicliteral;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.MagicLiteralDetector;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class MagicLiteralSolver extends IssueSolver {

	private static final String PARAMETER_CONSTANT_NAME = "Constant name";

	private final Logger log = Logger.getLogger(getClass().getName());

	@Override
	public boolean canSolve(Issue issue) {
		return issue.getDetector() instanceof MagicLiteralDetector;
	}

	@Override
	protected Solution internalSolve(Issue issue,
			Map<String, Parameter> parameters) {

		LiteralSolutionBuilder solutionBuilder = LiteralSolutionBuilderFactory
				.getSolutionBuilder(issue, this, parameters,
						PARAMETER_CONSTANT_NAME);

		Solution solution = null;

		if (solutionBuilder != null) {
			solution = solutionBuilder.build();
		} else {

			log.error("No suitable SolutionBuilder found.");
		}

		return solution;
	}

	@Override
	protected Map<String, Parameter> defaultParameters() {
		Map<String, Parameter> parameters = new HashMap<>();
		Parameter constantNameParameter = new Parameter(
				PARAMETER_CONSTANT_NAME, "CONSTANT_NAME");
		constantNameParameter.getConstraints().add(new Parameter.Constraint() {
			@Override
			public boolean isValid(Object value) {
				return ((String) value)
						.matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
			}
		});
		parameters.put(PARAMETER_CONSTANT_NAME, constantNameParameter);
		return parameters;
	}

}
