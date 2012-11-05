package nl.han.ica.core;

import nl.han.ica.core.strategies.Strategy;

import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Represents a problem found at a certain spot in the source code.
 */
public class Issue {

    private Strategy strategy;
    private ASTNode violatedNode;
    private SourceHolder sourceHolder;
    
    /**
     * Creates a new issue with given strategy.
     *
     * @param strategy The strategy to solve the issue with.
     */
    public Issue(Strategy strategy, ASTNode violationNode, SourceHolder sourceHolder) {
        this.strategy = strategy;
        this.violatedNode = violationNode;
        cloneSourceHolder(sourceHolder);
    }
    
    private void cloneSourceHolder(SourceHolder sourceHolder){
        SourceHolder newHolder = new SourceHolder();
        newHolder.setFile(sourceHolder.getFile());
        newHolder.setCompilationUnit(sourceHolder.getCompilationUnit());
        this.sourceHolder = newHolder;
    }

    /**
     * Gets the strategy for this issue.
     *
     * @return The strategy.
     */
    public Strategy getStrategy() {
        return strategy;
    }

    
    
    public SourceHolder getSourceHolder() {
        return sourceHolder;
    }

    public ASTNode getViolatedNodes() {
        return violatedNode;
    }

    public String getDescription() {
        return "HOMOOOOOOOOOOOOO";
    }

}
