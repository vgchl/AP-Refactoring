var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
    mode: "text/x-java",
    lineNumbers: true,
    lineWrapping: true,
    onCursorActivity: function() {
        editor.setLineClass(hlLine, null, null);
        hlLine = editor.setLineClass(editor.getCursor().line, null, "activeline");
    }
});
var hlLine = editor.setLineClass(0, "activeline");
