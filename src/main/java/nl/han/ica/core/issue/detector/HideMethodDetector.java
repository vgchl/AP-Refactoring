package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.*;

import java.util.*;

/**
 * @author: Wouter Konecny
 * @created: 12-11-12
 */
public class HideMethodDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Hide Method";
    public static final String STRATEGY_DESCRIPTION = "Hide method when it is not used by any other class.";

    private List<MethodDeclaration> methodDeclarationList = new ArrayList<>();

    private Map<IMethodBinding, List<MethodInvocation>> methodUsages;

    public HideMethodDetector() {
        methodUsages = new WeakHashMap<>();
    }

    @Override
    public Set<Issue> detectIssues() {
        return null;
    }

//    @Override
//    public boolean visit(MethodDeclaration node) {
//        node.resolveBinding().getMethodDeclaration();
//        return super.visit(node);
//    }

//    @Override
//    public boolean visit(MethodInvocation node) {
//        node.resolveMethodBinding().getMethodDeclaration().getName();
//        if (invocationsForMethods.get(activeMethod) == null) {
//            invocationsForMethods.put(activeMethod, new ArrayList<MethodInvocation>());
//        }
//        invocationsForMethods.get(activeMethod).add(node);
//        return super.visit(node);
//    }
//
//    @Override
//    public boolean visit(MethodInvocation methodInvocation) {
//        try {
//            IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
//
//            if (!methodUsages.containsKey(methodBinding)) {
//                methodUsages.put(methodBinding, new ArrayList<MethodInvocation>());
//            }
//            methodUsages.get(methodBinding).add(methodInvocation);
//        } catch (NullPointerException e) {
//
//        }
//        return super.visit(methodInvocation);
//    }

    /**
     * Finds all violated nodes and places them in the violatedNodes list.
     */
    private void findViolatedNodes() {

        for (Map.Entry<IMethodBinding, List<MethodInvocation>> entry : methodUsages.entrySet()) {
            System.out.println("Method: " + entry.getKey().getName());
            for (MethodInvocation methodInvocation : entry.getValue()) {
                System.out.println("\tInvocation: " + methodInvocation.getName());
            }
        }

        outerloop:
        for (Map.Entry<IMethodBinding, List<MethodInvocation>> entry : methodUsages.entrySet()) {

            IMethodBinding methodDeclaration = entry.getKey();
            List<MethodInvocation> methodInvocationList = entry.getValue();

            System.out.println("Declaration: " + methodDeclaration.getName());

            for (MethodInvocation methodInvocation  : methodInvocationList) {
                //TypeDeclaration typeDeclarationForMethodClass = getTypeDeclarationForNode(methodDeclaration);
                //String methodClass = typeDeclarationForMethodClass.getName().toString();

                TypeDeclaration typeDeclarationForInvocationClass = getTypeDeclarationForNode(methodInvocation);
                String invocationClass = typeDeclarationForInvocationClass.getName().toString();

                System.out.println("  MC: " + methodInvocation + " - IC " + invocationClass);

                int modifiers = methodDeclaration.getModifiers();
                IMethodBinding methodDeclarationBinding = methodDeclaration.getMethodDeclaration();
                IMethodBinding methodInvocationBinding = methodInvocation.resolveMethodBinding().getMethodDeclaration();
                System.out.println("xx");
                if(methodDeclaration.getMethodDeclaration().equals(methodInvocation.resolveMethodBinding().getMethodDeclaration()) && Modifier.isPublic(modifiers)) {
                    System.out.println("Found private: " + methodDeclaration.getName());
                    continue outerloop;
                }
            }

            System.out.println("Could be private.");
            if(!Modifier.isPrivate(methodDeclaration.getModifiers())) {
                System.out.println("SHOULD BE PRIVATE!");
                //violatedNodes.add(methodDeclaration);
            }
        }
    }

    // Throw exception when node is null.
    private TypeDeclaration getTypeDeclarationForNode(ASTNode node) {
        ASTNode parentNode = node.getParent();
        if(parentNode instanceof TypeDeclaration) {
            return (TypeDeclaration) parentNode;
        }
        return getTypeDeclarationForNode(parentNode);
    }
//
//    @Override
//    public void before() {
//        // Do nothing
//    }
//
//    @Override
//    public void after() {
//        findViolatedNodes();
//    }

    @Override
    public String getTitle() {
        return STRATEGY_NAME;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }
}