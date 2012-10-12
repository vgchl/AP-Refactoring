editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
    mode: "text/x-java",
    lineNumbers: true,
    onCursorActivity: function() {
        editor.setLineClass(hlLine, null, null);
        hlLine = editor.setLineClass(editor.getCursor().line, null, "activeline");
    },
    readOnly: true
});
var hlLine = editor.setLineClass(0, "activeline");
