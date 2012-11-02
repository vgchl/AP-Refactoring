package nl.han.ica.app.models;

import javafx.scene.web.WebView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.doNothing;

/**
 * @author: Wouter Konecny
 * @created: 26-10-12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(WebView.class)
public class CodeEditorTest {

    private CodeEditor codeEditor = null;

    private WebView webView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        webView = PowerMockito.mock(WebView.class);

        doNothing().when(codeEditor).initializeWebView();
        codeEditor = new CodeEditor(webView);
    }

    @Test
    public void testSetValue() throws Exception {
        codeEditor.setValue("x");
    }

    @Test
    public void testHighlightLine() throws Exception {
        codeEditor.highlightLine(1, "MyClass", "MyClass");
    }

    @Test
    public void testHighlightText() throws Exception {
        codeEditor.highlightText(1, 2, 3, 4, "MyClass");
    }
}
