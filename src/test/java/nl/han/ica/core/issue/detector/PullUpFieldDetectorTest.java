package nl.han.ica.core.issue.detector;

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

//    private PullUpFieldDetector detector;
//
//    private Set<CompilationUnit> compilationUnits;
//
//    @Before
//    public void setUp() throws IOException {
//
//        // build some example files
//        // make compilationunits from them
//        // iterate over compilationunits and accept the detector
//        // get issues from detector
//
//        detector = new PullUpFieldDetector();
//
//        Set<SourceFile> sourceFiles = new HashSet<SourceFile>();
//        SourceFile superClassSourceFile = new SourceFile(getSuperClassFile());
//        sourceFiles.add(superClassSourceFile);
//
//        for (File file : getSubclassFiles()) {
//            SourceFile sourceFile = new SourceFile(file);
//            sourceFiles.add(sourceFile);
//        }
//
//        Parser parser = new Parser();
//
//        compilationUnits = parser.parse(sourceFiles);
//        detector.setCompilationUnits(compilationUnits);
//    }
//
//    @Test
//    public void testDetectIssuesNotNull() {
//        Set<Issue> issues = detector.detectIssues();
//        assertNotNull(issues);
//    }
//
//    @Test
//    public void testDetectIssues() {
//        assertEquals(1, detector.detectIssues().size());
//    }
//
//    private File getSuperClassFile() throws IOException {
//        File file = new File("");
//
//        String content = "package nl.random.test;\n" +
//                "\n" +
//                "public abstract class Fruit {\n" +
//                "\n" +
//                "}\n";
//
//
//        FileUtil.setFileContent(file, content);
//
//        return file;
//    }
//
//    private List<File> getSubclassFiles() throws IOException {
//        List<File> files = new ArrayList<File>();
//
//        String banaan = "package nl.random.test;\n" +
//                "\n" +
//                "public class Banaan extends Fruit {\n" +
//                "\n" +
//                "\tprivate String naam;\n" +
//                "\n" +
//                "\tpublic Banaan() {\n" +
//                "\t\tnaam = \"Banaan\";\n" +
//                "\t}\n" +
//                "\n" +
//                "\tpublic String getNaam() {\n" +
//                "\t\treturn naam;\n" +
//                "\t}\n" +
//                "}\n";
//
//        String appel = "package nl.random.test;\n" +
//                "\n" +
//                "public class Appel extends Fruit {\n" +
//                "\n" +
//                "\tprivate String naam = \"\";\n" +
//                "\n" +
//                "\tpublic Appel() {\n" +
//                "\t\tnaam = \"Appel\";\n" +
//                "\t}\n" +
//                "\n" +
//                "\tpublic String getNaam() {\n" +
//                "\t\treturn naam;\n" +
//                "\t}\n" +
//                "}\n";
//
//        File banaanFile = new File("");
//        FileUtil.setFileContent(banaanFile, banaan);
//        files.add(banaanFile);
//
//        File appelFile = new File("");
//        FileUtil.setFileContent(appelFile, appel);
//        files.add(appelFile);
//
//
//        return files;
//    }
}
