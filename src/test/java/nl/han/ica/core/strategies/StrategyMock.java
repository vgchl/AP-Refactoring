package nl.han.ica.core.strategies;


import net.sourceforge.pmd.RuleViolation;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 3-10-12
 * Time: 9:44
 * To change this template use File | Settings | File Templates.
 */
public class StrategyMock extends Strategy {

    public StrategyMock(RuleViolation ruleViolation) {
        super("Mocked Name", ruleViolation);
    }

    @Override
    public void rewriteAST() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
