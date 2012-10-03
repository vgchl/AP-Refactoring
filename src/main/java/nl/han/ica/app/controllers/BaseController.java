package nl.han.ica.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.log4j.Logger;

import java.io.IOException;

import static org.apache.log4j.Logger.getLogger;

public abstract class BaseController {

    protected Logger logger = getLogger(getClass().getName());
    private String viewPath;

    public BaseController(String viewPath) {
        this.viewPath = viewPath;
    }

    public Scene getScene() {
        if (viewPath == null) {
            throw new IllegalStateException("No viewPath given.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            logger.fatal("Error loading StrategySelectionController scene.");
        }

        return new Scene((Parent) fxmlLoader.getRoot());
    }

}
