package nl.han.ica.core.issue.detector.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
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

    @Override
    public boolean visit(MethodDeclaration node) {
        methodDeclarations.add(node);
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethodDeclarations() {
        return methodDeclarations;
    }
}
