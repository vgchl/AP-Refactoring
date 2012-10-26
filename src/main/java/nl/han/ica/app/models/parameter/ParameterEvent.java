package nl.han.ica.app.models.parameter;

import javafx.event.Event;
import javafx.event.EventType;
import nl.han.ica.core.strategies.solvers.Parameter;

/**
 * Provides context to events involving parameters.
 */
public class ParameterEvent extends Event {

    private Parameter parameter;

    /**
     * Instantiate a new parameter event.
     */
    public ParameterEvent() {
        super(new EventType<ParameterEvent>());
    }

    /**
     * Instantiate a new parameter event for a parameter.
     * @param parameter The parameter involved in the event.
     */
    public ParameterEvent(Parameter parameter) {
        this();
        this.parameter = parameter;
    }

    /**
     * Returns parameter involved in the event.
     * @return parameter involved in the event
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Set the parameter involved in the event.
     * @param parameter The parameter involved in the event.
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

}
