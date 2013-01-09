package art.core.visitors;

import art.core.util.ASTUtil;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;


public class FieldAccessVisitor extends ASTVisitor {

    private ArrayList<FieldAccess> fieldAccessList = new ArrayList<>();
    private ArrayList<QualifiedName> qualifiedNameList = new ArrayList<>();

    @Override
    public boolean visit(FieldAccess node) {
        fieldAccessList.add(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionStatement expression) {
        return super.visit(expression);
    }

    @Override
    public boolean visit(QualifiedName node) {
        if (node.resolveBinding() != null && ASTUtil.parent(MethodDeclaration.class, node) != null) {
            qualifiedNameList.add(node);
        }
        return super.visit(node);
    }

    public ArrayList<QualifiedName> getQualifiedNameList() {
        return qualifiedNameList;
    }

    public ArrayList<FieldAccess> getFieldAccessList() {
        return fieldAccessList;
    }

}
