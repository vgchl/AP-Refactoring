/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


package nl.han.ica.core.issue;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class IssueSolver {

    private Logger logger;

    public IssueSolver() {
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

        for (Delta delta : solution.getDeltas()) {
            try {
                FileUtil.setFileContent(delta.getFile(), delta.getAfter());
            } catch (IOException ex) {
                logger.fatal("Could not apply solution: error during file write. \n" + ex);
            }
        }
    }

    protected Map<String, Parameter> defaultParameters() {
        return new HashMap<>();
    }

    private void mergeDefaultParameters(final Map<String, Parameter> parameters) {
        for (Map.Entry<String, Parameter> entry : defaultParameters().entrySet()) {
            if (!parameters.containsKey(entry.getKey())) {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }
    }

}
