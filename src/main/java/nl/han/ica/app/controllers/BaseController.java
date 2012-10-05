package nl.han.ica.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.log4j.Logger;

import java.io.IOException;

import static org.apache.log4j.Logger.getLogger;

/**
 * Provides all basic functionality for standard controllers.
 */
public abstract class BaseController {

    /**
     * Debugging logger.
     */
    protected Logger logger;
    protected FXMLLoader fxmlLoader;


    public BaseController() {
        logger = getLogger(getClass().getName());
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(this);
    }

    /**
     * Get the view belonging to the controller.
     *
     * @return The view belonging to the controller.
     */
    abstract public Parent getView();

    /**
     * Load and build the controller's view from its FXML resource.
     *
     * @param viewPath The path to the FXML resource.
     * @return The initialized view's root element.
     */
    protected Parent buildView(String viewPath) {
        if (viewPath == null) {
            throw new IllegalArgumentException("No viewPath given.");
        }

        fxmlLoader.setLocation(getClass().getResource(viewPath));

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            logger.fatal("Error loading StrategySelectionController scene.");
            // TODO: Throw new Exception
        }
        return (Parent) fxmlLoader.getRoot();
    }

}
