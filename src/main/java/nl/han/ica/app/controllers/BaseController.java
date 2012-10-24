package nl.han.ica.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.apache.log4j.Logger.getLogger;

/**
 * Provides all basic functionality for standard controllers.
 */
public abstract class BaseController implements Initializable {

    /**
     * Debugging logger.
     */
    protected final Logger logger;
    protected FXMLLoader fxmlLoader;

    /**
     * Initialize a new BaseController.
     */
    public BaseController() {
        logger = getLogger(getClass().getName());

        logger.info("Constructing.");

        fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing.");
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
    protected Parent buildView(String viewPath) throws IOException {
        if (viewPath == null) {
            throw new IllegalArgumentException("No viewPath given.");
        }
        logger.info("Building view: " + viewPath);
        fxmlLoader.setLocation(getClass().getResource(viewPath));
        fxmlLoader.load();
        return (Parent) fxmlLoader.getRoot();
    }

}
