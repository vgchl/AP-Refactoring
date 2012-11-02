package nl.han.ica.core.strategies.solvers;

import net.sourceforge.pmd.IRuleViolation;
import nl.han.ica.core.Parameter;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class StrategySolver  {

    protected CompilationUnit compilationUnit;
    protected IRuleViolation ruleViolation;
    protected ASTParser astParser;
    protected IDocument document;
    protected Logger logger;
    protected Map<String, Parameter> parameters;

    /**
     * Creates a strategy solver with rule violation.
     *
     * @param ruleViolation The rule violation.
     */
    public StrategySolver(final IRuleViolation ruleViolation){
        logger = Logger.getLogger(getClass().getName());
        this.ruleViolation = ruleViolation;

        parameters = getDefaultParameters();
    }

    public abstract void rewriteAST();

    /**
     * Builds the AST.
     *
     * @param file File to build the AST from.
     */
    public void buildAST(File file) {
        astParser = ASTParser.newParser(AST.JLS3);

        String existingFile = getFileContents(file);
        document = new Document(existingFile);

        astParser.setSource(existingFile.toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);

        Map options = JavaCore.getOptions();
        astParser.setCompilerOptions(options);
        compilationUnit = (CompilationUnit) astParser.createAST(null);
        compilationUnit.recordModifications();

    }
    
    private String getFileContents(File file) {        
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            char[] buffer = new char[1024];
            int returnedBytes = br.read(buffer);
            while (returnedBytes != -1) {
                sb.append(buffer, 0, returnedBytes);
                returnedBytes = br.read(buffer);
            }
        return sb.toString();
        } catch (IOException ex) {
            logger.error(StrategySolver.class.getName(), ex);
        }
        return null;
    }
    
    /**
     * Get the top level type declaration (public class/interface in the file)
     * @return TypeDeclaration top level typedeclaration
     */
    protected TypeDeclaration getTopLevelTypeDeclaration(){
        Iterator iter = compilationUnit.types().iterator();
        while (iter.hasNext()) {
            TypeDeclaration td = (TypeDeclaration) iter.next();
            if (td.getParent().equals(compilationUnit)
                    && (td.getModifiers() & Modifier.PUBLIC) > 0) {
                return td;
            }
        }
        return null;      
    }
    
    /**
     * Get the type declaration in the file with a given name
     * @param name name of interface/class
     * @return TypeDeclaration with the matching name
     */
    protected TypeDeclaration getTypeDeclaration(String name){
        Iterator iter = compilationUnit.types().iterator();
        while (iter.hasNext()) {
            TypeDeclaration td = (TypeDeclaration) iter.next();
            if(td.getName().toString().equals(name)){
                return td;
            }
        }
        return null;
    }
    
    /**
     * Refresh the compilation unit,
     * use this method after you made some changes in the document
     */
    protected void refreshCompilationUnit(){
        astParser.setSource(document.get().toCharArray());
        compilationUnit = (CompilationUnit) astParser.createAST(null);
    }
    
    /**
     * Apply rewrite changes on the document
     * @param rewrite AstRewrite wich keeps the changes made in the ast.
     */
    protected void applyChanges(ASTRewrite rewrite){
        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());        
        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException ex) {
            logger.error(StrategySolver.class.getName(), ex);
        }
    }

    /**
     * Gets the Compilation Unit
     *
     * @return The current compilation unit.
     */
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * Gets the Rule Violation for the solver.
     *
     * @return The rule violation interface.
     */
    public IRuleViolation getRuleViolation() {
        return ruleViolation;
    }

    /**
     * Sets the rule violation.
     *
     * @param ruleViolation The rule violation to set.
     */
    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
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
        return document;
    }
}
