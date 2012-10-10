/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies;

import net.sourceforge.pmd.RuleSet;

/**
 *
 * @author Corne
 */
public abstract class Strategy {

    protected RuleSet ruleSet;

    
    public abstract String getName();
        
    public RuleSet getRuleSet() {
        return ruleSet;
    }

}
