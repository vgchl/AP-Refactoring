package nl.han.ica.core.strategies.solvers;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.rules.XPathRule;
import nl.han.ica.core.strategies.ReplaceMagicNumber;

public class StrategySolverFactory {

    public static StrategySolver createStrategySolver(RuleViolation violation){
        if(violation.getRule() instanceof XPathRule){
            //XPathRules are PMD-tool created rules,
            //so multiple ruleviolations are instance off XPathRule

            if(violation.getDescription().contains(ReplaceMagicNumber.STRATEGY_DESCRIPTION)){
                return new ReplaceMagicNumberSolver(violation);
            }
        }

        return null;
    }
}
