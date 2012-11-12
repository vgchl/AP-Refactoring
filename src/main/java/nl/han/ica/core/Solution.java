package nl.han.ica.core;

import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Solution {

    private Issue issue;
    private IssueSolver issueSolver;
    private Map<String, Parameter> parameters;
    private List<Delta> deltas;

    /**
     * Creates a solution with a strategy solver and additional parameters.
     *
     * @param issueSolver The IssueSolver that created this solution.
     * @param parameters The parameters that the solver needs to apply the refactoring.
     */
    public Solution(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters) {
        this.issue = issue;
        this.issueSolver = issueSolver;
        this.parameters = parameters;
        deltas = new ArrayList<>();
    }

    public IssueSolver getIssueSolver() {
        return issueSolver;
    }

    public Issue getIssue() {
        return issue;
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

    public Delta createDelta(SourceFile sourceFile) {
        Delta delta = new Delta(sourceFile);
        deltas.add(delta);
        return delta;
    }
}
