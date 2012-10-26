package nl.han.ica.core;

import nl.han.ica.core.strategies.solvers.Parameter;
import nl.han.ica.core.strategies.solvers.StrategySolver;

import java.util.Map;

public class Solution {

    private StrategySolver strategySolver;
    private Map<String, Parameter> parameters;
    private String before;
    private String after;

    /**
     * Creates a solution with a strategy solver.
     *
     * @param strategySolver The strategy solver to use to apply this solution.
     */
    public Solution(StrategySolver strategySolver) {
        this.strategySolver = strategySolver;
    }

    /**
     * Creates a solution with a strategy solver and additional parameters.
     *
     * @param strategySolver The strategy solver to use.
     * @param parameters The parameters that the solver needs to apply the refactoring.
     */
    public Solution(StrategySolver strategySolver, Map<String, Parameter> parameters) {
        this(strategySolver);
        this.parameters = parameters;
    }

    /**
     * Gets the strategy solver.
     *
     * @return The current strategy solver.
     */
    public StrategySolver getStrategySolver() {
        return strategySolver;
    }

    /**
     * Set the strategy solver.
     *
     * @param strategySolver The strategy solver to set.
     */
    public void setStrategySolver(StrategySolver strategySolver) {
        this.strategySolver = strategySolver;
    }

    /**
     * Get the before solution state.
     *
     * @return A string that contains the before-solution-applied state
     */
    public String getBefore() {
        return before;
    }

    /**
     * Sets the before state of a file as String.
     *
     * @param before The contents of the file before an solution is applied.
     */
    public void setBefore(String before) {
        this.before = before;
    }

    /**
     * Get the after solution state.
     *
     * @return A string that contains the after-solution-applied state
     */
    public String getAfter() {
        return after;
    }

    /**
     * Sets the after state of a file as String.
     *
     * @param after The contents of the file after an solution is applied.
     */
    public void setAfter(String after) {
        this.after = after;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }
}
