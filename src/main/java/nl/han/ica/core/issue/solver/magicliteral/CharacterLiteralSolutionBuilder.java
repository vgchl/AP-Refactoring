package nl.han.ica.core.issue.solver.magicliteral;

import java.util.Map;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;

public class CharacterLiteralSolutionBuilder extends LiteralSolutionBuilder {

	public CharacterLiteralSolutionBuilder(Issue issue,
			IssueSolver issueSolver, Map<String, Parameter> parameters,
			String parameterConstantName) {
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
