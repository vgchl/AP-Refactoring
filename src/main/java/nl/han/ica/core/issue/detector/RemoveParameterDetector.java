package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.MethodDeclarationVisitor;
import nl.han.ica.core.ast.visitors.MethodInvocationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;

import nl.han.ica.core.util.ASTUtil;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class RemoveParameterDetector extends IssueDetector {

    private Logger log = Logger.getLogger(getClass().getName());

    private static final String STRATEGY_NAME = "Remove parameter";
    private static final String STRATEGY_DESCRIPTION = "Remove unused parameter from method.";

    List<MethodDeclaration> methodDeclarations;
    List<MethodInvocation> methodInvocations;
    List<FieldAccess> fieldAccessList;

    public RemoveParameterDetector() {
        methodDeclarations = new ArrayList<MethodDeclaration>();
        methodInvocations = new ArrayList<MethodInvocation>();
        fieldAccessList = new ArrayList<FieldAccess>();
    }

    @Override
    public void detectIssues() {
        reset();
        collectMethodDeclarations();
        collectMethodInvocations();

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

    private boolean usesVariable(MethodDeclaration methodDeclaration,
                                 SingleVariableDeclaration variableDeclaration) {
        String methodBlock = methodDeclaration.getBody().toString();

        return methodBlock.contains(variableDeclaration.getName().toString());
    }

    private boolean hasAnnotation(MethodDeclaration methodDeclaration) {
        return methodDeclaration.modifiers().get(0) instanceof Annotation;
    }

    private void collectMethodDeclarations() {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        for (CompilationUnit compilationUnit : compilationUnits) {
            compilationUnit.accept(visitor);
        }
        methodDeclarations = visitor.getMethodDeclarations();
    }

    private void collectMethodInvocations() {
        MethodInvocationVisitor visitor = new MethodInvocationVisitor();
        for (CompilationUnit compilationUnit : compilationUnits) {
            compilationUnit.accept(visitor);
        }
        methodInvocations = visitor.getMethodInvocations();
    }

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
