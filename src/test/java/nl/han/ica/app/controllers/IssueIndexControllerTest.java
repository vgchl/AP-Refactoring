package nl.han.ica.app.controllers;

import javafx.scene.Parent;
import nl.han.ica.core.Job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author: Wouter Konecny
 * @created: 26-10-12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseController.class, URL.class})
public class IssueIndexControllerTest {

    private IssueIndexController issueIndexController = null;

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
        parent = mock(Parent.class);

        issueIndexController = new IssueIndexController(job);
    }

    @Test
    public void testNotNull() {
        assertTrue(issueIndexController != null);
    }
}
