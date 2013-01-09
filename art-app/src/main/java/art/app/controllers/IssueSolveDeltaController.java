package art.app.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import art.app.models.CodeEditor;
import art.app.models.parameter.ParameterChangeListener;
import art.app.models.parameter.ParameterControlFactory;
import art.app.models.parameter.ParameterEvent;
import art.core.solution.Delta;
import art.core.solution.Parameter;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Handles the presentation of a single delta from a solution to an issue.
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
    @FXML
    protected TitledPane screenTitle;

    /**
     * Instantiate a new IssueSolveDeltaController.
     *
     * @param delta      The delta this controller handles.
     * @param parameters The parameters used in the creation of the delta's solution.
     */
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
        screenTitle.setText(delta.getFile().getName());
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

    /**
     * Add a listener for the parameter changed event.
     *
     * @param listener Parameter change listener
     */
    public void addParameterChangeListener(ParameterChangeListener listener) {
        parameterChangeListeners.add(ParameterChangeListener.class, listener);
    }

    /**
     * Remove a listener for the parameter changed event.
     *
     * @param listener Parameter change listener
     */
    public void removeParameterChangeListener(ParameterChangeListener listener) {
        parameterChangeListeners.remove(ParameterChangeListener.class, listener);
    }

    /**
     * Trigger the parameter change event and notify all listeners.
     *
     * @param event The causing ParameterEvent
     */
    protected void triggerParameterChange(ParameterEvent event) {
        ParameterChangeListener[] listeners = parameterChangeListeners.getListeners(ParameterChangeListener.class);
        for (ParameterChangeListener listener : listeners) {
            listener.changed(event);
        }
    }

    private void initializeEditors() {
        editorBefore = new CodeEditor(editorBeforeView);
        editorBefore.setValue(delta.getBefore());
        editorAfter = new CodeEditor(editorAfterView);
        editorAfter.setValue(delta.getAfter());

        highlightChangesInEditor();
    }

    private void highlightChangesInEditor() {
        List<difflib.Delta> deltas = delta.getDifferences();
        for (difflib.Delta del : deltas) {
            int originalChangeStartingPosition = del.getOriginal().getPosition();
            int originalChangeEndPosition = del.getOriginal().last();
            String beforeChangeTypeClass = del.getType().toString().toLowerCase();
            editorBefore.highlightLines(originalChangeStartingPosition, originalChangeEndPosition, beforeChangeTypeClass);

            int revisedChangeStartingPosition = del.getRevised().getPosition();
            int revisedChangeEndPosition = del.getRevised().last();
            String afterChangeTypeClass = del.getType().toString().toLowerCase();
            editorAfter.highlightLines(revisedChangeStartingPosition, revisedChangeEndPosition, afterChangeTypeClass);
        }
    }

    @Override
    public Parent getView() {
        try {
            return buildView("/views/issue_solve_delta.fxml");
        } catch (IOException e) {
            logger.fatal("Could not build the view from the FXML document.", e);
            return null;
        }
    }

}
