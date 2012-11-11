//package nl.han.ica.core.strategies.solvers;
//
//import net.sourceforge.pmd.Rule;
//import net.sourceforge.pmd.RuleContext;
//import net.sourceforge.pmd.RuleViolation;
//import net.sourceforge.pmd.ast.SimpleJavaNode;
//import net.sourceforge.pmd.rules.UnusedLocalVariableRule;
//import net.sourceforge.pmd.symboltable.SourceFileScope;
//
//import org.eclipse.jdt.core.dom.AST;
//import org.eclipse.jdt.core.dom.ASTParser;
//import org.eclipse.jdt.core.dom.CompilationUnit;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//
//public class StrategySolverTest {
//
//    private StrategySolver strategy;
//    private File file;
//
//    @Before
//    public void setUp() throws Exception {
//
//        Rule rule = new UnusedLocalVariableRule();
//        RuleContext context = new RuleContext();
//
//        file = TestInputFile.createTempFile();
//
//        context.setSourceCodeFilename(file.getAbsolutePath());
//        SimpleJavaNode node = new SimpleJavaNode(1);
//
//        SourceFileScope scope = new SourceFileScope(file.getAbsolutePath());
//        node.setScope(scope);
//
//        node.testingOnly__setBeginColumn(1);
//        node.testingOnly__setBeginLine(1);
//
//        RuleViolation testViolation = new RuleViolation(rule, context, node);
//        strategy = new StrategySolverMock(testViolation);
//    }
//
//    @Test
//    public void testBuildAST() throws Exception {
//        strategy.buildAST(file);
//
//        BufferedReader in;
//        ASTParser parser = ASTParser.newParser(AST.JLS3);
//        in = new BufferedReader(new FileReader(file));
//        final StringBuffer buffer = new StringBuffer();
//        String line;
//        while (null != (line = in.readLine())) {
//            buffer.append(line).append("\n");
//        }
//
//        parser.setSource(buffer.toString().toCharArray());
//        parser.setKind(ASTParser.K_COMPILATION_UNIT);
//        parser.setResolveBindings(true);
//
//        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
//
//        Assert.assertNotNull(strategy.getCompilationUnit());
//        Assert.assertEquals(strategy.getCompilationUnit().toString(), compilationUnit.toString());
//
//    }
//}
