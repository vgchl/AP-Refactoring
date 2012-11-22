package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wouter Konecny
 * @created: 12-11-12
 */
public class MethodInvocationVisitor extends ASTVisitor {

    private List<MethodInvocation> methodInvocations;

    public MethodInvocationVisitor() {
        methodInvocations = new ArrayList<>();
    }

    /**
     * Adds all OWN methodInvocations to the methodInvocation list.
     *
     * @param methodInvocation
     * @return
     */
    @Override
    public boolean visit(MethodInvocation methodInvocation) {
        if (methodInvocation.resolveMethodBinding() != null && methodInvocation.resolveMethodBinding().getDeclaringClass().isFromSource()) {
            methodInvocations.add(methodInvocation);
        }
        return super.visit(methodInvocation);
    }

    public List<MethodInvocation> getMethodInvocations() {
        return methodInvocations;
    }
}
