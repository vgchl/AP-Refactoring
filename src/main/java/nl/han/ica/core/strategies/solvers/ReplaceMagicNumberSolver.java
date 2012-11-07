package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class ReplaceMagicNumberSolver extends StrategySolver {

    private static final String PARAMETER_CONSTANT_NAME = "Constant name";
    private Map<String, Parameter> defaultParameters;


    //TODO this constructor will replace the one with the ruleviolation as parameter
    public ReplaceMagicNumberSolver() {
        super();
        initializeDefaultParameters();
    }

    @Override
    public void rewriteAST() {
        if (violationNode instanceof NumberLiteral) {
            NumberLiteral literal = (NumberLiteral) violationNode;
            //CompilationUnit compilationUnit = (CompilationUnit) literal.getRoot();
            CompilationUnit compilationUnit = sourceFile.getCompilationUnit();
            TypeDeclaration typeDeclaration = (TypeDeclaration) compilationUnit.types().get(0);
            rewriteMagicNumber(typeDeclaration.getAST(), literal);

            FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
            typeDeclaration.accept(fieldDeclarationVisitor);
            if (!fieldDeclarationVisitor.hasFieldName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue())) { // TODO: Check for inherited constants.
                addStaticFinalField(typeDeclaration, literal.getToken());
            }

        }

    }


    /*@Override
    public void rewriteAST() {
        TypeDeclaration typeDeclaration = getTypeDeclaration(ruleViolation.getClassName());
        
        NumberLiteralVisitor numberLiteralVisitor = new NumberLiteralVisitor(compilationUnit);

        typeDeclaration.accept(numberLiteralVisitor);
        FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
        typeDeclaration.accept(fieldDeclarationVisitor);
        
        NumberLiteral literalViolation = numberLiteralVisitor.getLiteralViolation(ruleViolation.getBeginLine(),
                ruleViolation.getBeginColumn());
        
        setDefaultReplaceName(fieldDeclarationVisitor, literalViolation.getToken());
        
        rewriteMagicNumber(typeDeclaration.getAST(), literalViolation);
        
        if (!fieldDeclarationVisitor.hasFieldName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue())){
            addStaticFinalField(typeDeclaration, literalViolation.getToken());
        }
    }*/

    private void setDefaultReplaceName(FieldDeclarationVisitor fieldDeclarationVisitor, String violationValue) {
        List<FieldDeclaration> fieldDeclarations = fieldDeclarationVisitor.getFieldDeclarationWithValue(violationValue);
        if (!fieldDeclarations.isEmpty()) {
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclarations.get(0).fragments().get(0);
            defaultParameters.get(PARAMETER_CONSTANT_NAME).setValue(variableDeclaration.getName().toString());
        }
    }

    private void initializeDefaultParameters() {
        defaultParameters = new HashMap<>();
        Parameter constantName = new Parameter(PARAMETER_CONSTANT_NAME, "THAT_CONSTANT_NAME");
        constantName.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        });
        defaultParameters.put(constantName.getTitle(), constantName);
    }

    @Override
    public Map<String, Parameter> getDefaultParameters() {
        return defaultParameters;
    }

    /**
     * Rewrites the code so the magic number is replaced with an constant.
     *
     * @param ast           The AST to use when adding the constant to the code.
     * @param numberLiteral The constant.
     */
    private void rewriteMagicNumber(AST ast, NumberLiteral numberLiteral) {
        ASTRewrite rewrite = ASTRewrite.create(numberLiteral.getAST());
        SimpleName newSimpleName = ast.newSimpleName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue());
        rewrite.replace(numberLiteral, newSimpleName, null);
        applyChanges(rewrite);
    }

    private void addStaticFinalField(TypeDeclaration typeDeclaration, String fieldValue) {
        AST ast = typeDeclaration.getAST();
        ASTRewrite rewrite = ASTRewrite.create(ast);
        ListRewrite listRewrite = rewrite.getListRewrite(typeDeclaration,
                TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        FieldDeclaration fieldDeclaration = createNewFieldDeclaration(ast, fieldValue);
        listRewrite.insertFirst(fieldDeclaration, null);

        applyChanges(rewrite);
    }

    private FieldDeclaration createNewFieldDeclaration(AST ast, String fieldValue) {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue()));
        variableDeclarationFragment.setInitializer(ast.newNumberLiteral(fieldValue));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
        fieldDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL));
        return fieldDeclaration;
    }

}
