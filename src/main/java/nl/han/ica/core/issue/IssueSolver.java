package nl.han.ica.core.issue;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class IssueSolver {

    private Logger logger;
    protected Map<String, Parameter> defaultParameters;

    public IssueSolver() {
        defaultParameters = new HashMap<>();
        logger = Logger.getLogger(getClass().getName());
    }

    public abstract boolean canSolve(Issue issue);

    public Solution createSolution(Issue issue, Map<String, Parameter> parameters) {
        logger.info("Creating solution for issue " + issue);
        if (!canSolve(issue)) {
            throw new IllegalArgumentException("Cannot solve issue. This solver does not know how to solve that kind of issue.");
        }
        if (null == parameters) {
            parameters = new HashMap<>();
        }
        mergeDefaultParameters(parameters);
        return internalSolve(issue, parameters);
    }

    protected abstract Solution internalSolve(Issue issue, Map<String, Parameter> parameters);

    public void applySolution(Solution solution) {
        logger.info("Applying solution " + solution);
        if (solution.getIssueSolver() != this) {
            throw new IllegalArgumentException("Cannot apply solution. The solution was made by a different solver.");
        }
        // TODO: Apply solution
//        try {
//            FileUtil.setFileContent(file, solution.getAfter());
//        } catch (IOException e) {
//            logger.fatal("Could not apply solution: error during file write.");
//        }
    }

    private void mergeDefaultParameters(final Map<String, Parameter> parameters) {
        for (Map.Entry<String, Parameter> entry : defaultParameters.entrySet()) {
            if (!parameters.containsKey(entry.getKey())) {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }
    }

}
