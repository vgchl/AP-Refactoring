/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.Set;

public class MagicLiteralDetector extends IssueDetector {

    @Override
    public String getTitle() {
        return "Magic Literal";
    }

    @Override
    public String getDescription() {
        return "Avoid using literals in conditional statements.";
    }

    @Override
    public void detectIssues() {
        MagicLiteralVisitor magicLiteralVisitor = new MagicLiteralVisitor();
        context.accept(magicLiteralVisitor);
        createIssues(magicLiteralVisitor.getMagicLiterals());
    }

    private class MagicLiteralVisitor extends ASTVisitor {
        //All violated nodes
        private Set<ASTNode> magicLiterals;

        //TODO: Implement number literal visitor and make sure it is added to the violated nodes.
        // Hint: Use the NumberLiteral class

        //TODO: Define if numberliteral is a magicnumber
        //Hint: Check the ASTNODE class for nodetypes


        public Set<ASTNode> getMagicLiterals() {
            return magicLiterals;
        }
    }
}
