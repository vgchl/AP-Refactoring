package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import net.sourceforge.pmd.RuleViolation;

/**
 * Created with IntelliJ IDEA.
 * User: Corne
 * Date: 1-10-12
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class Strategy  {

    private CompilationUnit compilationUnit = null;

    public abstract void rewriteAST();

    public void buildAST(RuleViolation ruleViolation) {

        //compilationUnit = JavaParser.parse(ruleViolation.get)
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }
}
