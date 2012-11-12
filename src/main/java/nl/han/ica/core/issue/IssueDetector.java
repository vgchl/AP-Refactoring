package nl.han.ica.core.issue;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.CompilationUnit;


public abstract class IssueDetector {

    protected Set<Issue> issues;
    protected Set<CompilationUnit> compilationUnits;

    public IssueDetector() {
        issues = new HashSet<>();
    }

    public void reset() {
        issues.clear();
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    private Issue createIssue() {
        Issue issue = new Issue(this);
        issues.add(issue);
        return issue;
    }

    protected Issue createIssue(ASTNode node) {
        Issue issue = createIssue();
        issue.getNodes().add(node);
        return issue;
    }
    
    protected void createIssues(List<ASTNode> nodes){
        for(ASTNode node : nodes){
            issues.add(createIssue(node));
        }
    }

    public void setCompilationUnits(Set<CompilationUnit> compilationUnits) {
        if (compilationUnits == null || compilationUnits.isEmpty())
        {
                         throw new IllegalArgumentException("List must contain at least one compilationunit.");
        }
        this.compilationUnits = compilationUnits;
    }
    
    public abstract Set<Issue> detectIssues();

    public abstract String getTitle();

    public abstract String getDescription();

}
