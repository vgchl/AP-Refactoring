package nl.han.ica.core.issues.detector;

import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    @Before
    public void setUp() throws IOException {

        System.out.println("setUp");
        detector = new PullUpFieldDetector();

        StringBuffer buffer = new StringBuffer("");

        buffer.append(getSuperClassFile());
        buffer.append(getAppelFile());
        buffer.append(getBanaanFile());

        ASTParser parser = createParser();

        System.out.println("setSource for superclass file");
        parser.setSource(buffer.toString().toCharArray());
        CompilationUnit superclass = (CompilationUnit) parser.createAST(null);
        System.out.println(superclass.toString());
        compilationUnits.add(superclass);

//        ASTParser parser2 = createParser();
//        System.out.println("setSource for appel file");
//        parser2.setSource(getAppelFile().toCharArray());
//        CompilationUnit appelCu = (CompilationUnit) parser2.createAST(null);
//        System.out.println(appelCu.toString());
//        compilationUnits.add(appelCu);
//
//        ASTParser parser3 = createParser();
//        System.out.println("setSource for banaanfile");
//        parser3.setSource(getBanaanFile().toCharArray());
//        CompilationUnit banaanCu = (CompilationUnit) parser3.createAST(null);
//        System.out.println(banaanCu.toString());
//        compilationUnits.add(banaanCu);


        System.out.println("setting compilationunits in detector. size: " + compilationUnits.size());
        detector.setCompilationUnits(compilationUnits);

        System.out.println("done");
    }

    private ASTParser createParser() {
        System.out.println("creating parser");
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setCompilerOptions(JavaCore.getOptions());
        return parser;
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
