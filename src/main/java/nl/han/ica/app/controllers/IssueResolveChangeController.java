package nl.han.ica.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.web.WebView;
import nl.han.ica.app.CodeEditor;
import nl.han.ica.core.Solution;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IssueResolveChangeController extends BaseController implements Initializable {

    private Solution solution;
    private CodeEditor editorBefore;
    private CodeEditor editorAfter;

    @FXML
    protected WebView editorBeforeView;
    @FXML
    protected WebView editorAfterView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEditors();
    }

    public void initializeEditors() {
        editorBefore = new CodeEditor(editorBeforeView);
        editorAfter = new CodeEditor(editorAfterView);
    }

    private void updateEditors() {
        editorBefore.setValue(solution.getBefore());
        editorAfter.setValue(solution.getAfter());
    }

    @Override
    public Parent getView() {
        try {
            return buildView("/views/issue_resolve_change.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        if (this.solution != solution) {
            this.solution = solution;
            updateEditors();
        }
    }
}
