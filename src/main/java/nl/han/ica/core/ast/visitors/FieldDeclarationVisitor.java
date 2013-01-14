/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import java.util.HashSet;
import java.util.Set;

public class FieldDeclarationVisitor extends ASTVisitor {

    private Set<FieldDeclaration> fieldDeclarations;

    public FieldDeclarationVisitor() {
        fieldDeclarations = new HashSet<>();
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        fieldDeclarations.add(node);
        return super.visit(node);
    }

    public final Set<FieldDeclaration> getFieldDeclarations() {
        return fieldDeclarations;
    }

    public boolean hasFieldName(String name) {
        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclaration.fragments().get(0);
            if (variableDeclaration.getName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
