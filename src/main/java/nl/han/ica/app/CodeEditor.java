package nl.han.ica.app;

import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringEscapeUtils;

public class CodeEditor {

    private WebView webView;

    public CodeEditor(WebView webView) {
        this.webView = webView;
        this.webView.getEngine().load(getClass().getResource("/editor/editor.html").toExternalForm());
    }

    public void setValue(String value) {
        StringBuilder script = new StringBuilder("editor.setValue('");
        script.append(StringEscapeUtils.escapeEcmaScript(value));
        script.append("')");
        execute(script.toString());
    }

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

    public void highlightText(int lineBegin, int columnBegin, int lineEnd, int columnEnd, String className) {
        Position start = new Position(lineBegin-1, columnBegin-1);
        Position end = new Position(lineEnd-1, columnEnd);
        StringBuilder script = new StringBuilder("editor.markText(");
        script.append(start.toScript());
        script.append(", ");
        script.append(end.toScript());
        script.append(", ");
        script.append("'").append(className).append("'");
        script.append(")");
        execute(script.toString());
    }

    private Object execute(String script) {
        return webView.getEngine().executeScript(script);
    }

    public class Position {
        private int line;
        private int column;

        public Position(int line, int column) {
            this.line = line;
            this.column = column;
        }

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
