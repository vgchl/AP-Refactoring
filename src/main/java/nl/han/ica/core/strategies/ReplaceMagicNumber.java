/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies;

/**
 *
 * @author Corne
 */
public class ReplaceMagicNumber implements Strategy {

    private static final String RULE_NAME_AVOID_USING_LITERAL = "AvoidLiteralsInIfCondition";
    private static final String STRATEGY_NAME = "Replace Magic Number with Symbolic Constant";
    public static final String STRATEGY_DESCRIPTION = "Avoid using Literals in Conditional Statements";

    public ReplaceMagicNumber() {        
    }
    
    @Override
    public String getName() {
        return STRATEGY_NAME;
    }
    
}
