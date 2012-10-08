package nl.han.ica.core.strategies;

import japa.parser.ASTHelper;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleViolation;

import java.io.InputStream;


public class ReplaceMagicNumber extends Strategy {

    private String replaceName = "MAGIC";

    public ReplaceMagicNumber() {
        super("Replace Magic Number with Symbolic Constant");

        InputStream rs = PMD.class.getClassLoader().getResourceAsStream("rulesets/controversial.xml");

        RuleSetFactory ruleSetFactory = new RuleSetFactory();
        setRuleSet(ruleSetFactory.createRuleSet(rs, PMD.class.getClassLoader()));

       // System.out.println(ruleViolation.getDescription());
    }

    @Override
    public void rewriteAST() {
        System.out.println(ruleViolation.toString());
        ReplaceMagicNumberMethodVisitor methodVisitor = new ReplaceMagicNumberMethodVisitor();
        methodVisitor.visit(compilationUnit, ruleViolation);
        IntegerLiteralExpr literalExpr = methodVisitor.getIntegerLiteralViolationExpr();
        if (literalExpr != null) {
            addStaticFinalField(literalExpr.getValue());
            literalExpr.setValue(replaceName + literalExpr.getValue());
        }
    }

    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }

    private void addStaticFinalField(String magicNumber) {
        PrimitiveType primitiveType = new PrimitiveType(PrimitiveType.Primitive.Int);

        VariableDeclarator variableDeclarator = new VariableDeclarator(new VariableDeclaratorId(replaceName + magicNumber),
                new IntegerLiteralExpr(magicNumber));

        int modifier = ModifierSet.addModifier(ModifierSet.PRIVATE, ModifierSet.STATIC);
        modifier = ModifierSet.addModifier(modifier, ModifierSet.FINAL);

        BodyDeclaration declaration = new FieldDeclaration(modifier, primitiveType, variableDeclarator);
        declaration.setBeginLine(4);
        declaration.setEndLine(4);

        ASTHelper.addMember(compilationUnit.getTypes().get(0), declaration);
    }

    private class ReplaceMagicNumberMethodVisitor extends VoidVisitorAdapter {

        private IntegerLiteralExpr integerLiteralViolationExpr = null;

        @Override
        public void visit(IntegerLiteralExpr n, Object arg) {
            System.out.println("IntegerLiteral: " + n.toString());
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
