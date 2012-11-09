package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.han.ica.core.ast.ASTHelper;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class ReplaceMagicNumberSolver extends StrategySolver {

    private static final String PARAMETER_CONSTANT_NAME = "Constant name";

    
    //TODO this constructor will replace the one with the ruleviolation as parameter
    public ReplaceMagicNumberSolver(){
        super();
        initializeDefaultParameters();
    }

    @Override
    public void rewriteAST() {  
        if(violationNode instanceof NumberLiteral){//should alwasys be true
            NumberLiteral literal = (NumberLiteral) violationNode;

            TypeDeclaration typeDeclaration = ASTHelper.getTypeDeclarationForNode(violationNode);

            FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();            
            typeDeclaration.accept(fieldDeclarationVisitor);
            
            boolean setDefaultReplaceName = setDefaultReplaceName(fieldDeclarationVisitor, violationNode.toString());
            
            rewriteMagicNumber(typeDeclaration.getAST(), literal);
            
            if (!fieldDeclarationVisitor.hasFieldName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue()) &&
                    !setDefaultReplaceName){
                addStaticFinalField(typeDeclaration, literal.getToken());
            }
        }
        
    }

    
    private boolean setDefaultReplaceName(FieldDeclarationVisitor fieldDeclarationVisitor, String violationValue){
        List<FieldDeclaration> fieldDeclarations = fieldDeclarationVisitor.getFieldDeclarationWithValue(violationValue);
        if (!fieldDeclarations.isEmpty()){
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclarations.get(0).fragments().get(0);
            parameters.get(PARAMETER_CONSTANT_NAME).setValue(variableDeclaration.getName().toString());
            return true;
        }
        return false;
    }

    private void initializeDefaultParameters() {
        parameters = new HashMap<>();
        Parameter constantName = new Parameter(PARAMETER_CONSTANT_NAME, "THAT_CONSTANT_NAME");
        constantName.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        });
        parameters.put(constantName.getTitle(), constantName);
    }

    @Override
    public Map<String, Parameter> getDefaultParameters() {
        return parameters;
    }

    /**
     * Rewrites the code so the magic number is replaced with an constant.
     *
     * @param ast The AST to use when adding the constant to the code.
     * @param numberLiteral The constant.
     */
    private void rewriteMagicNumber(AST ast, NumberLiteral numberLiteral){
        ASTRewrite rewrite = ASTRewrite.create(numberLiteral.getAST());
        SimpleName newSimpleName = ast.newSimpleName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue());
        rewrite.replace(numberLiteral, newSimpleName, null);
        applyChanges(rewrite);
    }
    
    private void addStaticFinalField(TypeDeclaration typeDeclaration, String fieldValue){        
        AST ast = typeDeclaration.getAST();
        ASTRewrite rewrite = ASTRewrite.create(ast);
        ListRewrite listRewrite = rewrite.getListRewrite(typeDeclaration,
                TypeDeclaration.BODY_DECLARATIONS_PROPERTY);        
                
        FieldDeclaration fieldDeclaration = createNewFieldDeclaration(ast, fieldValue);
        listRewrite.insertFirst(fieldDeclaration, null);
        
        applyChanges(rewrite);
    }    

    private FieldDeclaration createNewFieldDeclaration(AST ast, String fieldValue){
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName((String) parameters.get(PARAMETER_CONSTANT_NAME).getValue()));
        variableDeclarationFragment.setInitializer(ast.newNumberLiteral(fieldValue));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
        fieldDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL));
        return fieldDeclaration;
    }
    
}
