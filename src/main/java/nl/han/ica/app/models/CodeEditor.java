package nl.han.ica.app.models;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.event.EventHandler;
import javafx.scene.web.WebEvent;
import netscape.javascript.JSException;
import org.apache.log4j.Logger;

/**
 * Wrapper around a WebView containing a JavaScript code editor plugin. Provides easy means of communication between the
 * Java and the JavaScript environments.
 */
public class CodeEditor {

    private WebView webView;
    private List<String> scriptCache;
    private Logger logger;

    /**
     * The code editor constructor.
     *
     * @param webView The WebView where the editor is loaded in.
     */
    public CodeEditor(WebView webView) {
        logger = Logger.getLogger(getClass());
        logger.info("Created CodeEditor");
        scriptCache = new ArrayList<>();
        this.webView = webView;
        initializeWebView();
    }

    /**
     * Sets the value of the Code Editor.
     *
     * @param value The value to set in the Code Editor.
     */
    public void setValue(String value) {
        StringBuilder script = new StringBuilder("editor.setValue('");
        script.append(StringEscapeUtils.escapeEcmaScript(value));
        script.append("')");
        execute(script.toString());
    }

    /**
     * Highlights the given line number in the Code Editor.
     *
     * @param lineNr              The line number to highlight.
     * @param textClassname       The classname.
     * @param backgroundClassName The background classname.
     */
    public void highlightLine(int lineNr, String textClassname, String backgroundClassName) {
        StringBuilder script = new StringBuilder("editor.setLineClass(");
        script.append(lineNr).append(", ");
        if (null != textClassname) {
            script.append("'").append(textClassname).append("'");
        } else {
            script.append("null");
        }
        script.append(", ");
        if (null != backgroundClassName) {
            script.append("'").append(backgroundClassName).append("'");
        } else {
            script.append("null");
        }
        script.append(");");
        execute(script.toString());
    }

    /**
     * Highlights a part of a line or a block, for example one word or a few lines.
     *
     * @param lineBegin   The line to start highlight.
     * @param columnBegin The column to start highlighting.
     * @param lineEnd     The line where the highlighting ends.
     * @param columnEnd   The column where the highlighting stops.
     * @param className   The current class name.
     */
    public void highlightText(int lineBegin, int columnBegin, int lineEnd, int columnEnd, String className) {
        Position start = new Position(lineBegin - 1, columnBegin - 1);
        Position end = new Position(lineEnd - 1, columnEnd);
        StringBuilder script = new StringBuilder("editor.markText(");
        script.append(start.toScript());
        script.append(", ");
        script.append(end.toScript());
        script.append(", ");
        script.append("'").append(className).append("'");
        script.append(")");
        execute(script.toString());
    }

    /**
     * Initialize the webview that contains the browser environment for the CodeEditor.
     */
    private void initializeWebView() {
        logger.info("Initialising web view");
        
        String debug = getClass().getResource("/editor/editor.html").toExternalForm();
        webView.getEngine().load(debug);
        
        webView.getEngine().getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
            @Override           
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {

                if (newState == Worker.State.SUCCEEDED && !scriptCache.isEmpty() && webView.getEngine().getDocument() != null) {            
                    for (String script : scriptCache) {
                        execute(script);
                    }
                    scriptCache.clear();
                }else if(newState == Worker.State.SUCCEEDED && webView.getEngine().getDocument() == null){ 
                    webView.getEngine().reload();
                }
            }
        });
        
    }

    /**
     * Execute a JavaScript script in the CodeEditor's browser environment.
     *
     * @param script The script to run
     */
    protected void execute(final String script) {
        if (webView.getEngine().getDocument() != null) {
            logger.info("Executing...  " + script);
            try {
                webView.getEngine().executeScript(script); 
            } catch(JSException ex){
                logger.fatal("Executed script " + ex);               
            }
            
        } else {
            scriptCache.add(script);
        }
    }

    /**
     * Represents a line and column in the JavaScript Editor.
     */
    public static class Position {
        private int line;
        private int column;

        /**
         * Instantiate a new Position.
         *
         * @param line   The line number
         * @param column The column number
         */
        public Position(int line, int column) {
            this.line = line;
            this.column = column;
        }

        /**
         * Outputs the position in JavaScript formatting that can be used by the CodeEditor.
         *
         * @return JavaScript representation of this position.
         */
        public String toScript() {
            StringBuilder script = new StringBuilder("{ ");
            script.append("line: ").append(line);
            script.append(", ");
            script.append("ch: ").append(column);
            script.append(" }");
            return script.toString();
        }
    }

}
