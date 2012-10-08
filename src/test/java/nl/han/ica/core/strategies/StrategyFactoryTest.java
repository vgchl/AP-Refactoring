package nl.han.ica.core.strategies;

import junit.framework.Assert;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.ast.SimpleJavaNode;
import net.sourceforge.pmd.rules.XPathRule;
import net.sourceforge.pmd.symboltable.SourceFileScope;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 12:19
 * To change this template use File | Settings | File Templates.
 */
public class StrategyFactoryTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCreateStrategy() throws Exception {
        XPathRule magicNumberRule = new XPathRule();

        //AvoidLiteralsInIfCondition
        RuleContext context = new RuleContext();

        File file = TestInputFile.createTempFile();

        context.setSourceCodeFilename(file.getAbsolutePath());
        SimpleJavaNode node = new SimpleJavaNode(1);

        SourceFileScope scope = new SourceFileScope(file.getAbsolutePath());
        node.setScope(scope);

        node.testingOnly__setBeginColumn(1);
        node.testingOnly__setBeginLine(1);

        RuleViolation ruleViolation = new RuleViolation(magicNumberRule, context, node);

        Strategy strategy = StrategyFactory.createStrategy(ruleViolation);

        Assert.assertNotNull(strategy);
        Assert.assertTrue(strategy instanceof ReplaceMagicNumber);
    }
}
