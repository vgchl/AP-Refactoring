package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wouter Konecny
 * @created: 12-11-12
 */
public class MethodDeclarationVisitor extends ASTVisitor {

    private List<MethodDeclaration> methodDeclarations = new ArrayList<>();
    private boolean useExternBindings = false;
    private boolean includeConstructor = false;

    @Override
    public boolean visit(MethodDeclaration node) {

        if (shouldAddMethod(node)) {
            methodDeclarations.add(node);
        }
        return super.visit(node);
    }

    private boolean shouldAddMethod(MethodDeclaration node) {
        if (node.isConstructor() && !includeConstructor) {
            return false;
        }/*else if(!useExternBindings && !node.getReturnType2().resolveBinding().isFromSource()
                && !node.getReturnType2().isPrimitiveType() && !node.getReturnType2().){
            return false;
        }*/ else if (node.resolveBinding() == null) {
            return false;
        }
        return true;
    }

    public void useExternBindings(boolean useExternBindings) {
        this.useExternBindings = useExternBindings;
    }

    public void includeConstructor(boolean includeConstructor) {
        this.includeConstructor = includeConstructor;
    }

    public List<MethodDeclaration> getMethodDeclarations() {
        return methodDeclarations;
    }
}
