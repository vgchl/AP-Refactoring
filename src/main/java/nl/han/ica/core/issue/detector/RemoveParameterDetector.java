package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.MethodDeclarationVisitor;
import nl.han.ica.core.ast.visitors.MethodInvocationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class RemoveParameterDetector extends IssueDetector {
    private static final String STRATEGY_NAME = "Remove parameter";
    private static final String STRATEGY_DESCRIPTION = "Remove unused parameter from method.";
    List<MethodDeclaration> methodDeclarations = new ArrayList<>();
    List<MethodInvocation> methodInvocations = new ArrayList<>();
    List<FieldAccess> fieldAccessList = new ArrayList<>();

    @Override
    public void detectIssues() {
        reset();
        collectMethodDeclarations();
        collectMethodInvocations();
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            List<SingleVariableDeclaration> declaredVariables = methodDeclaration.parameters();
            List<MethodInvocation> invocationsForThisDeclaration = new ArrayList<>();
            if (declaredVariables != null) {
                System.out.println("Variables: " + methodDeclaration.parameters().toString());
                List<Statement> methodStatements = methodDeclaration.getBody().statements();
                String methodBlock = methodDeclaration.getBody().toString();
                //System.out.println("Method Statements: x"+methodStatements.toString());
                for (SingleVariableDeclaration variable : declaredVariables) {
                    //System.out.println("Binding: ");
                    //System.out.println("variable resolved bindings : " + variable.getName() + " " + variable);
                    if (!methodBlock.contains(variable.getName().toString())) {
                        //System.out.println("Found unused variable:" + variable.getName().toString());
                        Issue issue = createIssue();
                        issue.getNodes().add(methodDeclaration);
                        issue.getNodes().add(variable);
                        for (MethodInvocation methodInvocation : methodInvocations) {
                            if (methodDeclaration.resolveBinding().isEqualTo(methodInvocation.resolveMethodBinding())) {
                                issue.getNodes().add(methodInvocation);
                            }
                        }

                    }
                }
            }
        }
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
