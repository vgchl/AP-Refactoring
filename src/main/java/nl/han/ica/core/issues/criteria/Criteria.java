/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues.criteria;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.strategies.Strategy;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 *
 * @author Corne
 */
public abstract class Criteria extends ASTVisitor {
   
    //maybe needed to have 2d list, because 1 issue can get multiple violationNodes
    protected List<ASTNode> violatedNodes = new ArrayList<>();

    public List<ASTNode> getViolatedNodes() {
        return violatedNodes;
    }
    
    public abstract Strategy getStrategy();
    
    public abstract void before();
    
    public abstract void after();
}
