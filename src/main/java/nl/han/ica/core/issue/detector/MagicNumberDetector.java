/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.NumberLiteral;

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
    public boolean visit(NumberLiteral node) {
        if (node.getParent().getNodeType() != ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
            getIssues().add(createIssue(node));
        }
        return super.visit(node);
    }

}
