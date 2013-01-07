package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.RemoveParameterDetector;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoveParameterSolver extends IssueSolver {
    private final static int METHODDECLARATION = 0;
    private final static int SINGLEVARAIABLEDECLARATION = 1;
    private final static int STARTMETHODINVOCATIONS = 2;

    private MethodDeclaration methodDeclaration;
    private SingleVariableDeclaration singleVariableDeclaration;
    private List<ASTNode> methodInvocations;

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof RemoveParameterDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {

        methodDeclaration = (MethodDeclaration) issue.getNodes().get(METHODDECLARATION);
        singleVariableDeclaration = (SingleVariableDeclaration) issue.getNodes().get(SINGLEVARAIABLEDECLARATION);

        /* Remove parameter from the method declaration */
        Delta methodDeclarationDelta = removeFromDeclaration();

        /* Remove the parameters from invocations */

        Solution solution = new Solution(issue, this, parameters);
        solution.getDeltas().add(methodDeclarationDelta);
        solution.getDeltas().addAll(removeFromInvocations(issue));

        return solution;
    }

    private Delta removeFromDeclaration() {
        ASTRewrite rewrite = ASTRewrite.create(methodDeclaration.getAST());
        rewrite.remove(singleVariableDeclaration, null);
        return createDelta(methodDeclaration, rewrite);
    }

    private List<Delta> removeFromInvocations(Issue issue) {
        List<Delta> deltas = new ArrayList<Delta>();

        if (issue.getNodes().size() > STARTMETHODINVOCATIONS) {
            methodInvocations = issue.getNodes().subList(STARTMETHODINVOCATIONS, issue.getNodes().size());

            for (ASTNode method : methodInvocations) {
                MethodInvocation methodInvocation = (MethodInvocation) method;
                Expression argument = (Expression) methodInvocation.arguments().get(methodDeclaration.parameters().indexOf(singleVariableDeclaration));
                ASTRewrite rewrite = ASTRewrite.create(method.getAST());
                rewrite.remove(argument, null);

                deltas.add(createDelta(methodInvocation, rewrite));
            }
        }

        return deltas;
    }
}
