package nl.han.ica.core;

import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;

import java.util.Map;

public class Solution {

    private Issue issue;
    private IssueSolver issueSolver;
    private Map<String, Parameter> parameters;
    private String before;
    private String after;

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
    }

    public IssueSolver getIssueSolver() {
        return issueSolver;
    }

    public Issue getIssue() {
        return issue;
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

    /**
     * Returns the parameters that were used in the creation of this solution.
     * @return the parameters that were used in the creation of this solution.
     */
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

}
