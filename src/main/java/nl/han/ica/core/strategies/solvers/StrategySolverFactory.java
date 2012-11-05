package nl.han.ica.core.strategies.solvers;

import net.sourceforge.pmd.IRuleViolation;
import net.sourceforge.pmd.rules.XPathRule;
import nl.han.ica.core.strategies.ReplaceMagicNumber;
import nl.han.ica.core.strategies.Strategy;

public class StrategySolverFactory {

    private StrategySolverFactory() {}

    public static StrategySolver createStrategySolver(IRuleViolation violation){
        if(violation.getRule() instanceof XPathRule){
            //XPathRules are PMD-tool created rules,
            //so multiple ruleviolations are instance off XPathRule

            if(violation.getDescription().contains(ReplaceMagicNumber.STRATEGY_DESCRIPTION)){
                return new ReplaceMagicNumberSolver();
            }
        }

        return null;
    }
    
    public static StrategySolver createStrategySolver(Strategy strategy){
        if(strategy instanceof ReplaceMagicNumber){
            return new ReplaceMagicNumberSolver();
        }
        return null;
    }
}
