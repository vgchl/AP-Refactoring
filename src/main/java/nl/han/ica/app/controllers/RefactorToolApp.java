package nl.han.ica.app.controllers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nl.han.ica.app.views.WorkspaceStage;

public class RefactorToolApp extends Application {
    private WorkspaceStage workspace;


    public static void startApp( String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new StackPane(), 300, 300));
        stage.show();
    }
}
