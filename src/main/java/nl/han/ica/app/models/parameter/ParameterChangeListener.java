/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


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
