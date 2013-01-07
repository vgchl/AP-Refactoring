package nl.han.ica.core.issue.solver.magicliteral;

import java.util.Map;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

public class LiteralSolutionBuilderFactory {

    public static LiteralSolutionBuilder getSolutionBuilder(Issue issue,
            IssueSolver issueSolver, Map<String, Parameter> parameters,
            final String parameterConstantName) {

        ASTNode node = issue.getNodes().get(0);

        if (node instanceof NumberLiteral) {
            return new NumberLiteralSolutionBuilder(issue, issueSolver,
                    parameters, parameterConstantName);
        } else if (node instanceof StringLiteral) {
            return new StringLiteralSolutionBuilder(issue, issueSolver,
                    parameters, parameterConstantName);
        } else if (node instanceof CharacterLiteral) {
            return new CharacterLiteralSolutionBuilder(issue, issueSolver,
                    parameters, parameterConstantName);
        }

        return null;

    }
}
