package nl.han.ica.core.strategies;

import net.sourceforge.pmd.RuleViolation;
import org.eclipse.jdt.core.dom.AST;

public abstract class Strategy  {

    private AST ast = null;

    public abstract void rewriteAST(AST ast);

    public void buildAST(RuleViolation ruleViolation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public AST getAst() {
        return ast;
    }
}
