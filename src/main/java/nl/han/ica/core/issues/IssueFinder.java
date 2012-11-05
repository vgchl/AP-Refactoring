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
        criterias.add(new MagicNumberCriteria(context.getSourceHolders().get(0)));
        
        for(CompilationUnit compilationUnit : context.getCompilationUnits()){
            //TODO only accepts 1 criteria atm
            compilationUnit.accept(criterias.get(0));
        }
        return criterias.get(0).getIssues();
    }

}
