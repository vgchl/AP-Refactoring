package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.MethodDeclarationVisitor;
import nl.han.ica.core.ast.visitors.MethodInvocationVisitor;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.util.ASTUtil;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author: Wouter Konecny
 * @created: 12-11-12
 */
public class HideMethodDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Hide Method";
    private static final String STRATEGY_DESCRIPTION = "Hide method when it is not used by any other class.";

    private List<MethodDeclaration> methodDeclarationList;
    private List<MethodInvocation> methodInvocationList;
    private Map<MethodDeclaration, List<MethodInvocation>> methodUsages;

    public HideMethodDetector() {
        methodDeclarationList = new ArrayList<>();
        methodInvocationList = new ArrayList<>();
        methodUsages = new WeakHashMap<>();
    }

    @Override
    public void detectIssues() {
        for (CompilationUnit compilationUnit : compilationUnits) {
            //System.out.println("COMPILATIONUNIT: " + compilationUnit);
            MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
            compilationUnit.accept(methodDeclarationVisitor);

            for (MethodDeclaration methodDeclaration : methodDeclarationVisitor.getMethodDeclarations()) {
                methodUsages.put(methodDeclaration, new ArrayList<MethodInvocation>());
            }

            MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
            compilationUnit.accept(methodInvocationVisitor);
            methodInvocationList.addAll(methodInvocationVisitor.getMethodInvocations());
        }

        buildHashMapWithMethodDeclarationsAndInvocations();
        findViolatedNodesAndCreateIssues();
    }

    /**
     * Finds all violated nodes and places them in the violatedNodes list.
     */
    private void findViolatedNodesAndCreateIssues() {

        outerloop:
        for (Map.Entry<MethodDeclaration, List<MethodInvocation>> entry : methodUsages.entrySet()) {
            MethodDeclaration methodDeclaration = entry.getKey();
            int modifiers = methodDeclaration.getModifiers();

            for (MethodInvocation methodInvocation : entry.getValue()) {
                if (!Modifier.isPrivate(modifiers)
                        && ASTUtil.parent(TypeDeclaration.class, methodDeclaration) != ASTUtil.parent(TypeDeclaration.class, methodInvocation)) {
                    continue outerloop;
                }
            }

            if ((!Modifier.isPrivate(modifiers) && !methodDeclaration.isConstructor() && !Modifier.isStatic(modifiers)
                    && !hasAnnotation(methodDeclaration)
                    && !isMainMethod(methodDeclaration))
                    && !Modifier.isAbstract(ASTUtil.parent(TypeDeclaration.class, methodDeclaration).getModifiers())
                    && !ASTUtil.parent(TypeDeclaration.class, methodDeclaration).isInterface()) {
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

    private void buildHashMapWithMethodDeclarationsAndInvocations() {
        for (MethodInvocation methodInvocation : methodInvocationList) {
            for (MethodDeclaration methodDeclaration : methodUsages.keySet()) {
                if (methodDeclaration.resolveBinding().equals(methodInvocation.resolveMethodBinding())) {
                    methodUsages.get(methodDeclaration).add(methodInvocation);
                    break;
                }
            }
        }

    }

    private boolean hasAnnotation(MethodDeclaration methodDeclaration) {
        return methodDeclaration.modifiers().get(0) instanceof Annotation;
    }

    private boolean isMainMethod(MethodDeclaration methodDeclaration) {
        return (methodDeclaration.getName().toString().equals("main"));
    }
}