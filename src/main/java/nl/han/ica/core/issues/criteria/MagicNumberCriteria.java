/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues.criteria;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.Issue;
import nl.han.ica.core.SourceHolder;
import nl.han.ica.core.strategies.ReplaceMagicNumber;
import nl.han.ica.core.strategies.Strategy;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.NumberLiteral;

/**
 *
 * @author Corne
 */
public class MagicNumberCriteria extends Criteria {

    private static final Strategy strategy = new ReplaceMagicNumber();

    public MagicNumberCriteria() {
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

    @Override
    public void before() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void after() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
