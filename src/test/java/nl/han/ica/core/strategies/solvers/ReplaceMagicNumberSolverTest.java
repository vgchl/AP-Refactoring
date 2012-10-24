package nl.han.ica.core.strategies.solvers;

import java.io.*;

import junit.framework.Assert;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.ast.SimpleJavaNode;
import net.sourceforge.pmd.rules.XPathRule;
import net.sourceforge.pmd.symboltable.SourceFileScope;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Corne
 * Date: 1-10-12
 * Time: 12:20
 */
public class ReplaceMagicNumberSolverTest {

    private ReplaceMagicNumberSolver replaceMagicNumber;

    @Before
    public void setUp() throws Exception {


        XPathRule magicNumberRule = new XPathRule();
        magicNumberRule.setName("Avoid using Literals in Conditional Statements");
        magicNumberRule.setMessage("Avoid using Literals in Conditional Statements:16");
        magicNumberRule.setDescription("Avoid using Literals in Conditional Statements:16");
        

        //AvoidLiteralsInIfCondition
        RuleContext context = new RuleContext();

        File file = TestInputFile.createTempFile();

        context.setSourceCodeFilename(file.getAbsolutePath());
        SimpleJavaNode node = new SimpleJavaNode(1);

        SourceFileScope scope = new SourceFileScope(file.getAbsolutePath());
        node.setScope(scope);

        node.testingOnly__setBeginColumn(17);
        node.testingOnly__setBeginLine(16);

        RuleViolation ruleViolation = new RuleViolation(magicNumberRule, context, node);
        
        replaceMagicNumber = new ReplaceMagicNumberSolver(ruleViolation);
        
        replaceMagicNumber.buildAST(file);
    }

    @Test
    public void testRewriteAST() throws Exception {
        BufferedReader in;
        replaceMagicNumber.setReplaceName("MAGICINT");
        replaceMagicNumber.rewriteAST();


        ASTParser parser = ASTParser.newParser(AST.JLS3);
        in = new BufferedReader(new FileReader(rewrittenFile()));
        final StringBuffer buffer = new StringBuffer();
        String line;
        while (null != (line = in.readLine())) {
            buffer.append(line).append("\n");
        }

        parser.setSource(buffer.toString().toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);

        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

        Assert.assertEquals(replaceMagicNumber.getCompilationUnit().toString(), compilationUnit.toString());

    }

    private static File rewrittenFile(){
        File file = null;
        try {
            file = File.createTempFile("TempFile.txt", ".tmp");
            file.deleteOnExit();

            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write("public class UnusedCodeTester {\n" +
                    "\n" +
                    
                    "\tpublic static String string1 = \"Si!\";\n" +
                    " \tpublic static String string2 = \"No!\";\n" +
                    "    public static String MYvariBal = \"x\";\n" +
                    "\tprivate static final int MAGICINT0 = 0;\n" +
                    "\n" +
                    "\tpublic static void main(String[] args) {\n" +
                    "    \tSystem.out.println(string1);    \n" +
                    "    \tused();\n" +
                    "    }\n" +
                    "    \n" +
                    "    private String unused(String a,\n" +
                    "                          String b,\n" +
                    "                          String c) {\n" +
                    "        int i =0;\n" +
                    "        if(i == MAGICINT0){\n" +
                    "\n" +
                    "        }else if(i == 23){\n" +
                    "\n" +
                    "        }\n" +
                    "\n" +
                    "    \treturn \"YES\";\n" +
                    "    \t\n" +
                    "    }\n" +
                    "    \n" +
                    "    private static boolean used() {\n" +
                    "    \treturn true;\n" +
                    "    }\n" +
                    "}");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
