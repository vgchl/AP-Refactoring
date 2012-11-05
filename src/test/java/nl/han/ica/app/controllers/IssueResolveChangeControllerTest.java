package nl.han.ica.app.controllers;

import javafx.scene.Parent;
import nl.han.ica.core.Job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.util.ResourceBundle;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author: Wouter Konecny
 * @created: 26-10-12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseController.class, URL.class})
public class IssueResolveChangeControllerTest {

    private IssueResolveChangeController issueResolveChangeController = null;

    private BaseController baseController;

    @Mock
    private Job job;

    @Mock
    private URL url;

    @Mock
    private ResourceBundle resourceBundle;

    @Mock
    private Parent parent;

    @Before
    public void setUp() throws Exception {
        baseController = PowerMockito.mock(BaseController.class);
        job = mock(Job.class);
        url = PowerMockito.mock(URL.class);

        issueResolveChangeController = new IssueResolveChangeController();
    }

    @Test
    public void testInitialize() throws Exception {
        doNothing().when(baseController).initialize(url, resourceBundle);
    }

    @Test
    public void testGetView() throws Exception {
        when(baseController.getView()).thenReturn(parent);

    }

    @Test
    public void testInitializeEditors() throws Exception {

    }

    @Test
    public void testGetSolution() throws Exception {

    }

    @Test
    public void testSetSolution() throws Exception {

    }
}
