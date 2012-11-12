package nl.han.ica.core.issues.detector;

import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;
import nl.han.ica.core.parser.Parser;
import nl.han.ica.core.util.FileUtil;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
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

    private TemporaryFolder folder;

    @Before
    public void setUp() throws IOException {

        System.out.println("setUp");
        detector = new PullUpFieldDetector();
        folder = new TemporaryFolder();

        Set<SourceFile> sourceFiles = new HashSet<SourceFile>();
        System.out.println("creating superclass SourceFile");
        SourceFile superClassSourceFile = new SourceFile(getSuperClassFile());

        sourceFiles.add(superClassSourceFile);

        System.out.println("creating subclass SourceFiles");
        for (File file : getSubclassFiles()) {
            SourceFile sourceFile = new SourceFile(file);
            sourceFiles.add(sourceFile);
        }
        System.out.println("creating parser");
        Parser parser = new Parser();

        System.out.println("parsing sourceFiles to compilationunits");
        compilationUnits = parser.parse(sourceFiles);

        System.out.println("setting compilationunits in detector");
        detector.setCompilationUnits(compilationUnits);

        System.out.println("done");
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

    private File getSuperClassFile() throws IOException {
        File file = folder.newFile("Fruit.java");

        String content = "package nl.random.test;\n" +
                "\n" +
                "public abstract class Fruit {\n" +
                "\n" +
                "}\n";


        FileUtil.setFileContent(file, content);

        return file;
    }

    private List<File> getSubclassFiles() throws IOException {
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

        File banaanFile = folder.newFile("Banaan.java");
        FileUtil.setFileContent(banaanFile, banaan);
        files.add(banaanFile);

        File appelFile = folder.newFile("Appel.java");
        FileUtil.setFileContent(appelFile, appel);
        files.add(appelFile);


        return files;
    }
}
