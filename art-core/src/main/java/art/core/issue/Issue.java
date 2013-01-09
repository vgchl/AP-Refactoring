package art.core.issue;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a problem found at a certain spot in the source code.
 */
public class Issue {

    private IssueDetector detector;
    private List<ASTNode> nodes;

    /**
     * Instantiate a new Issue.
     *
     * @param detector The detector that detected this issue.
     */
    public Issue(IssueDetector detector) {
        this.detector = detector;
        nodes = new ArrayList<>();
    }

    /**
     * Returns the IssueDetector that detected this issue.
     *
     * @return The IssueDetector that detected this issue.
     */
    public IssueDetector getDetector() {
        return detector;
    }

    /**
     * Returns the {@link org.eclipse.jdt.core.dom.ASTNode}s that are involved in this issue.
     *
     * @return The {@link org.eclipse.jdt.core.dom.ASTNode}s that are involved in this issue.
     */
    public List<ASTNode> getNodes() {
        return nodes;
    }

    /**
     * Sets the {@link org.eclipse.jdt.core.dom.ASTNode}s that are involved in this issue.
     *
     * @param nodes The {@link org.eclipse.jdt.core.dom.ASTNode}s that are involved in this issue.
     */
    public void setNodes(List<ASTNode> nodes) {
        this.nodes = nodes;
    }

}
