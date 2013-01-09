package art.core.issue.solver.magicliteral;

import art.core.issue.Issue;
import art.core.issue.IssueSolver;
import art.core.solution.Parameter;
import org.eclipse.jdt.core.dom.*;

import java.util.Map;

public class CharacterLiteralSolutionBuilder extends LiteralSolutionBuilder {

    public CharacterLiteralSolutionBuilder(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters, String parameterConstantName) {
        super(issue, issueSolver, parameters, parameterConstantName);
    }

    @Override
    protected String getValueForConstant() {
        CharacterLiteral charLiteral = (CharacterLiteral) literal;
        return charLiteral.getEscapedValue();
    }

    @Override
    protected Expression getInitializerExpression(String value, AST ast) {
        CharacterLiteral newCharacterLiteral = ast.newCharacterLiteral();
        newCharacterLiteral.setEscapedValue(value);
        return newCharacterLiteral;
    }

    @Override
    protected Type getType(AST ast) {
        return ast.newPrimitiveType(PrimitiveType.CHAR);
    }
}
