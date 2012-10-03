package nl.han.ica.core.strategies;

import net.sourceforge.pmd.RuleViolation;
import org.eclipse.jdt.core.dom.AST;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class Strategy  {

    public abstract void rewriteAST(AST ast);

    public void buildAST(RuleViolation ruleViolation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
