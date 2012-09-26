import net.sourceforge.pmd.*;
import nl.han.ica.app.controllers.RefactorToolApp;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class RefactorTool {

    public static void main(String[] args) {
        RefactorToolApp.startApp(args);
    }
//    public static void main(String[] args) throws PMDException, IOException {
//        try {
//            PMD pmd = new PMD();
//
//            URL url = RefactorTool.class.getResource("UnusedCodeTester.java");
//            InputStream is = RefactorTool.class.getResourceAsStream("UnusedCodeTester.java");
//
//            File f = new File(url.toURI());
//            System.out.println(f.getAbsolutePath());
//
//
//            RuleSet ruleSet = new RuleSet();
//
//            InputStream rs = pmd.getClassLoader().getResourceAsStream("rulesets/unusedcode.xml");
//
//            RuleSetFactory ruleSetFactory = new RuleSetFactory();
//            ruleSet.addRuleSet(ruleSetFactory.createRuleSet(rs, pmd.getClassLoader()));
//
//            RuleContext rc = new RuleContext();
//            rc.setSourceCodeFilename(f.getAbsolutePath());
//
//
//            pmd.processFile(is, ruleSet, rc);
//            System.out.println(rc.getReport().getCountSummary().size());
//        } catch (PMDException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//    }
}
