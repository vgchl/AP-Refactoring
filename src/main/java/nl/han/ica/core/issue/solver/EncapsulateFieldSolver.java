package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.EncapsulateFieldDetector;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.IOException;
import java.util.Map;

public class EncapsulateFieldSolver extends IssueSolver {

    public EncapsulateFieldSolver() {
        super();
    }

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof EncapsulateFieldDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        ASTNode node = issue.getNodes().get(0);
        Solution solution = new Solution(issue, this, parameters);
        SourceFile sourceFile = (SourceFile) node.getRoot().getProperty(SourceFile.SOURCE_FILE_PROPERTY);

        IDocument document = null;
        try {
            document = sourceFile.toDocument();
        } catch (IOException e) {
            // Log
        }

        Delta delta = solution.createDelta(sourceFile);
        delta.setBefore(document.get());

        ASTRewrite rewrite = ASTRewrite.create(node.getAST());

        // maen's magic



        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());

        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            // Log
        }

        delta.setAfter(document.get());

        return solution;
    }
}
