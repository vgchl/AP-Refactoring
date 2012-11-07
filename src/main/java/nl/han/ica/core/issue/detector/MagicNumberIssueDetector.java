/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import nl.han.ica.core.strategies.ReplaceMagicNumber;
import nl.han.ica.core.strategies.Strategy;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.NumberLiteral;

/**
 *
 * @author Corne
 */
public class MagicNumberIssueDetector extends IssueDetector {

    private static final Strategy strategy = new ReplaceMagicNumber();

    public MagicNumberIssueDetector() {
    }
    
    
    
    @Override
    public boolean visit(NumberLiteral node) {
        //ASTNode
        if(node.getParent().getNodeType() != ASTNode.VARIABLE_DECLARATION_FRAGMENT){
            violatedNodes.add(node);
            
        }
        return super.visit(node);
    }

    @Override
    public Strategy getStrategy() {
        return strategy;
    }
    
    
}
