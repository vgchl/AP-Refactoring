/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;

import java.util.HashSet;
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
    public void detectIssues() {
        for (CompilationUnit compilationUnit : compilationUnits) {
            MagicNumberVisitor magicNumberVisitor = new MagicNumberVisitor();
            compilationUnit.accept(magicNumberVisitor);
            createIssues(magicNumberVisitor.getMagicNumbers());
        }
    }


    private class MagicNumberVisitor extends ASTVisitor {
        private Set<ASTNode> magicNumbers;

        public MagicNumberVisitor() {
            magicNumbers = new HashSet<>();
        }

        @Override
        public boolean visit(NumberLiteral node) {
            if (node.getParent().getNodeType() != ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
                magicNumbers.add(node);
            }
            return super.visit(node);
        }

        public Set<ASTNode> getMagicNumbers() {
            return magicNumbers;
        }
    }
}
