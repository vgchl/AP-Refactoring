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


package nl.han.ica.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.han.ica.app.controllers.BaseController;
import nl.han.ica.app.controllers.IssueDetectorIndexController;
import nl.han.ica.core.Job;

import java.io.IOException;

public class RefactorTool extends Application {

    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - v0.Charmeleon";

    /**
     * The application entry point.
     *
     * @param args Arguments for the application (we have none).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application.
     *
     * @param stage The stage to load the application in (JavaFX 2).
     * @throws IOException When the view could not be loaded.
     * @throws InterruptedException When the application gets interrupted with faulty concurrency.
     */
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Scene scene = new Scene(null);

        BaseController strategySelectionController = new IssueDetectorIndexController(scene, new Job());
        scene.setRoot(strategySelectionController.getView());

        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.show();
    }
}
