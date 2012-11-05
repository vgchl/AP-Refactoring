/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues.criteria;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.Issue;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 *
 * @author Corne
 */
public abstract class Criteria extends ASTVisitor {
    
    protected List<Issue> issues = new ArrayList<>();

    public List<Issue> getIssues() {
        return issues;
    }
}
