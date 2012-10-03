package nl.han.ica.app.controllers;

import junit.framework.Assert;
import nl.han.ica.core.strategies.TestInputFile;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class PMDConnectorTest {

    private PMDConnector pmdConnector;
    private File file;

    @Before
    public void setUp() throws Exception {
        pmdConnector = new PMDConnector();
        file = TestInputFile.createTempFile();
    }

    @Test
    public void testRunPMD() throws Exception {
        pmdConnector.runPMD(file);
        Assert.assertNotNull(pmdConnector.getReportTree());
    }

    @Test
    public void testProcessResults() throws Exception {
        pmdConnector.processResults();
        pmdConnector.runPMD(file);
        Assert.assertNull(pmdConnector.getReportTree());
    }
}
