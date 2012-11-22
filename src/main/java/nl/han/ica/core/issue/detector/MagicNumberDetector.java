/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    private class MagicNumberVisitor extends ASTVisitor {
        private List<ASTNode> magicNumbers = new ArrayList<>();

        @Override
        public boolean visit(NumberLiteral node) {
            if (node.getParent().getNodeType() != ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
                magicNumbers.add(node);
            }
            return super.visit(node);
        }

        public List<ASTNode> getMagicNumbers() {
            return magicNumbers;
        }

    }
}
