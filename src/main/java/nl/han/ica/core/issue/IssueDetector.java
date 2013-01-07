package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Provides basic functionality for detecting issues in {@link CompilationUnit}s.
 */
public abstract class IssueDetector {

    /**
     * The set of compilation units to scan for issues.
     */
    protected Set<CompilationUnit> compilationUnits;
    /**
     * The set of detected issues.
     */
    protected Set<Issue> issues;

    /**
     * Instantiate a new issue detector.
     */
    public IssueDetector() {
        compilationUnits = new HashSet<>();
        issues = new HashSet<>();
    }

    /**
     * Scans the set of compilation units for issues.
     */
    public abstract void detectIssues();

    /**
     * Returns the detector's title.
     *
     * @return The detector's title.
     */
    public abstract String getTitle();

    /**
     * Returns the detector's description.
     *
     * @return The detector's description.
     */
    public abstract String getDescription();

    /**
     * Reset the detector. Clears the set of detected issues.
     */
    public void reset() {
        issues.clear();
    }

    /**
     * Returns the set of detected issues.
     *
     * @return The set of detected issues.
     */
    public Set<Issue> getIssues() {
        return Collections.unmodifiableSet(issues);
    }

    /**
     * Convenience method for creating a new issue. Automatically adds the issue to the set of detected issues, and
     * associates the issue with this detector.
     *
     * @return The created issue.
     */
    protected Issue createIssue() {
        Issue issue = new Issue(this);
        issues.add(issue);
        return issue;
    }

    /**
     * Convenience method for creating a new issue, specifying the node that's the root to the issue. Automatically adds
     * the issue to the set of detected issues, and associates the issue with this detector.
     *
     * @param node The source node for the issue.
     * @return The created issue.
     */
    protected Issue createIssue(ASTNode node) {
        Issue issue = createIssue();
        issue.getNodes().add(node);
        return issue;
    }

    /**
     * Convenience method for creating new issues, specifying the nodes that are the root to their issues. Automatically
     * adds the issue to the set of detected issues, and associates the issue with this detector.
     *
     * @param nodes The source nodes to create issues for.
     */
    protected void createIssues(Set<ASTNode> nodes) {
        for (ASTNode node : nodes) {
            issues.add(createIssue(node));
        }
    }

    /**
     * Set the compilation units this detector will scan for issues.
     *
     * @param compilationUnits The compilation units this detector will scan through.
     */
    public void setCompilationUnits(Set<CompilationUnit> compilationUnits) {
        this.compilationUnits = compilationUnits;
    }

    /**
     * Get the compilation units
     *
     * @return compilationUnits The set of compilation units
     */

    public Set<CompilationUnit> getCompilationUnits() {
        return this.compilationUnits;
    }
}
