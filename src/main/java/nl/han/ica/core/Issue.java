package nl.han.ica.core;

import net.sourceforge.pmd.IRuleViolation;
import nl.han.ica.core.strategies.Strategy;

import java.io.File;

/**
 * Represents a problem found at a certain spot in the source code.
 */
public class Issue {

    private Strategy strategy;
    private File file;
    private IRuleViolation ruleViolation;

    /**
     * Creates a new issue with given strategy.
     *
     * @param strategy The strategy to solve the issue with.
     */
    public Issue(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Gets the strategy for this issue.
     *
     * @return The strategy.
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Sets the strategy for this issue.
     *
     * @param strategy The strategy to set.
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public IRuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }
}
