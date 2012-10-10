package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;
import net.sourceforge.pmd.RuleViolation;

public abstract class Strategy  {

    protected CompilationUnit compilationUnit = null;
    protected RuleViolation ruleViolation = null;

    public Strategy(RuleViolation ruleViolation){
        this.ruleViolation = ruleViolation;
    }

    public abstract void rewriteAST();

    public void buildAST(File file) {

        try {
            compilationUnit = JavaParser.parse(file);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }
}
