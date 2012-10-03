package nl.han.ica.app;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.han.ica.app.controllers.BaseController;
import nl.han.ica.app.controllers.ResolveIssuesController;
import nl.han.ica.app.controllers.StrategySelectionController;

import java.io.*;

public class RefactorTool extends Application {

    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - v0.Bulbasaur";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        stage.setTitle(APP_TITLE);

        BaseController strategySelectionController = new StrategySelectionController(stage);
        stage.setScene(strategySelectionController.getScene());

        stage.show();
    }
}
