package nl.han.ica.core.ast.visitors;

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

    private List<MethodInvocation> methodInvocations;

    public MethodInvocationVisitor() {
        methodInvocations = new ArrayList<>();
    }

    @Override
    public boolean visit(MethodInvocation methodInvocation) {
        methodInvocations.add(methodInvocation);
        return super.visit(methodInvocation);
    }

    public List<MethodInvocation> getMethodInvocations() {
        return methodInvocations;
    }
}
