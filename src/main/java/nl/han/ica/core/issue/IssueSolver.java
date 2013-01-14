package nl.han.ica.core.issue;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.util.FileUtil;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class IssueSolver {

    protected final Logger logger;
    private final Map<AST, ASTRewrite> rewrites;

    public IssueSolver() {
        logger = Logger.getLogger(getClass().getName());
        rewrites = new HashMap<>();
    }

    public abstract boolean canSolve(Issue issue);

    public Solution createSolution(Issue issue, Map<String, Parameter> parameters, Set<SourceFile> sourceFiles) {
        logger.info("Creating solution for issue " + issue);
        if (!canSolve(issue)) {
            throw new IllegalArgumentException("Cannot solve issue. This solver does not know how to solve that kind of issue.");
        }
        if (null == parameters) {
            parameters = new HashMap<>();
        }
        mergeDefaultParameters(parameters);
        return internalSolve(issue, parameters, sourceFiles);
    }

    protected abstract Solution internalSolve(Issue issue, Map<String, Parameter> parameters, Set<SourceFile> sourceFiles);

    public void applySolution(Solution solution) {
        logger.info("Applying solution " + solution);
        if (solution.getIssueSolver() != this) {
            throw new IllegalArgumentException("Cannot apply solution. The solution was made by a different solver.");
        }

        for (Delta delta : solution.getDeltas()) {
            try {
                FileUtil.setFileContent(delta.getFile(), delta.getAfter());
            } catch (IOException ex) {
                logger.fatal("Could not apply solution: error during file write. \n" + ex);
            }
        }
    }

    protected Map<String, Parameter> defaultParameters() {
        return new HashMap<>();
    }

    protected SourceFile retrieveSourceFile(ASTNode node) {
        return (SourceFile) node.getRoot().getProperty(SourceFile.SOURCE_FILE_PROPERTY);
    }

    private void mergeDefaultParameters(final Map<String, Parameter> parameters) {
        for (Map.Entry<String, Parameter> entry : defaultParameters().entrySet()) {
            if (!parameters.containsKey(entry.getKey())) {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }
    }

    protected Delta createDelta(ASTNode node, ASTRewrite rewrite) {
        SourceFile sourceFile = retrieveSourceFile(node);
        IDocument document = null;
        try {
            document = sourceFile.toDocument();
        } catch (IOException e) {
            logger.fatal("Could not read the source file.", e);
        }

        Delta delta = new Delta(sourceFile);
        delta.setBefore(document.get());

        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());
        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            logger.fatal("Could not rewrite the AST tree.", e);
        }

        delta.setAfter(document.get());
        return delta;
    }

    protected ASTRewrite rewriteFor(AST ast) {
        if (!rewrites.containsKey(ast)) {
            rewrites.put(ast, ASTRewrite.create(ast));
        }
        return rewrites.get(ast);
    }

    public Map<AST, ASTRewrite> getRewrites() {
        return rewrites;
    }
}
