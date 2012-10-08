package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
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

public class StrategyTest {

    private Strategy strategy;
    private File file;

    @Before
    public void setUp() throws Exception {

        Rule rule = new UnusedLocalVariableRule();
        RuleContext context = new RuleContext();

        file = TestInputFile.createTempFile();

        context.setSourceCodeFilename(file.getAbsolutePath());
        SimpleJavaNode node = new SimpleJavaNode(1);

        SourceFileScope scope = new SourceFileScope(file.getAbsolutePath());
        node.setScope(scope);

        node.testingOnly__setBeginColumn(1);
        node.testingOnly__setBeginLine(1);

        RuleViolation testViolation = new RuleViolation(rule, context, node);
        strategy = new StrategyMock(testViolation);
    }

    @Test
    public void testBuildAST() throws Exception {
        strategy.buildAST(file);

        CompilationUnit unit = JavaParser.parse(file);

        Assert.assertNotNull(strategy.getCompilationUnit());
        Assert.assertEquals(strategy.getCompilationUnit(), unit);

    }
}
