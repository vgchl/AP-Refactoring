package nl.han.ica.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import nl.han.ica.app.models.CodeEditor;
import nl.han.ica.core.Solution;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IssueResolveChangeController extends BaseController {

    private Solution solution;
    private CodeEditor editorBefore;
    private CodeEditor editorAfter;

    @FXML
    protected WebView editorBeforeView;
    @FXML
    protected WebView editorAfterView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeEditors();
    }

    /**
     * Initializes the editors (before and after views).
     */
    public void initializeEditors() {
        editorBefore = new CodeEditor(editorBeforeView);
        editorAfter = new CodeEditor(editorAfterView);
    }

    private void updateEditors() {
        logger.info("Updating editors");
        editorBefore.setValue(solution.getBefore());
        editorAfter.setValue(solution.getAfter());
    }

    @Override
    public Parent getView() {
        try {
            return buildView("/views/issue_resolve_change.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

    /**
     * Gets the solution.
     *
     * @return The solution of this controller.
     */
    public Solution getSolution() {
        return solution;
    }

    /**
     * Sets the solution.
     *
     * @param solution The solution for this controller.
     */
    public void setSolution(Solution solution) {
        if (this.solution != solution) {
            this.solution = solution;
            updateEditors();
        }
    }

    @FXML
    public void parametersChanged(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        solution.getParameters().put(textField.getId(), textField.getText());
    }

}
