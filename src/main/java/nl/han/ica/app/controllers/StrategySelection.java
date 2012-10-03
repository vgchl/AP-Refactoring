package nl.han.ica.app.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import static org.apache.log4j.Logger.getLogger;

public class StrategySelection {
    private File file;
    private Logger logger = getLogger(getClass().getName());

    @FXML
    private Label selectedFile;

    @FXML
    private Label selectedFilePath;

    @FXML
    private Button analyzeButton;

    @FXML
    protected void browse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Show open file dialog
        file = fileChooser.showOpenDialog(null);

        if(file != null) {
            selectedFilePath.setText(file.getPath());
            selectedFilePath.setVisible(true);
            selectedFile.setVisible(true);
            analyzeButton.setDisable(false);
        }
    }

    @FXML
    protected void analyze(ActionEvent event) throws FileNotFoundException {
        System.out.println(checkFile());
    }

    @FXML
    protected void replMagNumClick(ActionEvent event) {
        System.out.println("Replace Magic Num with Symbolic Content Clicked.");
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