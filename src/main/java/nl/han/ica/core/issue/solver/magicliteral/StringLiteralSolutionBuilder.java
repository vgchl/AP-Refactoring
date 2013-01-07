package nl.han.ica.core.issue.solver.magicliteral;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;

import java.util.Map;

public class StringLiteralSolutionBuilder extends LiteralSolutionBuilder {

    public StringLiteralSolutionBuilder(Issue issue, IssueSolver issueSolver,
            Map<String, Parameter> parameters, String parameterConstantName) {
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
