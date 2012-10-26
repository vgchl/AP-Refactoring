package nl.han.ica.core.strategies.solvers;

import net.sourceforge.pmd.IRuleViolation;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

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

        String existingFile = getExistingFileContents(file);
        document = new Document(existingFile);

        astParser.setSource(existingFile.toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);

        Map options = JavaCore.getOptions();
        astParser.setCompilerOptions(options);
        compilationUnit = (CompilationUnit) astParser.createAST(null);
        compilationUnit.recordModifications();

    }
    
    private String getExistingFileContents(File file) { // TODO: Replace by FileUtil.getFileContents(File)
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
    
    protected void refreshCompilationUnit(){
        astParser.setSource(document.get().toCharArray());
        compilationUnit = (CompilationUnit) astParser.createAST(null);
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

    public IDocument getDocument() {
        return document;
    }
}
