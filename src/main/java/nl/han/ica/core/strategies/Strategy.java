package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import net.sourceforge.pmd.RuleViolation;

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
