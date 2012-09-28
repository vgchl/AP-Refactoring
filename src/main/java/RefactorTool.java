import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.app.controllers.RefactorToolApp;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

public class RefactorTool {

    public static void main(String[] args) throws PMDException, IOException  {
        RefactorToolApp.startApp(args);

    }
}
