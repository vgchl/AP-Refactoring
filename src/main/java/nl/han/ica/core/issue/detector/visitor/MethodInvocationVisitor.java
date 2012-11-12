package nl.han.ica.core.issue.detector.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author: Wouter Konecny
 * @created: 12-11-12
 */
public class MethodInvocationVisitor extends ASTVisitor {

    private Map<IMethodBinding, List<MethodInvocation>> methodUsages;

    public MethodInvocationVisitor() {
        methodUsages = new WeakHashMap<>();
    }

    @Override
    public boolean visit(MethodInvocation methodInvocation) {
        try {
            IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();

            if (!methodUsages.containsKey(methodBinding)) {
                methodUsages.put(methodBinding, new ArrayList<MethodInvocation>());
            }
            methodUsages.get(methodBinding).add(methodInvocation);
        } catch (NullPointerException e) {

        }
        return super.visit(methodInvocation);
    }

    public Map<IMethodBinding, List<MethodInvocation>> getMethodInvocations() {
        return methodUsages;
    }
}
