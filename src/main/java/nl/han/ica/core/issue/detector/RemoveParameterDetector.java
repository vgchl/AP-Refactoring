package nl.han.ica.core.issue.detector;

import nl.han.ica.core.Context;
import nl.han.ica.core.ast.visitors.MethodDeclarationVisitor;
import nl.han.ica.core.ast.visitors.MethodInvocationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.util.ASTUtil;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class RemoveParameterDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Remove parameter";
    private static final String STRATEGY_DESCRIPTION = "Remove unused parameter from method.";

    List<MethodDeclaration> methodDeclarations;
    List<MethodInvocation> methodInvocations;
    List<FieldAccess> fieldAccessList;

    public RemoveParameterDetector() {
        methodDeclarations = new ArrayList<>();
        methodInvocations = new ArrayList<>();
        fieldAccessList = new ArrayList<>();
    }

    @Override
    public void internalDetectIssues(Context context) {
        collectMethodDeclarations(context);
        //TODO REFACTOR, Because not needed to combine invocations already with his declarations
        collectMethodInvocations(context);

        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            if (!Modifier.isAbstract(methodDeclaration.getModifiers())
                    && !hasAnnotation(methodDeclaration)
                    && !ASTUtil.parent(TypeDeclaration.class, methodDeclaration).isInterface()
                    && !ASTUtil.isMainMethod(methodDeclaration)) {
                List<SingleVariableDeclaration> declaredVariables = methodDeclaration.parameters();

                if (declaredVariables != null) {
                    for (SingleVariableDeclaration variable : declaredVariables) {
                        if (!usesVariable(methodDeclaration, variable)) {
                            Issue issue = createIssue();
                            issue.getNodes().add(methodDeclaration);
                            issue.getNodes().add(variable);

                            for (MethodInvocation methodInvocation : methodInvocations) {
                                if (methodDeclaration.resolveBinding().isEqualTo(
                                        methodInvocation.resolveMethodBinding())) {
                                    issue.getNodes().add(methodInvocation);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private boolean usesVariable(MethodDeclaration methodDeclaration, SingleVariableDeclaration variableDeclaration) {
        String methodBlock = methodDeclaration.getBody().toString();

        return methodBlock.contains(variableDeclaration.getName().toString());
    }

    private boolean hasAnnotation(MethodDeclaration methodDeclaration) {
        return methodDeclaration.modifiers().get(0) instanceof Annotation;
    }

    private void collectMethodDeclarations(Context context) {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        context.accept(visitor);
        methodDeclarations = visitor.getMethodDeclarations();
    }

    private void collectMethodInvocations(Context context) {
        MethodInvocationVisitor visitor = new MethodInvocationVisitor();
        context.accept(visitor);
        methodInvocations = visitor.getMethodInvocations();
    }

    @Override
    public void reset() {
        methodDeclarations.clear();
        super.reset();
    }

    @Override
    public String getTitle() {
        return STRATEGY_NAME;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }
}
