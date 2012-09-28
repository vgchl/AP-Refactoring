package nl.han.ica.app.controllers;

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
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import static org.apache.log4j.Logger.getLogger;

public class RefactorToolApp extends Application {
    private static final String APP_TITLE = "ART (Awesome Refactor Tool) - 0.1";
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 600;

    private Logger logger = getLogger(getClass().getName());

    private File file;
    private Label output = new Label("");
    
    public static void startApp( String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.setTitle(APP_TITLE);
        Group root = new Group();

        HBox hBox = new HBox();
        hBox.setMinSize(APP_WIDTH, APP_HEIGHT);
        hBox.setMaxSize(APP_WIDTH, APP_HEIGHT);
        Label label = new Label();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(getBrowseButton(label), label, output);
        Button analyzeButton = new Button("Analyze");
        analyzeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    output.setText(checkFile());
                } catch (FileNotFoundException e) {
                    logger.fatal("Cannot open File.");
                }
            }
        });

        hBox.getChildren().addAll(vBox, analyzeButton);

        root.getChildren().add(hBox);
        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    private Button getBrowseButton(final Label chosenFilePathLabel) {
        Button button = new Button();
        button.setText("Browse...");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                //Show open file dialog
                file = fileChooser.showOpenDialog(null);

                chosenFilePathLabel.setText(file.getPath());
                logger.info("Test");
            }
        });

        return button;
    }


    private String checkFile() throws FileNotFoundException {
        
        StringBuffer stringBuffer = new StringBuffer();
        try {
            PMD pmd = new PMD();

            InputStream inputStream = new FileInputStream(file);
            RuleSet ruleSet = new RuleSet();

            InputStream rs = pmd.getClassLoader().getResourceAsStream("rulesets/unusedcode.xml");
            InputStream rs2 = pmd.getClassLoader().getResourceAsStream("rulesets/naming.xml");

            RuleSetFactory ruleSetFactory = new RuleSetFactory();
            ruleSet.addRuleSet(ruleSetFactory.createRuleSet(rs, pmd.getClassLoader()));
            ruleSet.addRuleSet(ruleSetFactory.createRuleSet(rs2, pmd.getClassLoader()));

            RuleContext rc = new RuleContext();
            rc.setSourceCodeFilename(file.getAbsolutePath());

            pmd.processFile(inputStream, ruleSet, rc);

            ReportTree reportTree = rc.getReport().getViolationTree();
            Iterator it = reportTree.iterator();
            while(it.hasNext()) {
                RuleViolation ruleViolation = (RuleViolation) it.next();

                stringBuffer.append("Class name:\t" + ruleViolation.getClassName() + "\n");
                stringBuffer.append("Description:\t" + ruleViolation.getDescription() + "\n");
                stringBuffer.append("Line num:\t\t" + ruleViolation.getBeginLine() + "\n");
                stringBuffer.append("Column num:\t" + ruleViolation.getBeginColumn() + "\n");
                stringBuffer.append("End line num:\t" + ruleViolation.getEndLine() + "\n");
                stringBuffer.append("Var name:\t" + ruleViolation.getVariableName() + "\n");
                stringBuffer.append("\n");
            }

        } catch (PMDException e) {
            e.printStackTrace();
        }
        
        return stringBuffer.toString();
    }
}
