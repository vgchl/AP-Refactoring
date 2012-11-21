package nl.han.ica.core.strategies.solvers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 3-10-12
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public class InputFile {

    public static File createTempFile(){
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
                    "        if(i == 0){\n" +
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return file;

    }
}
