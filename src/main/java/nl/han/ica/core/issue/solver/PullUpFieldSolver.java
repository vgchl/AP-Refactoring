package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;
import nl.han.ica.core.util.ASTUtil;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.IOException;
import java.util.Map;

public class PullUpFieldSolver extends IssueSolver {

    private static final int VARIABLE_A_INDEX = 0;
    private static final int VARIABLE_B_INDEX = 1;
    private static final short SUPERTYPE_INDEX = 2;

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof PullUpFieldDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        VariableDeclarationFragment variableA = (VariableDeclarationFragment) issue.getNodes().get(VARIABLE_A_INDEX);
        VariableDeclarationFragment variableB = (VariableDeclarationFragment) issue.getNodes().get(VARIABLE_B_INDEX);
        TypeDeclaration supertype = (TypeDeclaration) issue.getNodes().get(SUPERTYPE_INDEX);

        ASTRewrite rewriteA = ASTRewrite.create(variableA.getAST());
        ASTRewrite rewriteB = ASTRewrite.create(variableB.getAST());
        ASTRewrite rewriteT = ASTRewrite.create(supertype.getAST());

        FieldDeclaration field = createSupertypeField(variableA, variableB, supertype, rewriteT);
        removeFieldFromSubtype(variableA, rewriteA);
        removeFieldFromSubtype(variableB, rewriteB);
        addFieldToSupertype(field, supertype, rewriteT);

        Solution solution = new Solution(issue, this, parameters);
        solution.getDeltas().add(createDelta(variableA, rewriteA));
        solution.getDeltas().add(createDelta(variableB, rewriteB));
        solution.getDeltas().add(createDelta(supertype, rewriteT));
        return solution;
    }

    private FieldDeclaration createSupertypeField(VariableDeclarationFragment variableA, VariableDeclarationFragment variableB, TypeDeclaration supertype, ASTRewrite rewrite) {
        VariableDeclarationFragment variable = (VariableDeclarationFragment) ASTNode.copySubtree(supertype.getAST(), variableA);
        FieldDeclaration field = supertype.getAST().newFieldDeclaration(variable);
        Type fieldAType = ASTUtil.parent(FieldDeclaration.class, variableA).getType();
        field.setType((Type) ASTNode.copySubtree(supertype.getAST(), fieldAType));

        ListRewrite listRewrite = rewrite.getListRewrite(field, FieldDeclaration.MODIFIERS2_PROPERTY);
        if (mustFieldBePublic(variableA, variableB)) {
            listRewrite.insertFirst(supertype.getAST().newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD), null);
        } else {
            listRewrite.insertFirst(supertype.getAST().newModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD), null);
        }

        return field;
    }

    private boolean mustFieldBePublic(VariableDeclarationFragment variableA, VariableDeclarationFragment variableB) {
        FieldDeclaration fieldA = ASTUtil.parent(FieldDeclaration.class, variableA);
        FieldDeclaration fieldB = ASTUtil.parent(FieldDeclaration.class, variableB);
        return Modifier.isPublic(fieldA.getModifiers()) || Modifier.isPublic(fieldB.getModifiers());
    }

    protected Delta createDelta(ASTNode node, ASTRewrite rewrite) { // TODO Generalize and pull up
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

    private void addFieldToSupertype(FieldDeclaration field, TypeDeclaration supertype, ASTRewrite rewrite) {
        ListRewrite bodyRewrite = rewrite.getListRewrite(supertype, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        bodyRewrite.insertFirst(field, null);
    }

    private void removeFieldFromSubtype(VariableDeclarationFragment variable, ASTRewrite rewrite) {
        FieldDeclaration field = ASTUtil.parent(FieldDeclaration.class, variable);
        if (field.fragments().size() > 1) {
            ListRewrite fragmentsRewrite = rewrite.getListRewrite(field, FieldDeclaration.FRAGMENTS_PROPERTY);
            fragmentsRewrite.remove(variable, null);
        } else {
            rewrite.remove(field, null);
        }
    }

}
