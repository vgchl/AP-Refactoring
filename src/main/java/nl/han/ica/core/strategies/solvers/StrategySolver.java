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

    /**
     * Creates a strategy solver with rule violation.
     *
     * @param ruleViolation The rule violation.
     */
    public StrategySolver(IRuleViolation ruleViolation){
        logger = Logger.getLogger(getClass().getName());

        this.ruleViolation = ruleViolation;
    }

    /**
     * Creates a strategy solver with rule violation and parameters.
     *
     * @param ruleViolation The rule violation.
     * @param parameters The parameters that the solver needs to perform the refactoring.
     */
    public StrategySolver(IRuleViolation ruleViolation, Parameters parameters) {
        this(ruleViolation);
        this.parameters = parameters;
    }

    public abstract void rewriteAST();

    /**
     * Builds the AST.
     *
     * @param file File to build the AST from.
     */
    public void buildAST(File file) {

        try {
            compilationUnit = JavaParser.parse(file);
        } catch (ParseException | IOException e) {
            logger.error("Could not parse the Java File(s) to an AST.");
        }
    }

    /**
     * Gets the Compilation Unit
     *
     * @return The current compilation unit.
     */
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * Gets the Rule Violation for the solver.
     *
     * @return The rule violation interface.
     */
    public IRuleViolation getRuleViolation() {
        return ruleViolation;
    }

    /**
     * Sets the rule violation.
     *
     * @param ruleViolation The rule violation to set.
     */
    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

    /**
     * Gets the parameters for the strategy solver.
     *
     * @return The parameters for this strategy solver.
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters for the solver that are needed to perform the refactoring.
     *
     * @param parameters The parameters to set.
     */
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets the default parameters (empty).
     *
     * @return A empty parameter list.
     */
    public Parameters getDefaultParameters() {
        return new Parameters();
    }
}
