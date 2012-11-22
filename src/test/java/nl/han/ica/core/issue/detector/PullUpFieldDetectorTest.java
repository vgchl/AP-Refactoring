package nl.han.ica.core.issue.detector;

import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.parser.Parser;
import nl.han.ica.core.util.FileUtil;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 9-11-12
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class PullUpFieldDetectorTest {

    private PullUpFieldDetector detector;

    private Set<CompilationUnit> compilationUnits;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {

        detector = new PullUpFieldDetector();

        Parser parser = new Parser();

        File fruit = folder.newFile("Fruit.java");
        FileUtil.setFileContent(fruit, getSuperClassFile());
        File banaan = folder.newFile("Banaan.java");
        FileUtil.setFileContent(banaan, getBanaanFile());
        File appel = folder.newFile("Appel.java");
        FileUtil.setFileContent(appel, getAppelFile());

        Set<SourceFile> sourceFiles = new HashSet<SourceFile>();
        sourceFiles.add(new SourceFile(fruit));
        sourceFiles.add(new SourceFile(banaan));
        sourceFiles.add(new SourceFile(appel));

        compilationUnits = parser.parse(sourceFiles);

        detector.setCompilationUnits(compilationUnits);
    }

    @Test
    public void testDetectIssuesNotNull() {

        System.out.println("testDetectIssuesNotNull");
        Set<Issue> issues = detector.detectIssues();
        assertNotNull(issues);
    }

    @Test
    public void testDetectIssues() {
        System.out.println("testDetectIssues");
        assertEquals(1, detector.detectIssues().size());
    }

    private String getSuperClassFile() {

        String content = "package nl.random.test;\n" +
                "\n" +
                "public abstract class Fruit {\n" +
                "\n" +
                "}\n";

        return content;
    }

    private String getBanaanFile() {
        List<File> files = new ArrayList<File>();

        String banaan = "package nl.random.test;\n" +
                "\n" +
                "public class Banaan extends Fruit {\n" +
                "\n" +
                "\tprivate String naam;\n" +
                "\n" +
                "\tpublic Banaan() {\n" +
                "\t\tnaam = \"Banaan\";\n" +
                "\t}\n" +
                "\n" +
                "\tpublic String getNaam() {\n" +
                "\t\treturn naam;\n" +
                "\t}\n" +
                "}\n";
        return banaan;
    }

    private String getAppelFile() {
        String appel = "package nl.random.test;\n" +
                "\n" +
                "public class Appel extends Fruit {\n" +
                "\n" +
                "\tprivate String naam = \"\";\n" +
                "\n" +
                "\tpublic Appel() {\n" +
                "\t\tnaam = \"Appel\";\n" +
                "\t}\n" +
                "\n" +
                "\tpublic String getNaam() {\n" +
                "\t\treturn naam;\n" +
                "\t}\n" +
                "}\n";

        return appel;
    }
}
