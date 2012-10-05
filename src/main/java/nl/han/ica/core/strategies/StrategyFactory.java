package nl.han.ica.core.strategies;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.rules.XPathRule;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class StrategyFactory {

    public static Strategy createStrategy(RuleViolation violation){
        if(violation.getRule() instanceof XPathRule){
            return new ReplaceMagicNumber(violation);
        }

        return null;
    }
}
