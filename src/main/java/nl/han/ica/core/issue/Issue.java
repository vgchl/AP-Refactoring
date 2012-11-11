package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a problem found at a certain spot in the source code.
 */
public class Issue {

    private IssueDetector detector;
    private List<ASTNode> nodes;

    public Issue(IssueDetector detector) {
        this.detector = detector;
        nodes = new ArrayList<>();
    }

    public IssueDetector getDetector() {
        return detector;
    }

    public List<ASTNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ASTNode> nodes) {
        this.nodes = nodes;
    }

}
