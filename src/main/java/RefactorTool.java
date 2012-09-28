import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.app.controllers.RefactorToolApp;
import org.apache.log4j.Logger;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import static org.apache.log4j.Logger.getLogger;

public class RefactorTool extends Application {


    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - 0.1";
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 600;

    public static void main(String[] args) throws PMDException, IOException  {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        stage.setTitle(APP_TITLE);

        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);

        stage.show();
    }


}
