package nl.han.ica.app.models.parameter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import nl.han.ica.core.strategies.solvers.Parameter;

public class ParameterControlFactory {

    public Control controlForParameter(final Parameter parameter) {
        return controlForParameter(parameter, null);
    }

    public Control controlForParameter(final Parameter parameter, final EventHandler<ParameterEvent> eventHandler) {
        if (parameter.getValue().getClass() == String.class) {
            return createTextFieldControl(parameter, eventHandler);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + parameter.getValue().getClass());
        }
    }

    private TextField createTextFieldControl(final Parameter parameter, final EventHandler<ParameterEvent> eventHandler) {
        TextField textField = new TextField();
        textField.setText((String) parameter.getValue());
        textField.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                parameter.setValue(((TextField) event.getSource()).getText());
                if (null != eventHandler) {
                    eventHandler.handle(new ParameterEvent(parameter));
                }
            }
        });
        return textField;
    }

}
