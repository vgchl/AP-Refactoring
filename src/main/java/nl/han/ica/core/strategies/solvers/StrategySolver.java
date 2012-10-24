package nl.han.ica.core.strategies.solvers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import net.sourceforge.pmd.IRuleViolation;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public abstract class StrategySolver  {

    protected CompilationUnit compilationUnit;
    protected IRuleViolation ruleViolation;
    protected ASTParser astParser;
    protected IDocument document;
    private Parameters parameters;
    private Logger logger;

    /**
     * Creates a strategy solver with rule violation.
     *
     * @param ruleViolation The rule violation.
     */
    public StrategySolver(IRuleViolation ruleViolation){
        logger = Logger.getLogger(getClass().getName());

        this.ruleViolation = ruleViolation;
    }

    /**
     * Creates a strategy solver with rule violation and parameters.
     *
     * @param ruleViolation The rule violation.
     * @param parameters The parameters that the solver needs to perform the refactoring.
     */
    public StrategySolver(IRuleViolation ruleViolation, Parameters parameters) {
        this(ruleViolation);
        this.parameters = parameters;
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
     * Get the top level type declaration (class in the file)
     * @return TypeDeclaration off the first typedeclarion
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
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters for the solver that are needed to perform the refactoring.
     *
     * @param parameters The parameters to set.
     */
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets the default parameters (empty).
     *
     * @return A empty parameter list.
     */
    public Parameters getDefaultParameters() {
        return new Parameters();
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
