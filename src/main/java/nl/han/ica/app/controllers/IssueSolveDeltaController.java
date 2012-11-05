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
import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Handles the presentation of a solution to an issue.
 */
public class IssueSolveDeltaController extends BaseController {

    private Delta delta;
    private Map<String, Parameter> parameters;
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

    public IssueSolveDeltaController(Delta delta, Map<String, Parameter> parameters) {
        this.delta = delta;
        this.parameters = parameters;
        parameterChangeListeners = new EventListenerList();
        parameterControlFactory = new ParameterControlFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initializeEditors();
        initializeParametersForm();
    }

    private void initializeParametersForm() {
        parametersContainer.getChildren().clear();
        int i = 0, row, col;
        for (Parameter parameter : parameters.values()) {
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
        editorBefore.setValue(delta.getBefore().toString());
        editorAfter.setValue(delta.getAfter().toString());
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

}
