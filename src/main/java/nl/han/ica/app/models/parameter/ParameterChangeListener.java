package nl.han.ica.app.models.parameter;

import java.util.EventListener;

/**
 * Allows implementing classes to react to parameter changes.
 */
public interface ParameterChangeListener extends EventListener {

    /**
     * Called after changes to a parameter occurred.
     *
     * @param event The parameter change event.
     */
    public void changed(ParameterEvent event);

}
