package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.SourceHolder;
import nl.han.ica.core.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 7-11-12
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class PullUpFieldSolverTest {
    private PullUpFieldSolver solver;

    private SourceHolder superClass;

    private List<SourceHolder> subclasses;

    @Before
    public void setUP() throws Exception {

        superClass = new SourceHolder();

        subclasses = new ArrayList<SourceHolder>();

        solver = new PullUpFieldSolver(superClass, subclasses);
    }

    @Test
    public void testRewriteAST() {
        fail("Not yet implemented.");
    }

    @Test
    public void testDefaultParameters() {
        fail("Not yet implemented.");
    }

    private File getSuperClassFile() throws IOException {
        File file = new File("");

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
                "\t\n" +
                "\tpublic Banaan()\n" +
                "\t{\n" +
                "\t\tnaam = \"Banaan\";\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic String getNaam()\n" +
                "\t{\n" +
                "\t\treturn naam;\n" +
                "\t}\n" +
                "}\n";

        String appel = "package nl.random.test;\n" +
                "\n" +
                "public class Appel extends Fruit{\n" +
                "\n" +
                "\tprivate String naam = \"\";\n" +
                "\t\n" +
                "\tpublic Appel()\n" +
                "\t{\n" +
                "\t\tnaam = \"Appel\";\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic String getNaam()\n" +
                "\t{\n" +
                "\t\treturn naam;\n" +
                "\t}\n" +
                "}\n";

        File banaanFile = new File("");
        FileUtil.setFileContent(banaanFile, banaan);
        files.add(banaanFile);

        File appelFile = new File("");
        FileUtil.setFileContent(appelFile, appel);
        files.add(appelFile);


        return files;
    }
}
