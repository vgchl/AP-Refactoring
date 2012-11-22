package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.EncapsulateFieldDetector;
import nl.han.ica.core.util.ASTUtil;
import org.apache.commons.lang3.text.WordUtils;
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
        FieldDeclaration fieldDeclaration = (FieldDeclaration) ASTNode.copySubtree(node.getAST(), node);

        if (node instanceof FieldDeclaration) {
            int modifiers = fieldDeclaration.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                fieldDeclaration.modifiers().remove(0);
            }
            fieldDeclaration.modifiers().addAll(0, fieldDeclaration.getAST().newModifiers(Modifier.PRIVATE));
        }

        MethodDeclaration getter = createGetter(node.getAST(), fieldDeclaration);
//        createSetter();

        ListRewrite listRewrite = rewrite.getListRewrite(ASTUtil.parent(TypeDeclaration.class, node), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertLast(getter, null);

        rewrite.replace(node, fieldDeclaration, null);
        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());

        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            // Log
        }

        delta.setAfter(document.get());

        return solution;
    }

    private MethodDeclaration createGetter(AST ast, FieldDeclaration field) {
        MethodDeclaration method = ast.newMethodDeclaration();
        VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.fragments().get(0);
        String name = "get" + WordUtils.capitalize(fragment.getName().toString());

        method.setReturnType2((Type) ASTNode.copySubtree(ast, field.getType()));
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));

        Block getterBlock = ast.newBlock();

        ReturnStatement returnStatement = ast.newReturnStatement();

        Name expr2 = ast.newName(fragment.getName().toString());
        returnStatement.setExpression(expr2);


        getterBlock.statements().add(returnStatement);
        method.setBody(getterBlock);

        method.setName(ast.newSimpleName(name));

        return method;
    }

    private MethodDeclaration createSetter(AST ast, FieldDeclaration field) {
        MethodDeclaration method = ast.newMethodDeclaration();
        VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.fragments().get(0);
        String name = "set" + WordUtils.capitalize(fragment.getName().toString());

        method.setReturnType2((Type) ASTNode.copySubtree(ast, field.getType()));
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));

        Block getterBlock = ast.newBlock();

        ReturnStatement returnStatement = ast.newReturnStatement();

        FieldAccess expr2 = ast.newFieldAccess();
        expr2.setName(ast.newSimpleName(fragment.getName().toString()));
        returnStatement.setExpression(expr2);


        getterBlock.statements().add(returnStatement);
        method.setBody(getterBlock);

        method.setName(ast.newSimpleName(name));

        return method;
    }
}
