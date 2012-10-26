package nl.han.ica.app.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import nl.han.ica.app.models.CodeEditor;
import nl.han.ica.app.models.parameter.ParameterChangeListener;
import nl.han.ica.app.models.parameter.ParameterControlFactory;
import nl.han.ica.app.models.parameter.ParameterEvent;
import nl.han.ica.core.Solution;
import nl.han.ica.core.strategies.solvers.Parameter;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IssueResolveChangeController extends BaseController {

    private Solution solution;
    private CodeEditor editorBefore;
    private CodeEditor editorAfter;

    private EventListenerList parameterChangeListeners;
    private ParameterControlFactory parameterControlFactory;

    @FXML
    protected WebView editorBeforeView;
    @FXML
    protected WebView editorAfterView;
    @FXML
    protected GridPane parametersContainer;

    public IssueResolveChangeController() {
        parameterChangeListeners = new EventListenerList();
        parameterControlFactory = new ParameterControlFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeEditors();
    }

    private void initializeParametersForm() {
        int i = 0, row, col;
        for (Parameter parameter : solution.getParameters().values()) {
            Control control = parameterControlFactory.controlForParameter(parameter, new EventHandler<ParameterEvent>() {
                @Override
                public void handle(ParameterEvent event) {
                    triggerParameterChange(event);
                }
            });
            Label label = new Label(parameter.getTitle());
            label.setLabelFor(control);

            row = (int) Math.floor(i / 2);
            col = (i % 2 == 0) ? 0 : 2;

            parametersContainer.add(label, col, row);
            parametersContainer.add(control, col + 1, row);
            i++;
        }
    }

    public void addParameterChangeListener(ParameterChangeListener listener) {
        parameterChangeListeners.add(ParameterChangeListener.class, listener);
    }

    public void removeParameterChangeListener(ParameterChangeListener listener) {
        parameterChangeListeners.remove(ParameterChangeListener.class, listener);
    }

    protected void triggerParameterChange(ParameterEvent event) {
        ParameterChangeListener[] listeners = parameterChangeListeners.getListeners(ParameterChangeListener.class);
        for (ParameterChangeListener listener : listeners) {
            listener.changed(event);
        }
    }

    private void initializeEditors() {
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
            initializeParametersForm();
        }
    }

}
