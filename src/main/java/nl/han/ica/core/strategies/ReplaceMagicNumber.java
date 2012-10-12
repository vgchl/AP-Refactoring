/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies;

import java.io.InputStream;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;

/**
 *
 * @author Corne
 */
public class ReplaceMagicNumber extends Strategy {

    private static final String RULE_NAME_AVOID_USING_LITERAL = "AvoidLiteralsInIfCondition";
    private static final String STRATEGY_NAME = "Replace Magic Number with Symbolic Constant";
    public static final String STRATEGY_DESCRIPTION = "Avoid using Literals in Conditional Statements";

    public ReplaceMagicNumber() {
        //ReplaceMagicNumber is XPathRule, so get the rule from the xml file
        InputStream rs = PMD.class.getClassLoader().getResourceAsStream("rulesets/controversial.xml");
        RuleSetFactory ruleSetFactory = new RuleSetFactory();
        ruleSet = new RuleSet();
        ruleSet.addRule(ruleSetFactory.createRuleSet(rs, PMD.class.getClassLoader()).getRuleByName(RULE_NAME_AVOID_USING_LITERAL));
        
    }
    
    @Override
    public String getName() {
        return STRATEGY_NAME;
    }
    
}
