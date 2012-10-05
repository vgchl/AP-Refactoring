package nl.han.ica.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.han.ica.app.controllers.BaseController;
import nl.han.ica.app.controllers.StrategySelectionController;
import nl.han.ica.core.Job;

import java.io.IOException;

public class RefactorTool extends Application {

    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - v0.Bulbasaur";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Scene scene = new Scene(null);

        BaseController strategySelectionController = new StrategySelectionController(scene, new Job());
        scene.setRoot(strategySelectionController.getView());

        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.show();
    }
}
