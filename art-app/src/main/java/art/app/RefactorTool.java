package art.app;

import art.app.controllers.BaseController;
import art.app.controllers.IssueDetectorIndexController;
import art.core.Job;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class RefactorTool extends Application {

    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - v0.Charizard";

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
     * @throws java.io.IOException  When the view could not be loaded.
     * @throws InterruptedException When the application gets interrupted with faulty concurrency.
     */
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Scene scene = new Scene(null);

        BaseController strategySelectionController = new IssueDetectorIndexController(scene, new Job());
        scene.setRoot(strategySelectionController.getView());

        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        setApplicationIcon(stage);
        stage.show();
    }

    private void setApplicationIcon(Stage stage) {
        InputStream stream = RefactorTool.class.getResourceAsStream("/icon/icon.png");
        stage.getIcons().add(new Image(stream));

    }
}
