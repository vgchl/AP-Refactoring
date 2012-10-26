package nl.han.ica.app.models.parameter;

import javafx.event.Event;
import javafx.event.EventType;
import nl.han.ica.core.strategies.solvers.Parameter;

public class ParameterEvent extends Event {

    private Parameter parameter;

    public ParameterEvent() {
        super(new EventType<ParameterEvent>());
    }

    public ParameterEvent(Parameter parameter) {
        this();
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

}
