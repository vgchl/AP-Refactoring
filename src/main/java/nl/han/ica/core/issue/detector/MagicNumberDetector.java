/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import java.util.Set;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.MagicNumberVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class MagicNumberDetector extends IssueDetector {
    
    @Override
    public String getTitle() {
        return "Magic Number";
    }

    @Override
    public String getDescription() {
        return "Avoid using literals in conditional statements.";
    }

    @Override
    public Set<Issue> detectIssues() {
        for (CompilationUnit compilationUnit : compilationUnits) {
            MagicNumberVisitor magicNumberVisitor = new MagicNumberVisitor();
            compilationUnit.accept(magicNumberVisitor);
            createIssues(magicNumberVisitor.getMagicNumbers());
        }
        return issues;
    }
}
