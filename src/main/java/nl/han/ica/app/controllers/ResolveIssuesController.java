package nl.han.ica.app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.app.presenters.IssueViewModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ResolveIssuesController extends BaseController {

    private File file;
    private ObservableList<IssueViewModel> issueList;

    @FXML
    protected TableView detectedIssuesTableView;
    @FXML
    protected TextArea beforeView;
    @FXML
    protected TextArea afterView;
    @FXML
    protected Label fileNameLabel;
    @FXML
    protected Label lineNumberLabel;
    @FXML
    protected Label issueNameLabel;

    @Override
    public Parent getView() {
        return buildView("/views/resolve_issues.fxml");
    }

    @FXML
    protected void fillTableViewWithIssues() throws IOException {

        issueList = FXCollections.observableList(new ArrayList<IssueViewModel>());
        try {
            PMD pmd = new PMD();

            file = new File("/Users/wouterkonecny/Documents/workspace/ap-refactoring/src/main/resources/UnusedCodeTester.java");
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
            while (it.hasNext()) {
                RuleViolation ruleViolation = (RuleViolation) it.next();

                IssueViewModel issueViewModel = new IssueViewModel();
                issueViewModel.setRuleViolation(ruleViolation);
                issueViewModel.setIssueName(ruleViolation.getDescription());
                issueList.add(issueViewModel);
            }

            detectedIssuesTableView.setItems(issueList);
            TableColumn<IssueViewModel,String> issueNameCol = new TableColumn<IssueViewModel,String>("Issue Type");
            issueNameCol.setCellValueFactory(new PropertyValueFactory("issueName"));
            issueNameCol.setPrefWidth(250);
            issueNameCol.setResizable(false);
            detectedIssuesTableView.getColumns().setAll(issueNameCol);

        } catch (PMDException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void showIssueDetails() throws IOException {
        IssueViewModel issue = (IssueViewModel) detectedIssuesTableView.getSelectionModel().getSelectedItem();
        issueNameLabel.setText(issue.getIssueName());
        fileNameLabel.setText(file.getName());
        lineNumberLabel.setText(issue.getRuleViolation().getBeginLine() + ":" + issue.getRuleViolation().getBeginColumn());
        beforeView.setText(readFile(file));
        afterView.setText(readFile(file));
        afterView.setText("<font color=red>GAAAY</font>");
    }


    private static String readFile(File f) throws IOException {
        String lineSep = System.getProperty("line.separator");
        BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
        String nextLine = "";
        StringBuffer sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null) {
            sb.append(nextLine);
            sb.append(lineSep);
        }
        return sb.toString();
    }
}