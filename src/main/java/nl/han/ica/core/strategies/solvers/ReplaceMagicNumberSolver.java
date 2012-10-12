package nl.han.ica.core.strategies.solvers;

import japa.parser.ASTHelper;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import net.sourceforge.pmd.RuleViolation;
import nl.han.ica.core.ast.ASTStrategyHelper;
import nl.han.ica.core.ast.FieldDeclarationVisitor;


public class ReplaceMagicNumberSolver extends StrategySolver {

    private String replaceName = "MAGIC";

    public ReplaceMagicNumberSolver(RuleViolation ruleViolation) {
        super(ruleViolation);
    }

    @Override
    public void rewriteAST() {
        System.out.println(ruleViolation.toString());
        ReplaceMagicNumberLiteralExprVisitor expressionVisitor = new ReplaceMagicNumberLiteralExprVisitor();
        compilationUnit.accept(expressionVisitor, ruleViolation);

        IntegerLiteralExpr literalExpr = expressionVisitor.getIntegerLiteralViolationExpr();
        if (literalExpr != null) {
            addStaticFinalField(literalExpr.getValue());
            literalExpr.setValue(replaceName + literalExpr.getValue());
        }
    }

    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }

    private void addStaticFinalField(String magicNumber) {

        VariableDeclarator variableDeclarator = new VariableDeclarator(new VariableDeclaratorId(replaceName + magicNumber),
                new IntegerLiteralExpr(magicNumber));

        int modifier = ModifierSet.addModifier(ModifierSet.PRIVATE, ModifierSet.STATIC);
        modifier = ModifierSet.addModifier(modifier, ModifierSet.FINAL);

        BodyDeclaration fieldDeclaration = ASTHelper.createFieldDeclaration(modifier, ASTHelper.INT_TYPE, variableDeclarator);
        fieldDeclaration.setBeginLine(4);
        fieldDeclaration.setEndLine(5);

        FieldDeclarationVisitor fieldVisitor = new FieldDeclarationVisitor();
        compilationUnit.accept(fieldVisitor, null);
        ASTStrategyHelper.insertMember(compilationUnit.getTypes().get(0),
                fieldDeclaration, fieldVisitor.getNumberOfFields());
    }


    private static class ReplaceMagicNumberLiteralExprVisitor extends VoidVisitorAdapter {

        private IntegerLiteralExpr integerLiteralViolationExpr = null;

        @Override
        public void visit(IntegerLiteralExpr n, Object arg) {
            System.out.println("IntegerLiteral: " + n.getBeginLine() + " " + n.getBeginColumn());
            RuleViolation violation = (RuleViolation) arg;
            if (n.getBeginLine() == violation.getBeginLine() &&
                    n.getBeginColumn() == violation.getBeginColumn()) {
                integerLiteralViolationExpr = n;
            }
        }

        public IntegerLiteralExpr getIntegerLiteralViolationExpr() {
            return integerLiteralViolationExpr;
        }

    }
}
