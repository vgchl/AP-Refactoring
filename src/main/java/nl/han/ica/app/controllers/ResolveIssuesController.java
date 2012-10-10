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
import javafx.scene.web.WebView;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.app.presenters.IssueViewModel;
import nl.han.ica.core.Job;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class ResolveIssuesController extends BaseController {

    private Job job;
    private ObservableList<IssueViewModel> issueList;
    private File file;

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
    @FXML
    protected WebView stateBefore;

    public ResolveIssuesController(Job job) {
        this.job = job;
        job.process();
    }

    @Override
    public Parent getView() {
        try {
            Parent view = buildView("/views/resolve_issues.fxml");
            fillTableViewWithIssues();
            initializeEditors();
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeEditors() {
        stateBefore.getEngine().load(getClass().getResource("/editor/editor.html").toExternalForm());
    }

    protected void fillTableViewWithIssues() {

        issueList = FXCollections.observableList(new ArrayList<IssueViewModel>());

        ReportTree reportTree = job.getReport().getViolationTree();
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
    }


    @FXML
    protected void showIssueDetails() throws IOException {
        IssueViewModel issue = (IssueViewModel) detectedIssuesTableView.getSelectionModel().getSelectedItem();
        issueNameLabel.setText(issue.getIssueName());
        fileNameLabel.setText(file.getName());
        lineNumberLabel.setText(issue.getRuleViolation().getBeginLine() + ":" + issue.getRuleViolation().getBeginColumn());
        beforeView.setText(readFile(file));
        afterView.setText(readFile(file));
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