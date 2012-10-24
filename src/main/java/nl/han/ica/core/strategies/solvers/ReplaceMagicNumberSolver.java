package nl.han.ica.core.strategies.solvers;


import net.sourceforge.pmd.IRuleViolation;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class ReplaceMagicNumberSolver extends StrategySolver {

    private static final String PARAMETER_CONSTANT_NAME = "magicConstantName";

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
        LiteralExprVisitor visitor = new LiteralExprVisitor(ruleViolation, compilationUnit);
        compilationUnit.accept(visitor);
        AST ast = compilationUnit.getAST();
        
        rewriteMagicNumber(ast, visitor.getLiteralViolation());
        addStaticFinalField(ast, visitor.getLiteralViolation().getToken());
    }

    @Override
    public Parameters getDefaultParameters() {
        Parameters defaults = new Parameters();
        defaults.put(PARAMETER_CONSTANT_NAME, "THAT_CONSTANT_NAME");
        return defaults;
    }

    private void rewriteMagicNumber(AST ast, NumberLiteral numberLiteral){
        ASTRewrite rewrite = ASTRewrite.create(numberLiteral.getAST());
        SimpleName newSimpleName = ast.newSimpleName((String) parameters.get(PARAMETER_CONSTANT_NAME));
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
    
    private void applyChanges(ASTRewrite rewrite){
        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());
        
        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException ex) {
            Logger.getLogger(ReplaceMagicNumberSolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private FieldDeclaration createNewFieldDeclaration(AST ast, String fieldValue){
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName((String) parameters.get(PARAMETER_CONSTANT_NAME)));
        variableDeclarationFragment.setInitializer(ast.newNumberLiteral(fieldValue));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
        fieldDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL));
        
        return fieldDeclaration;
    }


    private class LiteralExprVisitor extends ASTVisitor  {

        private IRuleViolation ruleViolation;
        private CompilationUnit compilationUnit;
        private NumberLiteral literalViolation;

        public LiteralExprVisitor(IRuleViolation ruleViolation, CompilationUnit compilationUnit) {
            this.ruleViolation = ruleViolation;
            this.compilationUnit = compilationUnit;
        }
        
        @Override
        public boolean visit(NumberLiteral node) {
            if(compilationUnit.getColumnNumber(node.getStartPosition()) == ruleViolation.getBeginColumn()-1 &&
                    compilationUnit.getLineNumber(node.getStartPosition()) == ruleViolation.getBeginLine()){
                literalViolation = node;
            }
            return super.visit(node);
        }

        public NumberLiteral getLiteralViolation() {
            return literalViolation;
        }
        
    }
}
