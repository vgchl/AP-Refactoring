package nl.han.ica.core;

import net.sourceforge.pmd.IRuleViolation;
import nl.han.ica.core.strategies.Strategy;

public class RuleViolationToIssueAdapter extends Issue {

    private IRuleViolation ruleViolation;

    public RuleViolationToIssueAdapter(Strategy strategy, IRuleViolation ruleViolation) {
        super(strategy);
        this.ruleViolation = ruleViolation;
    }

    public IRuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

}
