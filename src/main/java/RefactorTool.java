import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;

import static org.apache.log4j.Logger.getLogger;

public class RefactorTool extends Application {


    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - 0.1";
    private static final int APP_WIDTH = 900;
    private static final int APP_HEIGHT = 700;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/strategy_selection.fxml"));

        stage.setResizable(false);
        stage.setTitle(APP_TITLE);

        stage.setScene(new Scene(root, APP_WIDTH, APP_HEIGHT));
        stage.show();
    }


}
