package nl.han.ica.core;

import nl.han.ica.core.strategies.solvers.Parameters;
import nl.han.ica.core.strategies.solvers.StrategySolver;

public class Solution {

    private StrategySolver strategySolver;
    private Parameters parameters;
    private String before;
    private String after;

    public Solution(StrategySolver strategySolver) {
        this.strategySolver = strategySolver;
    }

    public Solution(StrategySolver strategySolver, Parameters parameters) {
        this(strategySolver);
        this.parameters = parameters;
    }

    public StrategySolver getStrategySolver() {
        return strategySolver;
    }

    public void setStrategySolver(StrategySolver strategySolver) {
        this.strategySolver = strategySolver;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

}
