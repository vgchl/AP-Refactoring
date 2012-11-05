package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.Parameter;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nl.han.ica.core.SourceHolder;
import org.eclipse.jface.text.Document;

public abstract class StrategySolver  {

    protected Logger logger;
    protected Map<String, Parameter> parameters;
    
    //protected List<SourceHolder> sourceHolders;
    protected SourceHolder sourceHolder;
    protected ASTNode violationNode;

    /**
     * Creates a strategy solver with rule violation.
     *
     * @param ruleViolation The rule violation.
     */
    public StrategySolver(){
        logger = Logger.getLogger(getClass().getName());
        parameters = getDefaultParameters();
    }

    public abstract void rewriteAST();
    
    
    /**
     * Get the type declaration in the file with a given name
     * @param name name of interface/class
     * @return TypeDeclaration with the matching name
     */
    /*protected TypeDeclaration getTypeDeclaration(String name){
        Iterator iter = compilationUnit.types().iterator();
        while (iter.hasNext()) {
            TypeDeclaration td = (TypeDeclaration) iter.next();
            if(td.getName().toString().equals(name)){
                return td;
            }
        }
        return null;
    }*/
    
    /**
     * Refresh the compilation unit,
     * use this method after you made some changes in the document
     */
    /*protected void refreshCompilationUnit(){
        astParser.setSource(document.get().toCharArray());
        compilationUnit = (CompilationUnit) astParser.createAST(null);
    }*/
    
    /**
     * Apply rewrite changes on the document
     * @param rewrite AstRewrite wich keeps the changes made in the ast.
     */
    protected void applyChanges(ASTRewrite rewrite){
        //TODO only applys changes on 1 doc
        TextEdit textEdit = rewrite.rewriteAST(sourceHolder.getDocument(), JavaCore.getOptions());
        
        try {
            textEdit.apply(sourceHolder.getDocument());
        } catch (MalformedTreeException | BadLocationException ex) {
            logger.error(StrategySolver.class.getName(), ex);
        }
    }

    public void setSourceHolder(SourceHolder sourceHolder) {
        this.sourceHolder = sourceHolder;
    }

    public void setViolationNodes(ASTNode violationNode) {
        this.violationNode = violationNode;
    }
    
    
    
    /**
     * Gets the parameters for the strategy solver.
     *
     * @return The parameters for this strategy solver.
     */
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters for the solver that are needed to perform the refactoring.
     *
     * @param parameters The parameters to set.
     */
    public void setParameters(Map<String, Parameter> parameters) {
        for (Map.Entry<String, Parameter> entry: getDefaultParameters().entrySet()) {
            if (! parameters.containsKey(entry.getKey())) {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }
        this.parameters = parameters;
    }

    /**
     * Gets the default parameters.
     *
     * @return The default parameters.
     */
    public Map<String, Parameter> getDefaultParameters() {
        return new HashMap<>();
    }

    /**
     * Get the document were changes will be applied on
     * 
     * @return docoument 
     */
    public IDocument getDocument() {
        return sourceHolder.getDocument();
    }
}
