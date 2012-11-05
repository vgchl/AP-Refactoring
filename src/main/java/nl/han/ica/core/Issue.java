package nl.han.ica.core;

import nl.han.ica.core.strategies.Strategy;

import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Represents a problem found at a certain spot in the source code.
 */
public class Issue {

    private Strategy strategy;
    private List<ASTNode> violatedNodes;
    private SourceHolder sourceHolder;
    
    /**
     * Creates a new issue with given strategy.
     *
     * @param strategy The strategy to solve the issue with.
     */
    public Issue(Strategy strategy, List<ASTNode> violationNodes, SourceHolder sourceHolder) {
        this.strategy = strategy;
        this.violatedNodes = violationNodes;
        this.sourceHolder = sourceHolder;
    }

    /**
     * Gets the strategy for this issue.
     *
     * @return The strategy.
     */
    public Strategy getStrategy() {
        return strategy;
    }

    public void setSourceHolder(SourceHolder sourceHolder) {
        this.sourceHolder = sourceHolder;
    }
    
    public SourceHolder getSourceHolder() {
        return sourceHolder;
    }

    public List<ASTNode> getViolatedNodes() {
        return violatedNodes;
    }

    public String getDescription() {
        return "HOMOOOOOOOOOOOOO";
    }

}
