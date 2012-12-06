package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.RemoveParameterDetector;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sjoerd van den Top
 * Date: 6-12-12
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public class RemoveParameterSolver extends IssueSolver {

    private final static int METHODDECLARATION = 0;
    private final static int SINGLEVARAIABLEDECLARATION = 1;
    private final static int STARTMETHODINVOCATIONS = 2;

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof RemoveParameterDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) issue.getNodes().get(METHODDECLARATION);
        SingleVariableDeclaration variable = (SingleVariableDeclaration) issue.getNodes().get(SINGLEVARAIABLEDECLARATION);
        List<ASTNode> methodInvocations = issue.getNodes().subList(STARTMETHODINVOCATIONS, issue.getNodes().size() - 1);

        ASTRewrite rewrite = ASTRewrite.create(methodDeclaration.getAST());

        rewrite.remove(variable, null);

        Delta delta = createDelta(methodDeclaration, rewrite);

        Solution solution = new Solution(issue, this, parameters);
        solution.getDeltas().add(delta);

        return solution;
    }
}
