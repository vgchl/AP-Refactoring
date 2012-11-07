/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues.criteria;

import nl.han.ica.core.issue.Issue;
import org.eclipse.jdt.core.dom.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public abstract class IssueDetector extends ASTVisitor {

    private List<Issue> issues;

    public IssueDetector() {
        issues = new ArrayList<>();
    }

    public List<Issue> getIssues() {
        return issues;
    }

}
