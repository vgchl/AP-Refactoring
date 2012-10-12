package nl.han.ica.core.strategies;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.rules.XPathRule;

public class StrategyFactory {

    public static Strategy createStrategy(RuleViolation violation){
        if(violation.getRule() instanceof XPathRule){
            return new ReplaceMagicNumber();
        }

        return null;
    }
}
