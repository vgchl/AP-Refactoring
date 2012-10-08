package nl.han.ica.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class StrategySelectionController extends BaseController {

    private File file;
    private Scene scene;
    @FXML
    private Label selectedFile;
    @FXML
    private Label selectedFilePath;
    @FXML
    private Button analyzeButton;

    public StrategySelectionController(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Parent getView() {
        return buildView("/views/strategy_selection.fxml");
    }

    @FXML
    protected void browse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Show open file dialog
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedFilePath.setText(file.getPath());
            selectedFilePath.setVisible(true);
            selectedFile.setVisible(true);
            analyzeButton.setDisable(false);
        }
    }

    @FXML
    protected void analyze(ActionEvent event) throws IOException {
        ResolveIssuesController resolveIssuesController = new ResolveIssuesController();
        scene.setRoot(resolveIssuesController.getView());

        event.consume();
    }

    @FXML
    protected void replMagNumClick(ActionEvent event) {
        System.out.println("Replace Magic Num with Symbolic Content Clicked.");
    }

}