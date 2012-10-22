package nl.han.ica.core.strategies.solvers;


import net.sourceforge.pmd.RuleViolation;
import nl.han.ica.core.ast.FieldDeclarationVisitor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;


public class ReplaceMagicNumberSolver extends StrategySolver {

    private String replaceName = "MAGIC";

    public ReplaceMagicNumberSolver(RuleViolation ruleViolation) {
        super(ruleViolation);
    }

    @Override
    public void rewriteAST() {
        LiteralExprVisitor visitor = new LiteralExprVisitor(ruleViolation, compilationUnit);
        compilationUnit.accept(visitor);
    }

    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }
    
    private void addStaticFinalField(){
        
    }



    private class LiteralExprVisitor extends ASTVisitor  {

        private RuleViolation ruleViolation;
        private CompilationUnit compilationUnit;
        private NumberLiteral literalViolation;

        public LiteralExprVisitor(RuleViolation ruleViolation, CompilationUnit compilationUnit) {
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
