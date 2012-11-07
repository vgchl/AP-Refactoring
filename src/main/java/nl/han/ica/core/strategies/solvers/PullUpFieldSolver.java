package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.SourceHolder;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 7-11-12
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class PullUpFieldSolver extends StrategySolver {

    private static final String FIELD_NAME = "Field name";

    private List<SourceHolder> subclasses;

    // TODO: which parameter? Issue?
    public PullUpFieldSolver(SourceHolder superclass, List<SourceHolder> subclasses, Map<String, Parameter> parameters) {
        // TODO get node for superclass field declarations, from Issue
        // TODO get nodes from subclasses field declarations, from Issue

        setSourceHolder(superclass);
        this.subclasses = subclasses;

        if (parameters == null) {
            this.parameters = initializeDefaultParameters();
        } else {
            this.parameters = parameters;
        }
    }

    public PullUpFieldSolver(SourceHolder superclass, List<SourceHolder> subclasses) {
        this(superclass, subclasses, null);
    }



    private Map<String, Parameter> initializeDefaultParameters() {
        Map<String, Parameter> defaultParameters = new HashMap<String, Parameter>();
        Parameter constantName = new Parameter(FIELD_NAME, "newFieldFromSubclass");

        constantName.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        });

        return defaultParameters;
    }

    @Override
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

        /* TODO: Rewrites the document only, make sure document is superclass file?*/
        applyChanges(rewrite);
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
        variableDeclarationFragment.setName(ast.newSimpleName((String) parameters.get(FIELD_NAME).getValue()));

        // TODO: choose correct literal type from the thing that we're moving : use Enum in TypeDeclaration?
        variableDeclarationFragment.setInitializer(ast.newNumberLiteral());

        // TODO: choose correct literal type from the thing that we're moving
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);

        // TODO: choose correct Modifier: should basically always be private.
        fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
        fieldDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE));

        return fieldDeclaration;
    }
}
