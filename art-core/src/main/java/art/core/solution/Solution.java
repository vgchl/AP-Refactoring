package art.core.solution;

import art.core.issue.Issue;
import art.core.issue.IssueSolver;
import art.core.SourceFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the set of changes that form the solution to an {@link Issue}.
 *
 * @author Teun van Vegchel
 */
public class Solution {

    private Issue issue;
    private IssueSolver issueSolver;
    private Map<String, Parameter> parameters;
    private List<Delta> deltas;

    /**
     * Creates a solution with a strategy solver and additional parameters.
     *
     * @param issueSolver The IssueSolver that created this solution.
     * @param parameters  The parameters that the solver needs to apply the refactoring.
     */
    public Solution(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters) {
        this.issue = issue;
        this.issueSolver = issueSolver;
        this.parameters = parameters;
        deltas = new ArrayList<>();
    }

    /**
     * Returns the {@link IssueSolver} that created this solution.
     *
     * @return The creating {@link IssueSolver} of this solution.
     */
    public IssueSolver getIssueSolver() {
        return issueSolver;
    }

    /**
     * Get the issue this solution solves.
     *
     * @return The issue this solution solves.
     */
    public Issue getIssue() {
        return issue;
    }

    /**
     * Get the delta's that make up the solution.
     *
     * @return The solution's deltas.
     */
    public List<Delta> getDeltas() {
        return deltas;
    }

    /**
     * Sets the delta's that make up the solution.
     *
     * @param deltas The solution's deltas.
     */
    public void setDeltas(List<Delta> deltas) {
        this.deltas = deltas;
    }

    /**
     * Returns the parameters that were used in the creation of this solution.
     *
     * @return the parameters that were used in the creation of this solution.
     */
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    /**
     * Convenience method that instantiates a new {@link Delta} and associates it with this solution.
     *
     * @param sourceFile The {@link art.core.SourceFile} the {@link Delta} applies to.
     * @return The newly created {@link Delta}.
     */
    public Delta createDelta(SourceFile sourceFile) {
        Delta delta = new Delta(sourceFile);
        deltas.add(delta);
        return delta;
    }

}
