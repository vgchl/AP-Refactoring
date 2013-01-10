package nl.han.ica.core.issue.detector;

import junit.framework.Assert;
import nl.han.ica.core.Context;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.util.FileUtil;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertThat;


public class MagicLiteralDetectorTest {

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

    }

    @Test
    public void testGetTitle() throws Exception {
        Assert.assertEquals("Magic Literal", magicLiteralDetector.getTitle());
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertEquals("Avoid using literals in conditional statements.", magicLiteralDetector.getDescription());
    }

    @Test
    public void testDetectIssues() throws Exception {
        magicLiteralDetector.detectIssues();
        Iterator<Issue> it = magicLiteralDetector.getIssues().iterator();

        Assert.assertTrue(it.hasNext());

        while(it.hasNext()) {
            Issue currentIssue = it.next();
              Assert.assertTrue(
                      currentIssue.getNodes().get(0) instanceof NumberLiteral ||
                      currentIssue.getNodes().get(0) instanceof StringLiteral ||
                      currentIssue.getNodes().get(0) instanceof CharacterLiteral
              );
        }
    }
}
