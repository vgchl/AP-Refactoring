package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.Set;

/**
 * Represents a problem found at a certain spot in the source code.
 */
public class Issue {

    private IssueDetector detector;
    private Set<ASTNode> nodes;

    public Issue(IssueDetector detector) {
        this.detector = detector;
    }

    public IssueDetector getDetector() {
        return detector;
    }

    public Set<ASTNode> getNodes() {
        return nodes;
    }

    public void setNodes(Set<ASTNode> nodes) {
        this.nodes = nodes;
    }

}
