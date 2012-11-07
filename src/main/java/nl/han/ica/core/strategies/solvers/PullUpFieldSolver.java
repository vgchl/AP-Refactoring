package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.Parameter;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 7-11-12
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class PullUpFieldSolver extends StrategySolver {

    private Map<String, Parameter> defaultParameters;
    private static final String PARAMETER_CONSTANT_NAME = "Field name";

    // TODO: which parameter? Issue?
    public PullUpFieldSolver() {
        // TODO get node for superclass field declarations, from Issue
        // TODO get nodes from subclasses field declarations, from Issue

        initializeDefaultParameters();
    }

    private void initializeDefaultParameters() {
        // TODO: use a default name for the new superfield
        defaultParameters = new HashMap<String, Parameter>();
        Parameter constantName = new Parameter(PARAMETER_CONSTANT_NAME, "newFieldFromSubclass");

        constantName.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        });

    }

    @Override
    public void rewriteAST() {
        // TODO check if default name is in the class
        // TODO remove fieldDeclarations from subclasses
        // TODO remove getters/setters from subclasses
        // TODO add fieldDeclaration of same type to superclass
        // TODO add getter and setter to superclass (if they existed in subclasses)
        addFieldToSuperClass();
    }

    /**
     *
     */
    private void addFieldToSuperClass() {

        // TODO get field that is present in subclasses and add it to the superclass
    }

    @Override
    public Map<String, Parameter> getDefaultParameters() {
        return defaultParameters;
    }
}
