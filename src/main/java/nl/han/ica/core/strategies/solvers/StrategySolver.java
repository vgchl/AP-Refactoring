package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.util.FileUtil;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class StrategySolver {

    protected Logger logger;
    protected IDocument document;
    protected Map<String, Parameter> parameters;
    protected SourceFile sourceFile;
    protected ASTNode violationNode;

    /**
     * Creates a strategy solver with rule violation.
     *
     * @param ruleViolation The rule violation.
     */
    public StrategySolver() {
        logger = Logger.getLogger(getClass().getName());
        parameters = getDefaultParameters();
    }

    public abstract void rewriteAST();

    /**
     * Apply rewrite changes on the document
     *
     * @param rewrite AstRewrite wich keeps the changes made in the ast.
     */
    protected void applyChanges(ASTRewrite rewrite) {
        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());
        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException ex) {
            logger.error(StrategySolver.class.getName(), ex);
        }
    }

    public void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
        setDocument(sourceFile.getFile());
    }

    private void setDocument(File file) {
        try {
            String fileContent = FileUtil.getFileContent(file);
            document = new Document(fileContent);
            document.get()
        } catch (IOException ex) {
            logger.error(StrategySolver.class.getName(), ex);
        }
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
        for (Map.Entry<String, Parameter> entry : getDefaultParameters().entrySet()) {
            if (!parameters.containsKey(entry.getKey())) {
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
