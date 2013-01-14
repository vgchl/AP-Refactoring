package nl.han.ica.core.ast.builders;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jdt.core.dom.*;

public class AccessorBuilder {

    private FieldDeclaration field;
    private VariableDeclarationFragment variable;
    private AST ast;

    public AccessorBuilder(final FieldDeclaration field, final VariableDeclarationFragment variable) {
        this.field = field;
        this.variable = variable;
        this.ast = field.getAST();
    }

    @SuppressWarnings("unchecked")
    public MethodDeclaration buildGetter() {
        MethodDeclaration method = ast.newMethodDeclaration();
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
        method.setReturnType2(copyVariableType());
        method.setName(ast.newSimpleName(getterMethodName()));

        ReturnStatement returnStatement = ast.newReturnStatement();
        returnStatement.setExpression(copyVariableName());

        Block body = ast.newBlock();
        body.statements().add(returnStatement);
        method.setBody(body);

        return method;
    }

    @SuppressWarnings("unchecked")
    public MethodDeclaration buildSetter() {
        MethodDeclaration method = ast.newMethodDeclaration();
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
        method.setName(ast.newSimpleName(setterMethodName()));

        SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
        parameter.setType(copyVariableType());
        parameter.setName(copyVariableName());
        method.parameters().add(parameter);

        FieldAccess fieldAccess = ast.newFieldAccess();
        fieldAccess.setExpression(ast.newThisExpression());
        fieldAccess.setName(copyVariableName());

        Assignment assignment = ast.newAssignment();
        assignment.setLeftHandSide(fieldAccess);
        assignment.setRightHandSide(copyVariableName());

        Block body = ast.newBlock();
        body.statements().add(ast.newExpressionStatement(assignment));
        method.setBody(body);

        return method;
    }

    @SuppressWarnings("unchecked")
    public MethodDeclaration buildCollectionAdder() {
        MethodDeclaration method = ast.newMethodDeclaration();
        method.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
        method.setName(ast.newSimpleName(collectionAdderMethodName()));

        SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
        Type type = copyVariableType();
        if (!(type instanceof ParameterizedType)) {
            throw new UnsupportedOperationException();
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type itemType = (Type) parameterizedType.typeArguments().get(0);
        parameter.setType(itemType);
        parameter.setName(copyVariableName());
        method.parameters().add(parameter);

        FieldAccess fieldAccess = ast.newFieldAccess();
        fieldAccess.setExpression(ast.newThisExpression());
        fieldAccess.setName(copyVariableName());

        Assignment assignment = ast.newAssignment();
        assignment.setLeftHandSide(fieldAccess);
        assignment.setRightHandSide(copyVariableName());

        MethodInvocation invocation = ast.newMethodInvocation();
        invocation.setExpression(fieldAccess);
        invocation.setName(ast.newSimpleName("add"));
        invocation.arguments().add(copyVariableName());

        Block body = ast.newBlock();
        body.statements().add(ast.newExpressionStatement(assignment));
        method.setBody(body);

        return method;
    }

    private String getterMethodName() {
        return "get" + WordUtils.capitalize(variable.getName().getIdentifier());
    }

    private String setterMethodName() {
        return "set" + WordUtils.capitalize(variable.getName().getIdentifier());
    }

    private String collectionAdderMethodName() {
        return "add" + WordUtils.capitalize(variable.getName().getIdentifier());
    }

    private Type copyVariableType() {
        return (Type) ASTNode.copySubtree(ast, field.getType());
    }

    private SimpleName copyVariableName() {
        return (SimpleName) ASTNode.copySubtree(ast, variable.getName());
    }

}
