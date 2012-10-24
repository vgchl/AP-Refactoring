package nl.han.ica.core;

import net.sourceforge.pmd.IRuleViolation;
import nl.han.ica.core.strategies.Strategy;

/**
 * Rule violations adapter.
 */
public class RuleViolationToIssueAdapter extends Issue {

    private IRuleViolation ruleViolation;

    /**
     * Creates a rule violation to issue adapter given a strategy and rule violation.
     *
     * @param strategy The strategy to apply.
     * @param ruleViolation The rule violation.
     */
    public RuleViolationToIssueAdapter(Strategy strategy, IRuleViolation ruleViolation) {
        super(strategy);
        this.ruleViolation = ruleViolation;
    }

    /**
     * Gets the rule violation.
     *
     * @return The current rule violation.
     */
    public IRuleViolation getRuleViolation() {
        return ruleViolation;
    }

    /**
     * Sets the rule violation.
     *
     * @return Set the rule violation.
     */
    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

}
