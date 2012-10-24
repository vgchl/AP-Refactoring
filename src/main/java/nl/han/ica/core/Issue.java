package nl.han.ica.core;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import net.sourceforge.pmd.IRuleViolation;
import net.sourceforge.pmd.RuleViolation;
import nl.han.ica.core.strategies.Strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Issue {

    private Strategy strategy;
    private CompilationUnit compilationUnit;
    private Node node;
    private Solution solution;
    private File file;
    private IRuleViolation ruleViolation;

    /**
     * Creates a new issue with given strategy.
     *
     * @param strategy The strategy to solve the issue with.
     */
    public Issue(Strategy strategy) {
        this(strategy, null, null);
    }

    /**
     * Creates a new issue given a strategy, compilationUnit and node.
     *
     * @param strategy The strategy to solve the issue with.
     * @param compilationUnit The compilation unit.
     * @param node The node to start solving.
     */
    public Issue(Strategy strategy, CompilationUnit compilationUnit, Node node) {
        this.strategy = strategy;
        this.compilationUnit = compilationUnit;
        this.node = node;
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

    /**
     * Gets the node for this issue.
     *
     * @return The node for this issue.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets the node for this issue.
     *
     * @param node The node to set.
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Gets the solution for this issue.
     *
     * @return The solution for this issue.
     */
    public Solution getSolution() {
        return solution;
    }

    /**
     * Sets the solution.
     *
     * @param solution The solution for this issue.
     */
    public void setSolution(Solution solution) {
        this.solution = solution;
    }


    /// LEGACY INTEGRATION ///


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
