package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;


import java.util.ArrayList;

public class FieldAccessVisitor extends ASTVisitor {

    private ArrayList<FieldAccess> fieldAccessList = new ArrayList();

    @Override
    public boolean visit(FieldAccess node) {
        fieldAccessList.add(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionStatement expression) {
        return super.visit(expression);
    }

    public ArrayList<FieldAccess> getFieldAccessList() {
        return fieldAccessList;
    }
}
