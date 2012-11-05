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

    private Strategy strategy = new ReplaceMagicNumber();
    private SourceHolder sourceHolder;

    public MagicNumberCriteria(SourceHolder sourceHolder) {
        this.sourceHolder = sourceHolder;
    }
    
    
    
    @Override
    public boolean visit(NumberLiteral node) {
        //ASTNode
        if(node.getParent().getNodeType() != ASTNode.VARIABLE_DECLARATION_FRAGMENT){
            List<ASTNode> templist = new ArrayList<>();
            templist.add(node);
            issues.add(new Issue(strategy, templist, sourceHolder));
            
        }
        return super.visit(node);
    }
}
