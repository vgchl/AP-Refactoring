package nl.han.ica.core.strategies;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.ast.SimpleJavaNode;
import net.sourceforge.pmd.rules.UnusedLocalVariableRule;
import net.sourceforge.pmd.symboltable.SourceFileScope;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class StrategyTest {

    Strategy strategy;
    RuleViolation testViolation;

    @Before
    public void setUp() throws Exception {
        strategy = new StrategyMock();

        Rule rule = new UnusedLocalVariableRule();
        RuleContext context = new RuleContext();
        Enumeration<URL> url = this.getClass().getClassLoader().getResources("nl/han/ica/core/strategies/StrategyTest.class");

        File file = new File(url.nextElement().toURI());
        System.out.println(file.getAbsolutePath());

        context.setSourceCodeFilename(file.getAbsolutePath());
        SimpleJavaNode node = new SimpleJavaNode(1);

        SourceFileScope scope = new SourceFileScope(file.getAbsolutePath());
        node.setScope(scope);

        node.testingOnly__setBeginColumn(1);
        node.testingOnly__setBeginLine(1);

        testViolation = new RuleViolation(rule, context, node);

    }

    @Test
    public void testBuildAST() throws Exception {
        strategy.buildAST(testViolation);
        Assert.assertNull(strategy.getAst());

    }
}
