package nl.han.ica.core.strategies.solvers;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import net.sourceforge.pmd.IRuleViolation;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public abstract class StrategySolver  {

    protected CompilationUnit compilationUnit;
    protected IRuleViolation ruleViolation;
    private Parameters parameters;
    private Logger logger;

    public StrategySolver(IRuleViolation ruleViolation){
        logger = Logger.getLogger(getClass().getName());

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
            logger.error("Could not parse the Java File(s) to an AST.");
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
