package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import net.sourceforge.pmd.RuleViolation;

import java.io.File;
import java.io.IOException;

public abstract class Strategy  {

    private String name = null;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
