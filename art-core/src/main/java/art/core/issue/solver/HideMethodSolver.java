package art.core.issue.solver;

import art.core.SourceFile;
import art.core.issue.Issue;
import art.core.issue.IssueSolver;
import art.core.issue.detector.HideMethodDetector;
import art.core.solution.Delta;
import art.core.solution.Parameter;
import art.core.solution.Solution;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.IOException;
import java.util.Map;

/**
 * Solver for Hide Method.
 */
public class HideMethodSolver extends IssueSolver {

    private Map<String, Parameter> defaultParameters;

    public HideMethodSolver() {
        super();
    }

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof HideMethodDetector;
    }

    //TODO clean
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
        MethodDeclaration newMethodDeclaration = (MethodDeclaration) ASTNode.copySubtree(node.getRoot().getAST(), node);
        if(node instanceof MethodDeclaration){
            replaceModifierWithPrivate(newMethodDeclaration, node);
        }

        rewrite.replace(node, newMethodDeclaration, null);
        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());

        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            // Log
        }

        delta.setAfter(document.get());

        return solution;
    }
    
    private void replaceModifierWithPrivate(MethodDeclaration methodDeclaration, ASTNode oldDeclaration){
        int modifiers = methodDeclaration.getModifiers();
        int modifierLocation = getAnnotationsSize((MethodDeclaration)oldDeclaration);
        if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
            methodDeclaration.modifiers().remove(modifierLocation);

        }
        methodDeclaration.modifiers().addAll(modifierLocation, oldDeclaration.getAST().newModifiers(Modifier.PRIVATE));
    }

    private int getAnnotationsSize(MethodDeclaration declaration) {
        if (declaration.resolveBinding().getAnnotations() != null) {
            return declaration.resolveBinding().getAnnotations().length;
        }
        return 0;
    }
}
