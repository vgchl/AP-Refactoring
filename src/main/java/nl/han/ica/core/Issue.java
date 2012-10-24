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

    public Issue(Strategy strategy) {
        this(strategy, null, null);
    }

    public Issue(Strategy strategy, CompilationUnit compilationUnit, Node node) {
        this.strategy = strategy;
        this.compilationUnit = compilationUnit;
        this.node = node;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Solution getSolution() {
        return solution;
    }

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
