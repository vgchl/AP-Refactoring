package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PullUpFieldSolver extends IssueSolver {

    private static final String FIELD_NAME = "Field name";

    private List<SourceFile> subclasses;

    public PullUpFieldSolver() {
        // TODO get node for superclass field declarations, from Issue
        // TODO get nodes from subclasses field declarations, from Issue

    }

    public PullUpFieldSolver(SourceFile superclass, List<SourceFile> subclasses) {
        //this(superclass, subclasses, null);
    }

    public void rewriteAST() {
        // TODO check if default name is in the class
        // TODO remove fieldDeclarations from subclasses
        // TODO remove getters/setters from subclasses
        // TODO add fieldDeclaration of same type to superclass
        // TODO add getter and setter to superclass (if they existed in subclasses)
        //addFieldToSuperClass();
        addGetterAndSetter();
        removeFieldsFromSubClasses();
        removeGettersAndSettersFromSubClasses();
    }

    private void removeGettersAndSettersFromSubClasses() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void removeFieldsFromSubClasses() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void addGetterAndSetter() {
        // TODO: ALL
    }

    /**
     *
     */
    private void addFieldToSuperClass(TypeDeclaration typeDeclaration, String fieldValue) {

        // TODO get field that is present in subclasses and add it to the superclass
        AST ast = typeDeclaration.getAST();
        ASTRewrite rewrite = ASTRewrite.create(ast);
        ListRewrite listRewrite = rewrite.getListRewrite(typeDeclaration, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        FieldDeclaration newField = createNewFieldDeclaration(ast, fieldValue);
        listRewrite.insertLast(newField, null);
    }

    /**
     * Create a new FieldDeclaration based on the duplicates found in subclasses. Copies
     *
     * @param ast
     * @param fieldValue
     * @return
     */
    private FieldDeclaration createNewFieldDeclaration(AST ast, String fieldValue) {

        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName((String) defaultParameters.get(FIELD_NAME).getValue())); // parameters.get(FIELD_NAME).getValue()));

        // TODO: choose correct literal type from the thing that we're moving : use Enum in TypeDeclaration?
        variableDeclarationFragment.setInitializer(ast.newNumberLiteral());

        // TODO: choose correct literal type from the thing that we're moving
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);

        // TODO: choose correct Modifier: should basically always be private.
        fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
        fieldDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE));

        return fieldDeclaration;
    }

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof PullUpFieldDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
