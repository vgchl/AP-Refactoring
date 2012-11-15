package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.ASTHelper;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.MethodDeclarationVisitor;
import nl.han.ica.core.ast.visitors.MethodInvocationVisitor;
import org.eclipse.jdt.core.dom.*;

import java.util.*;

/**
 * @author: Wouter Konecny
 * @created: 12-11-12
 */
public class HideMethodDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Hide Method";
    public static final String STRATEGY_DESCRIPTION = "Hide method when it is not used by any other class.";

    private List<MethodDeclaration> methodDeclarationList;
    private List<MethodInvocation> methodInvocationList;

    public HideMethodDetector() {
        methodDeclarationList = new ArrayList<>();
        methodInvocationList = new ArrayList<>();
    }

    @Override
    public Set<Issue> detectIssues() {

        for (CompilationUnit compilationUnit : compilationUnits) {
            MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
            compilationUnit.accept(methodDeclarationVisitor);
            methodDeclarationList.addAll(methodDeclarationVisitor.getMethodDeclarations());

            MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
            compilationUnit.accept(methodInvocationVisitor);
            methodInvocationList.addAll(methodInvocationVisitor.getMethodInvocations());
        }

        findViolatedNodesAndCreateIssues();

        return issues;
    }

    /**
     * Finds all violated nodes and places them in the violatedNodes list.
     */
    private void findViolatedNodesAndCreateIssues() {

        outerloop:
        for (MethodDeclaration methodDeclaration  : methodDeclarationList) {

            int modifiers = methodDeclaration.getModifiers();

            for (MethodInvocation methodInvocation : methodInvocationList) {
//                System.out.println(methodDeclaration);
//                System.out.println(methodInvocation);
//                System.out.println("MD B: " + methodDeclaration.resolveBinding().equals(methodInvocation.resolveMethodBinding()));
//                System.out.println("Modif: " + !Modifier.isPrivate(modifiers));
//                System.out.println("ASTH: " + (ASTHelper.getTypeDeclarationForNode(methodDeclaration) != ASTHelper.getTypeDeclarationForNode(methodInvocation)));
//                System.out.println("------------");
                if(methodDeclaration.resolveBinding().equals(methodInvocation.resolveMethodBinding())
                        && !Modifier.isPrivate(modifiers)
                        && ASTHelper.getTypeDeclarationForNode(methodDeclaration) != ASTHelper.getTypeDeclarationForNode(methodInvocation)) {
//                    System.out.println(ASTHelper.getTypeDeclarationForNode(methodDeclaration));
//                    System.out.println(ASTHelper.getTypeDeclarationForNode(methodInvocation));
                    continue outerloop;
                }
            }

            if(!Modifier.isPrivate(modifiers) && !methodDeclaration.isConstructor()) {
                System.out.println("----> Found method that could be hidden: " + methodDeclaration);
                createIssue(methodDeclaration);
            }
        }
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