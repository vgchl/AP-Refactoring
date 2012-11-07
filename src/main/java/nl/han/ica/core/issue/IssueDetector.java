package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.ASTVisitor;

import java.util.HashSet;
import java.util.Set;


public abstract class IssueDetector extends ASTVisitor {

    private Set<Issue> issues;

    public IssueDetector() {
        issues = new HashSet<>();
    }

    public void reset() {
        issues.clear();
    }

    public Set<Issue> getIssues() {
        return issues;
    }

}
