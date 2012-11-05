package nl.han.ica.core;

import nl.han.ica.core.strategies.solvers.StrategySolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Solution {

    private StrategySolver strategySolver;
    private Map<String, Parameter> parameters;
    private List<Delta> deltas;

    /**
     * Creates a solution with a strategy solver.
     *
     * @param strategySolver The strategy solver to use to apply this solution.
     */
    public Solution(StrategySolver strategySolver) {
        this.strategySolver = strategySolver;
        this.deltas = new ArrayList<>();
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

    public List<Delta> getDeltas() {
        return deltas;
    }

    public void setDeltas(List<Delta> deltas) {
        this.deltas = deltas;
    }

    /**
     * Returns the parameters that were used in the creation of this solution.
     * @return the parameters that were used in the creation of this solution.
     */
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

}
