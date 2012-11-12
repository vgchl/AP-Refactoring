package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.ASTNode;
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

    protected Issue createIssue() {
        Issue issue = new Issue(this);
        issues.add(issue);
        return issue;
    }

    protected Issue createIssue(ASTNode node) {
        Issue issue = createIssue();
        issue.getNodes().add(node);
        return issue;
    }

    public abstract String getTitle();

    public abstract String getDescription();

}
