package nl.han.ica.core.strategies.solvers;

import net.sourceforge.pmd.RuleViolation;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 7-10-12
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public class ReplacePublicFieldSolver extends StrategySolver {

    public ReplacePublicFieldSolver(RuleViolation ruleViolation) {
        super(ruleViolation);
    }
    
    @Override
    public void rewriteAST() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
