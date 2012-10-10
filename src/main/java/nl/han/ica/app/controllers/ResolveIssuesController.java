package nl.han.ica.app.controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.dfa.report.ReportTree;
import nl.han.ica.app.presenters.IssueViewModel;
import nl.han.ica.core.Job;
import nl.han.ica.core.strategies.solvers.ReplaceMagicNumberSolver;
import nl.han.ica.core.strategies.solvers.StrategySolverFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ResolveIssuesController extends BaseController {

    private Job job;
    private ObservableList<IssueViewModel> issueList;

    @FXML
    protected TableView detectedIssuesTableView;
    @FXML
    protected AnchorPane ruleDetailDisplay;
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
    protected Label issueDescriptionLabel;
    @FXML
    protected Button applyRefactoringButton;

    public ResolveIssuesController(Job job) {
        this.job = job;
        job.process();
    }

    @Override
    public Parent getView() {
        try {
            Parent p = buildView("/views/resolve_issues.fxml");
            fillTableViewWithIssues();
            return p;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void fillTableViewWithIssues() {

        issueList = FXCollections.observableList(new ArrayList<IssueViewModel>());

        ReportTree reportTree = job.getReport().getViolationTree();
        Iterator it = reportTree.iterator();
        while (it.hasNext()) {
            RuleViolation ruleViolation = (RuleViolation) it.next();

            IssueViewModel issueViewModel = new IssueViewModel();
            // TODO: Fix the below line so it chooses the correct file for the Issue. Can only handle single files now...
            issueViewModel.setFile(job.getFiles().get(0));
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
        beforeView.setText(readFile(issue.getFile()));

        ReplaceMagicNumberSolver replaceMagicNumber = (ReplaceMagicNumberSolver) StrategySolverFactory.createStrategy(issue.getRuleViolation());
        replaceMagicNumber.setRuleViolation(issue.getRuleViolation());
        replaceMagicNumber.buildAST(issue.getFile());

        replaceMagicNumber.setReplaceName("MAGICINT");
        replaceMagicNumber.rewriteAST();

        afterView.setText(replaceMagicNumber.getCompilationUnit().toString());

//        applyRefactoringButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//                ReplaceMagicNumber replaceMagicNumber = (ReplaceMagicNumber) StrategyFactory.createStrategy(issue.getRuleViolation());
//                replaceMagicNumber.setRuleViolation(issue.getRuleViolation());
//                replaceMagicNumber.buildAST(issue.getFile());
//
//                replaceMagicNumber.setReplaceName("MAGICINT");
//                replaceMagicNumber.rewriteAST();
//
//                replaceMagicNumber.getCompilationUnit();
//            }
//        });

        FadeTransition fadeTransition
                = new FadeTransition(Duration.millis(500), ruleDetailDisplay);
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