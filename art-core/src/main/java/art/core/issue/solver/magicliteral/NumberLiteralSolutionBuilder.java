package art.core.issue.solver.magicliteral;

import art.core.issue.Issue;
import art.core.issue.IssueSolver;
import art.core.solution.Parameter;
import org.eclipse.jdt.core.dom.*;

import java.util.Map;

public class NumberLiteralSolutionBuilder extends LiteralSolutionBuilder {

    public NumberLiteralSolutionBuilder(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters, String parameterConstantName) {
        super(issue, issueSolver, parameters, parameterConstantName);
    }

    @Override
    protected String getValueForConstant() {
        NumberLiteral numberLiteral = (NumberLiteral) literal;
        return numberLiteral.getToken();
    }

    @Override
    protected Type getType(AST ast) {
        NumberLiteral numberLiteral = (NumberLiteral) literal;
        String token = numberLiteral.getToken();

        if (token.contains("f")) {
            return ast.newPrimitiveType(PrimitiveType.FLOAT);
        } else if (token.contains(".")) {
            return ast.newPrimitiveType(PrimitiveType.DOUBLE);
        } else if (token.contains("L")) {
            return ast.newPrimitiveType(PrimitiveType.LONG);
        } else {
            return ast.newPrimitiveType(PrimitiveType.INT);
        }
    }

    @Override
    protected Expression getInitializerExpression(String value, AST ast) {
        return ast.newNumberLiteral(value);
    }

}
