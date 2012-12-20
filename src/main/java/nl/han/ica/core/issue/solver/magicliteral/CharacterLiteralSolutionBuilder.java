package nl.han.ica.core.issue.solver.magicliteral;

import java.util.Map;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
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
	public Solution build() {
		String name = (String) parameters.get(parameterConstantName).getValue();
		CharacterLiteral charLiteral = (CharacterLiteral) literal;
		String value = charLiteral.getEscapedValue();

		if (!existingConstantExists(name)) {
			createConstant(name, value);
		}
		replaceMagicLiteralWithConstant(name);
		return buildSolution();
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
