package nl.han.ica.core.issue.solver.magicliteral;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import org.eclipse.jdt.core.dom.*;

import java.util.Map;

public class CharacterLiteralSolutionBuilder extends LiteralSolutionBuilder {

    public CharacterLiteralSolutionBuilder(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters, String parameterConstantName) {
        super(issue, issueSolver, parameters, parameterConstantName);
    }

    @Override
    protected String getValueForConstant() {
        return null;
    }

    @Override
    protected Expression getInitializerExpression(String value, AST ast) {
        return null;
    }

    @Override
    protected Type getType(AST ast) {
        return null;
    }

}
