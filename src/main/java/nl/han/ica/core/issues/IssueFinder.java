/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.Context;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issues.criteria.IssueDetector;
import nl.han.ica.core.issues.criteria.MagicNumberIssueDetector;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 *
 * @author Corne
 */
public class IssueFinder {
    
    private Context context;
    private List<IssueDetector> issueDetectors = new ArrayList<>();
    
    
    public IssueFinder(Context context) {
        this.context = context;
    }

    public void setIssueDetectors(List<IssueDetector> issueDetectors) {
        this.issueDetectors = issueDetectors;
    }
    
    public List<Issue> findIssues(){
        List<Issue> issues = new ArrayList<>();
        //TODO instead of adding magicnumbercriteria add list off selected issueDetectors
        
        
        for(SourceFile sourceFile : context.getSourceHolders()){
            
            //REALLY UGLY BUT TEST FOR BUG FIX
            issueDetectors.add(new MagicNumberIssueDetector());
            sourceFile.getCompilationUnit().accept(issueDetectors.get(issueDetectors.size()-1));
            issues.addAll(createIssues(sourceFile, issueDetectors.get(issueDetectors.size()-1)));
        }

        return issues;
    }
    
    private List<Issue> createIssues(SourceFile sourceFile, IssueDetector issueDetector){
        List<Issue> issues = new ArrayList<>();
        
        List<ASTNode> nodes = issueDetector.getViolatedNodes();
        for(ASTNode node : nodes){
            Issue issue = new Issue(issueDetector.getStrategy(), node, sourceFile);
            issues.add(issue);
        }
        return issues;
    }

}
