/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.NumberLiteral;

/**
 *
 * @author Corne
 */
public class MagicNumberVisitor extends ASTVisitor {
    
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
