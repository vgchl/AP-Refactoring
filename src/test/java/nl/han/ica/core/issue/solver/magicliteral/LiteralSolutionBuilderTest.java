package nl.han.ica.core.issue.solver.magicliteral;

import junit.framework.Assert;
import nl.han.ica.core.Context;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.MagicLiteralDetector;
import nl.han.ica.core.util.ASTUtil;
import nl.han.ica.core.util.FileUtil;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.Map;

public class LiteralSolutionBuilderTest {

    private LiteralSolutionBuilder lsb;


    @Mock
    private IssueSolver issueSolver;
    @Mock
    private Map<String, Parameter> parameters;

    private String parameterConstantName;

    private MagicLiteralDetector magicLiteralDetector;

    @Before
    public void setUp() throws Exception {
        this.magicLiteralDetector = new MagicLiteralDetector();
        Context context = new Context();
        File file = new File(MagicLiteralDetector.class.getResource("/issueclasses/UnusedCodeTester.java").toURI());

        String source = FileUtil.getFileContent(file);
        Document document = new Document(source);
        ASTParser astParser = ASTParser.newParser(AST.JLS3);
        astParser.setSource(document.get().toCharArray());
        CompilationUnit compilationUnit = (CompilationUnit) astParser.createAST(null);

        SourceFile sourceFile = new SourceFile(file);
        sourceFile.setCompilationUnit(compilationUnit);
        context.addSourceFile(sourceFile);
        magicLiteralDetector.setContext(context);
        magicLiteralDetector.detectIssues();
        parameterConstantName = "Test";
        Issue issue = magicLiteralDetector.getIssues().iterator().next();
        lsb = new LiteralSolutionBuilderMock(issue, issueSolver, parameters, parameterConstantName);
    }


    @Test
    public void testExistingConstantExists() throws Exception {
        Assert.assertTrue(lsb.existingConstantExists("CONSTANT_NAME"));
    }

    @Test
    public void testCreateConstant() throws Exception {
        lsb.createConstant("CONSTANTINO", "293");
        ListRewrite listRewrite = lsb.rewrite.getListRewrite(lsb.literalClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        Assert.assertEquals("private static final int CONSTANTINO=293;\n", listRewrite.getRewrittenList().get(0).toString());
    }

    @Test
    public void testReplaceMagicLiteralWithConstant() throws Exception {
        String constantName = "CONSTANT_NAME";
        lsb.replaceMagicLiteralWithConstant(constantName);
        if (lsb.literal.getParent() instanceof InfixExpression){
            Assert.assertTrue( lsb.rewrite.get( lsb.literal.getParent(), InfixExpression.RIGHT_OPERAND_PROPERTY).toString().contains(constantName)
                    || lsb.rewrite.get( lsb.literal.getParent(), InfixExpression.LEFT_OPERAND_PROPERTY).toString().contains(constantName)  );
        }else if(lsb.literal.getParent() instanceof MethodInvocation){
            Assert.assertTrue(lsb.rewrite.getListRewrite(lsb.literal.getParent(), MethodInvocation.ARGUMENTS_PROPERTY).getRewrittenList().get(0).toString().contains(constantName));
        }
    }



    private class LiteralSolutionBuilderMock extends LiteralSolutionBuilder {


        private LiteralSolutionBuilderMock(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters, String parameterConstantName) {
            super(issue, issueSolver, parameters, parameterConstantName);
        }

        @Override
        protected String getValueForConstant() {
            return null;
        }

        @Override
        protected Expression getInitializerExpression(String value, AST ast) {
            return ast.newNumberLiteral(value);
        }

        @Override
        protected Type getType(AST ast) {
            return ast.newPrimitiveType(PrimitiveType.INT);
        }
    }

}
