package art.core.issue.detector;

import art.core.Context;
import art.core.SourceFile;
import art.core.visitors.MethodDeclarationVisitor;
import art.core.visitors.MethodInvocationVisitor;
import art.core.issue.IssueDetector;
import art.core.util.ASTUtil;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class HideMethodDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Hide Method";
    private static final String STRATEGY_DESCRIPTION = "Hide method when it is not used by any other class.";

    private List<MethodInvocation> methodInvocationList;
    private Map<MethodDeclaration, ArrayList<MethodInvocation>> methodUsages;

    public HideMethodDetector() {
        methodInvocationList = new ArrayList<>();
        methodUsages = new WeakHashMap<>();
    }

    @Override
    public void internalDetectIssues(Context context) {
        for (SourceFile sourceFile : context.getSourceFiles()) { // TODO: Rewrite with context.visit(ASTVisitor)
            MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
            sourceFile.getCompilationUnit().accept(methodDeclarationVisitor);

            for (MethodDeclaration methodDeclaration : methodDeclarationVisitor.getMethodDeclarations()) {
                methodUsages.put(methodDeclaration, new ArrayList<MethodInvocation>());
            }

            MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
            sourceFile.getCompilationUnit().accept(methodInvocationVisitor);
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
        for (Map.Entry<MethodDeclaration, ArrayList<MethodInvocation>> entry : methodUsages.entrySet()) {
            MethodDeclaration methodDeclaration = entry.getKey();
            int modifiers = methodDeclaration.getModifiers();

            if (Modifier.isPrivate(modifiers)) {
                continue;
            }
            for (MethodInvocation methodInvocation : entry.getValue()) {
                if (!Modifier.isPrivate(modifiers) &&
                        ASTUtil.parent(TypeDeclaration.class, methodDeclaration) != ASTUtil.parent(TypeDeclaration.class, methodInvocation)) {
                    continue outerloop;
                }
            }

            if ((!Modifier.isPrivate(modifiers) && !methodDeclaration.isConstructor() && !Modifier.isStatic(modifiers)
                    && !hasAnnotation(methodDeclaration)
                    && !ASTUtil.isMainMethod(methodDeclaration))
                    && !Modifier.isAbstract(ASTUtil.parent(TypeDeclaration.class, methodDeclaration).getModifiers())
                    && !ASTUtil.parent(TypeDeclaration.class, methodDeclaration).isInterface()) {
                createIssue(methodDeclaration);
            }
        }
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

    @Override
    public String getTitle() {
        return STRATEGY_NAME;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }

}
