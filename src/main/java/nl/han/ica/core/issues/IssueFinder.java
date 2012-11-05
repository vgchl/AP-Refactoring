/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issues;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.Context;
import nl.han.ica.core.Issue;
import nl.han.ica.core.SourceHolder;
import nl.han.ica.core.issues.criteria.Criteria;
import nl.han.ica.core.issues.criteria.MagicNumberCriteria;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 *
 * @author Corne
 */
public class IssueFinder {
    
    private Context context;
    private List<Criteria> criterias = new ArrayList<>();
    
    
    public IssueFinder(Context context) {
        this.context = context;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }
    
    public List<Issue> findIssues(){
        List<Issue> issues = new ArrayList<>();
        //TODO instead of adding magicnumbercriteria add list off selected criterias
        
        
        for(SourceHolder sourceHolder : context.getSourceHolders()){
            
            //REALLY UGLY BUT TEST FOR BUG FIX
            criterias.add(new MagicNumberCriteria());
            sourceHolder.getCompilationUnit().accept(criterias.get(criterias.size()-1));
            issues.addAll(createIssues(sourceHolder, criterias.get(criterias.size()-1)));
        }

        return issues;
    }
    
    private List<Issue> createIssues(SourceHolder sourceHolder, Criteria criteria){
        List<Issue> issues = new ArrayList<>();
        
        List<ASTNode> nodes = criteria.getViolatedNodes();
        for(ASTNode node : nodes){
            Issue issue = new Issue(criteria.getStrategy(), node, sourceHolder);
            issues.add(issue);
        }
        return issues;
    }

}
