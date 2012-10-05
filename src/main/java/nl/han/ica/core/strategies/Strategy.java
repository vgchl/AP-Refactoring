package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import net.sourceforge.pmd.RuleViolation;

import java.io.File;
import java.io.IOException;

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
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }
}
