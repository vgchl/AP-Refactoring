/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.strategies;

import java.io.InputStream;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.RuleSetFactory;

/**
 *
 * @author Corne
 */
public class ReplaceMagicNumber extends Strategy {

    private static final String name = "Replace Magic Number with Symbolic Constant";

    public ReplaceMagicNumber() {
        InputStream rs = PMD.class.getClassLoader().getResourceAsStream("rulesets/controversial.xml");
        RuleSetFactory ruleSetFactory = new RuleSetFactory();
        ruleSet = ruleSetFactory.createRuleSet(rs, PMD.class.getClassLoader());
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
