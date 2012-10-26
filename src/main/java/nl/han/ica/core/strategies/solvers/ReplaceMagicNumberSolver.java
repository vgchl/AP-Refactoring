package nl.han.ica.core.strategies.solvers;

import java.util.List;
import net.sourceforge.pmd.IRuleViolation;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.ast.visitors.NumberLiteralVisitor;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class ReplaceMagicNumberSolver extends StrategySolver {

    private String replaceName = "MAGIC";

    /**
     * The constructor for this solver.
     *
     * @param ruleViolation The rule violation.
     */
    public ReplaceMagicNumberSolver(IRuleViolation ruleViolation) {
        super(ruleViolation);
    }

    @Override
    public void rewriteAST() {
        NumberLiteralVisitor numberLiteralVisitor = new NumberLiteralVisitor(compilationUnit);       
        compilationUnit.accept(numberLiteralVisitor);
        
        FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
        compilationUnit.accept(fieldDeclarationVisitor);
        
        AST ast = compilationUnit.getAST();
        
        NumberLiteral literalViolation = numberLiteralVisitor.getLiteralViolation(ruleViolation.getBeginLine(),
                ruleViolation.getBeginColumn());
        
        setDefaultReplaceName(fieldDeclarationVisitor, literalViolation.getToken());
        
        rewriteMagicNumber(ast, literalViolation);
        
        if(!fieldDeclarationVisitor.hasFieldName(replaceName)){
            addStaticFinalField(ast, literalViolation.getToken());
        }
    }
    
    private void setDefaultReplaceName(FieldDeclarationVisitor fieldDeclarationVisitor, String violationValue){
        List<FieldDeclaration> fieldDeclarations = fieldDeclarationVisitor.getFieldDeclarationWithValue(violationValue);
        if(!fieldDeclarations.isEmpty()){
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclarations.get(0).fragments().get(0);
            replaceName = variableDeclaration.getName().toString();
        }
    }

    

    /**
     * Sets the replace name for the constant.
     *
     * @param replaceName The constant name.
     */
    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }

    /**
     * Rewrites the code so the magic number is replaced with an constant.
     *
     * @param ast The AST to use when adding the constant to the code.
     * @param numberLiteral The constant.
     */
    private void rewriteMagicNumber(AST ast, NumberLiteral numberLiteral){
        ASTRewrite rewrite = ASTRewrite.create(numberLiteral.getAST());
        SimpleName newSimpleName = ast.newSimpleName(replaceName);
        rewrite.replace(numberLiteral, newSimpleName, null);
        
        applyChanges(rewrite);
    }
    
    private void addStaticFinalField(AST ast, String fieldValue){        
        TypeDeclaration topLevelType = getTopLevelTypeDeclaration();
    
        ASTRewrite rewrite = ASTRewrite.create(topLevelType.getRoot().getAST());
        ListRewrite listRewrite = rewrite.getListRewrite(topLevelType,
                TypeDeclaration.BODY_DECLARATIONS_PROPERTY);        
                
        FieldDeclaration fieldDeclaration = createNewFieldDeclaration(ast, fieldValue);
        listRewrite.insertFirst(fieldDeclaration, null);
        
        applyChanges(rewrite);
    }    

    
    private FieldDeclaration createNewFieldDeclaration(AST ast, String fieldValue){
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName(replaceName));
        variableDeclarationFragment.setInitializer(ast.newNumberLiteral(fieldValue));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
        fieldDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL));
        
        return fieldDeclaration;
    }
    
}
