package nl.han.ica.core.strategies.solvers;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;

import net.sourceforge.pmd.IRuleViolation;
import net.sourceforge.pmd.RuleViolation;

public abstract class StrategySolver  {

    protected CompilationUnit compilationUnit;
    protected IRuleViolation ruleViolation;
    private Parameters parameters;

    public StrategySolver(IRuleViolation ruleViolation){
        this.ruleViolation = ruleViolation;
    }

    public StrategySolver(IRuleViolation ruleViolation, Parameters parameters) {
        this(ruleViolation);
        this.parameters = parameters;
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

    public IRuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Parameters getDefaultParameters() {
        return new Parameters();
    }
}
