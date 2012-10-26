package nl.han.ica.app.models.parameter;

import java.util.EventListener;

public interface ParameterChangeListener extends EventListener {

    public void changed(ParameterEvent event);

}
