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
            MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
            compilationUnit.accept(methodDeclarationVisitor);
            methodDeclarationList.addAll(methodDeclarationVisitor.getMethodDeclarations());

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
                if (methodDeclaration.resolveBinding().equals(methodInvocation.resolveMethodBinding())
                        && !Modifier.isPrivate(modifiers)
                        && ASTUtil.parent(TypeDeclaration.class, methodDeclaration) != ASTUtil.parent(TypeDeclaration.class, methodInvocation)) {
                    continue outerloop;
                }
            }

            if ((!Modifier.isPrivate(modifiers) && !methodDeclaration.isConstructor() && !Modifier.isStatic(modifiers)
                    && !hasOverrideAnnotation(methodDeclaration)
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
        for (MethodDeclaration methodDeclaration : methodDeclarationList) {

            if (!methodUsages.containsKey(methodDeclaration)) {
                methodUsages.put(methodDeclaration, new ArrayList<MethodInvocation>());
            }

            for (MethodInvocation methodInvocation : methodInvocationList) {
                if (methodDeclaration.resolveBinding().equals(methodInvocation.resolveMethodBinding())) {
                    methodUsages.get(methodDeclaration).add(methodInvocation);
                }
            }

            // Remove already sorted MethodInvocations from the list.
            for (MethodInvocation methodInvocation : methodUsages.get(methodDeclaration)) {
                methodInvocationList.remove(methodInvocation);
            }
        }
    }

    private boolean hasOverrideAnnotation(MethodDeclaration methodDeclaration) {
        if (methodDeclaration.modifiers().get(0) instanceof MarkerAnnotation) {
            return true;
        }

        /*if (methodDeclaration.resolveBinding() != null) {
            IAnnotationBinding[] annotationBindingList = methodDeclaration.resolveBinding().getAnnotations();
            for (IAnnotationBinding binding : annotationBindingList) {
                if (binding.toString().contains("Override")) {
                    return true;
                }
            }
        }*/
        return false;
    }

    private boolean isMainMethod(MethodDeclaration methodDeclaration) {
        return (methodDeclaration.getName().toString().equals("main"));
    }
}