package nl.han.ica.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.log4j.Logger;

import java.io.IOException;

import static org.apache.log4j.Logger.getLogger;

public abstract class BaseController {

    protected Logger logger = getLogger(getClass().getName());

    abstract public Parent getView();

    protected Parent buildView(String viewPath) {
        if (viewPath == null) {
            throw new IllegalArgumentException("No viewPath given.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));

        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            logger.fatal("Error loading StrategySelectionController scene.");
        }

        return (Parent) fxmlLoader.getRoot();
    }

}
