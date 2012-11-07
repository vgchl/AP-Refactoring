package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.strategies.solvers.StrategySolverFactory;
import nl.han.ica.core.issue.solver.ReplaceMagicNumberSolver;
import nl.han.ica.core.strategies.solvers.StrategySolver;
import junit.framework.Assert;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.ast.SimpleJavaNode;
import net.sourceforge.pmd.rules.XPathRule;
import net.sourceforge.pmd.symboltable.SourceFileScope;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class StrategySolverFactoryTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCreateStrategy() throws Exception {
        XPathRule magicNumberRule = new XPathRule();
        
        magicNumberRule.setMessage("Avoid using Literals in Conditional Statements:16");
        //magicNumberRule.setDescription("Avoid using Literals in Conditional Statements:16");
        
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

        StrategySolver strategy = StrategySolverFactory.createStrategySolver(ruleViolation);

        Assert.assertNotNull(strategy);
        Assert.assertTrue(strategy instanceof ReplaceMagicNumberSolver);
    }
}
