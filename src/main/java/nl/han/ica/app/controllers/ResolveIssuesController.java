package nl.han.ica.app.controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.app.CodeEditor;
import nl.han.ica.app.presenters.IssueViewModel;
import nl.han.ica.core.Job;
import nl.han.ica.core.strategies.solvers.ReplaceMagicNumberSolver;
import nl.han.ica.core.strategies.solvers.StrategySolverFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class ResolveIssuesController extends BaseController {

    private Job job;
    private ObservableList<IssueViewModel> issueList;
    private File file;

    @FXML
    protected TableView detectedIssuesTableView;
    @FXML
    protected AnchorPane ruleDetailDisplay;
    @FXML
    protected Label fileNameLabel;
    @FXML
    protected Label lineNumberLabel;
    @FXML
    protected Label issueNameLabel;
    @FXML
    protected Label issueDescriptionLabel;
    @FXML
    protected WebView stateBefore;
    @FXML
    protected WebView stateAfter;

    private CodeEditor editorBefore;
    private CodeEditor editorAfter;

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
        editorBefore = new CodeEditor(stateBefore);
        editorAfter = new CodeEditor(stateAfter);
    }

    protected void fillTableViewWithIssues() {

        issueList = FXCollections.observableList(new ArrayList<IssueViewModel>());

        ReportTree reportTree = job.getReport().getViolationTree();
        Iterator it = reportTree.iterator();
        while (it.hasNext()) {
            RuleViolation ruleViolation = (RuleViolation) it.next();

            IssueViewModel issueViewModel = new IssueViewModel();
            // TODO: Fix the below line so it chooses the correct file for the Issue. Can only handle single files now...
            issueViewModel.setFile(job.getFileByName(ruleViolation.getFilename()));
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
        final IssueViewModel issue = (IssueViewModel) detectedIssuesTableView.getSelectionModel().getSelectedItem();
        issueNameLabel.setText(issue.getIssueName());
        issueDescriptionLabel.setText(issue.getRuleViolation().getRule().getDescription().replace("\n", " ").replace("   ", ""));
        fileNameLabel.setText(issue.getRuleViolation().getFilename());
        lineNumberLabel.setText(issue.getRuleViolation().getBeginLine() + ":" + issue.getRuleViolation().getBeginColumn());

        ReplaceMagicNumberSolver replaceMagicNumberSolver = (ReplaceMagicNumberSolver) StrategySolverFactory.createStrategySolver(issue.getRuleViolation());
        replaceMagicNumberSolver.setRuleViolation(issue.getRuleViolation());
        replaceMagicNumberSolver.buildAST(issue.getFile());

        replaceMagicNumberSolver.setReplaceName("MAGICINT");
        replaceMagicNumberSolver.rewriteAST();

        RuleViolation violation = issue.getRuleViolation();
        editorBefore.setValue(readFile(issue.getFile()));
        editorBefore.highlightText(violation.getBeginLine(), violation.getBeginColumn(), violation.getEndLine(), violation.getEndColumn(), "change-before");
        editorAfter.setValue(replaceMagicNumberSolver.getDocument().get());

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), ruleDetailDisplay);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
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