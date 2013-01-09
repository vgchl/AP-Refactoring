package nl.han.ica.core.issue;

import nl.han.ica.core.Context;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Provides basic functionality for detecting issues in {@link Context}s.
 */
public abstract class IssueDetector {

    /**
     * The set of detected issues.
     */
    protected Set<Issue> issues;

    /**
     * Message logger.
     */
    protected Logger logger;

    /**
     * Instantiate a new issue detector.
     */
    public IssueDetector() {
        issues = new HashSet<>();
        logger = Logger.getLogger(getClass());
    }

    /**
     * Scans the context for issues. Resets the detector before the context is scanned, removing all previously detected
     * issues. After resetting, the call is forwarded to an abstract protected method for subclass specific tasks.
     *
     * @param context The context containing all the source code to scan through.
     */
    public Set<Issue> detectIssues(Context context) {
        reset();

        logger.debug("Searching for issues...");
        internalDetectIssues(context);
        logger.info("Found " + issues.size() + " issue(s).");

        return Collections.unmodifiableSet(issues);
    }

    /**
     * Handles the actual detection of issues in the context. Meant to be implemented by IssueDetector subclasses.
     *
     * @param context The context containing all the source code to scan through.
     */
    protected abstract void internalDetectIssues(Context context);

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

}
