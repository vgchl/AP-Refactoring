package nl.han.ica.core.strategies;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleViolation;

public abstract class Strategy  {

    private String name = null;
    private RuleSet ruleSet;

    protected CompilationUnit compilationUnit = null;
    protected RuleViolation ruleViolation = null;

    public Strategy(String name){
        this.name = name;
    }

    public Strategy(String name, RuleViolation ruleViolation){
        this.ruleViolation = ruleViolation;
        this.name = name;
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

    public RuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public void setRuleViolation(RuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }
}
