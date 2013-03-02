/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


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
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncapsulateFieldSolver extends IssueSolver {

    private static final String PARAMETER_GETTER_NAME = "Getter name";
    private static final String PARAMETER_SETTER_NAME = "Setter name";
    private Solution solution;
    private MethodDeclaration getter;
    private MethodDeclaration setter;
    private Logger log = Logger.getLogger(getClass());

    public EncapsulateFieldSolver() {
        super();
    }

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof EncapsulateFieldDetector;
    }

    private void refactorNodes(List<ASTNode> nodes) throws InvalidParameterException {
        for(ASTNode node : nodes){
            if(node instanceof FieldDeclaration){
                refactorFieldDeclaration((FieldDeclaration) node);
            }else if(node instanceof QualifiedName){
                refactorQualifiedNames((QualifiedName) node);
            }else {
                throw new InvalidParameterException();
            }
        }
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        //ASTNode node = issue.getNodes().get(0);
        solution = new Solution(issue, this, parameters);
        refactorNodes(issue.getNodes());
//        log.info(issue.getNodes());
        return solution;
    }


    private SourceFile getSourceFileFromNode(ASTNode node){
        return (SourceFile) node.getRoot().getProperty(SourceFile.SOURCE_FILE_PROPERTY);
    }

    private IDocument getSourceFileDocument(SourceFile sourceFile){

        try {
            return sourceFile.toDocument();
        } catch (IOException e) {
            return null;
        }
    }

    private Delta createDelta(SourceFile sourceFile, IDocument documentBefore){
        Delta delta = solution.createDelta(sourceFile);
        delta.setBefore(documentBefore.get());
        return delta;
    }

    @SuppressWarnings("unchecked")
    private void refactorFieldDeclaration(FieldDeclaration fieldDeclaration){
        SourceFile sourceFile = getSourceFileFromNode(fieldDeclaration);
        IDocument document = getSourceFileDocument(sourceFile);
        Delta delta = createDelta(sourceFile, document);

        ASTRewrite rewrite = ASTRewrite.create(fieldDeclaration.getAST());

        FieldDeclaration fieldDeclarationCopy = (FieldDeclaration) ASTNode.copySubtree(fieldDeclaration.getAST(), fieldDeclaration);

        int annotationsSize = ASTUtil.getAnnotationsSize( ((VariableDeclarationFragment) fieldDeclaration.fragments().get(0)).resolveBinding() );

        int modifiers = fieldDeclaration.getModifiers();

        if (Modifier.isPublic(modifiers)) {
            fieldDeclarationCopy.modifiers().remove(annotationsSize);
        }

        fieldDeclarationCopy.modifiers().addAll(annotationsSize, fieldDeclaration.getAST().newModifiers(Modifier.PRIVATE));
        getter = createGetter(fieldDeclaration.getAST(), fieldDeclarationCopy, solution.getParameters().get(PARAMETER_GETTER_NAME));
        setter = createSetter(fieldDeclaration.getAST(), fieldDeclarationCopy, solution.getParameters().get(PARAMETER_SETTER_NAME));

        ListRewrite listRewrite = rewrite.getListRewrite(ASTUtil.parent(TypeDeclaration.class, fieldDeclaration), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertLast(getter, null);
        listRewrite.insertLast(setter, null);

        rewrite.replace(fieldDeclaration, fieldDeclarationCopy, null);
        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());

        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            log.fatal(e);
        }
        delta.setAfter(document.get());
    }

    @SuppressWarnings("unchecked")
    private void refactorQualifiedNames(QualifiedName qualifiedName){
        SourceFile sourceFile = getSourceFileFromNode(qualifiedName);
        IDocument document = getSourceFileDocument(sourceFile);
        Delta delta = createDelta(sourceFile, document);
        AST ast = qualifiedName.getAST();

        ASTRewrite rewrite = ASTRewrite.create(ast);
        MethodInvocation methodInvocation = ast.newMethodInvocation();
        methodInvocation.setExpression(ast.newSimpleName(qualifiedName.getQualifier().toString()));

        if(qualifiedName.getParent() instanceof Assignment && qualifiedName != ((Assignment)qualifiedName.getParent()).getRightHandSide()) {
            Assignment assignment = (Assignment) qualifiedName.getParent();
            methodInvocation.setName(ast.newSimpleName(setter.getName().toString()));
            methodInvocation.arguments().add( ASTNode.copySubtree(ast, assignment.getRightHandSide() ) );
            rewrite.replace(assignment, methodInvocation, null);
        }
        else {
            methodInvocation.setName(ast.newSimpleName(getter.getName().toString()));
            rewrite.replace(qualifiedName, methodInvocation , null);
        }

        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());

        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
           log.fatal(e);
        }
        delta.setAfter(document.get());
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration createGetter(AST ast, FieldDeclaration field, Parameter getterNameParameter) {
        VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.fragments().get(0);
        String fieldName = fragment.getName().toString();
        String getterName = (String) getterNameParameter.getValue();
        if (getterName.isEmpty()) {
            getterName = "get" + WordUtils.capitalize(fieldName);
            getterNameParameter.setValue(getterName);
        }

        MethodDeclaration method = ast.newMethodDeclaration();
        method.setReturnType2((Type) ASTNode.copySubtree(ast, field.getType()));
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));

        ReturnStatement returnStatement = ast.newReturnStatement();
        returnStatement.setExpression(ast.newName(fieldName));

        Block getterBlock = ast.newBlock();

        getterBlock.statements().add(returnStatement);
        method.setBody(getterBlock);
        method.setName(ast.newSimpleName(getterName));
        return method;
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration createSetter(AST ast, FieldDeclaration field, Parameter setterNameParameter) {
        MethodDeclaration method = ast.newMethodDeclaration();
        VariableDeclarationFragment fragment = (VariableDeclarationFragment) field.fragments().get(0);
        String fieldName = fragment.getName().toString();
        String setterName = (String) setterNameParameter.getValue();
        if (setterName.isEmpty()) {
            setterName = "set" + WordUtils.capitalize(fieldName);
            setterNameParameter.setValue(setterName);
        }

        method.setReturnType2(null);
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));

        SingleVariableDeclaration singleVariableDeclaration = ast.newSingleVariableDeclaration();
        singleVariableDeclaration.setType((Type) ASTNode.copySubtree(ast, field.getType()));
        singleVariableDeclaration.setName(ast.newSimpleName(fieldName));

        method.parameters().add(singleVariableDeclaration);
        Block setterBlock = ast.newBlock();

        Assignment assignment = ast.newAssignment();
        FieldAccess fieldAccess = ast.newFieldAccess();

        fieldAccess.setName(ast.newSimpleName(fieldName));
        fieldAccess.setExpression(ast.newThisExpression());
        assignment.setLeftHandSide(fieldAccess);
        assignment.setRightHandSide(ast.newSimpleName(fieldName));

        ExpressionStatement expressionStatement = ast.newExpressionStatement(assignment);

        setterBlock.statements().add(expressionStatement);
        method.setBody(setterBlock);

        method.setName(ast.newSimpleName(setterName));

        return method;
    }



    @Override
    protected Map<String, Parameter> defaultParameters() {
        Map<String, Parameter> parameters = new HashMap<>();
        Parameter getterName = new Parameter(PARAMETER_GETTER_NAME, "");
        Parameter setterName = new Parameter(PARAMETER_SETTER_NAME, "");

        getterName.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^(get|is|has)[A-Z][A-Za-z0-9]*(_[A-Za-z0-9]+)*$");
            }
        });

        setterName.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^(set)[A-Z][A-Za-z0-9]*(_[A-Za-z0-9]+)*$");
            }
        });

        parameters.put(getterName.getTitle(), getterName);
        parameters.put(setterName.getTitle(), setterName);
        return parameters;
    }
}
