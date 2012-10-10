package nl.han.ica.core.strategies.solvers;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.rules.XPathRule;

public class StrategySolverFactory {

    public static StrategySolver createStrategy(RuleViolation violation){
        if(violation.getRule() instanceof XPathRule){
            //XPathRules are PMD-tool created rules,
            //so multiple ruleviolations are instance off XPathRule
            if(violation.getDescription().contains("Avoid using Literals in Conditional Statements")){
                return new ReplaceMagicNumberSolver(violation);
            }
        }

        return null;
    }
}
