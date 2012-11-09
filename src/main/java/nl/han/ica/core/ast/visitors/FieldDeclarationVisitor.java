/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;

/**
 *
 * @author Corne
 */
public class FieldDeclarationVisitor extends ASTVisitor {

    private List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
    private ClassInstanceCreation node;
    
    @Override
    public boolean visit(FieldDeclaration node) {
        fieldDeclarations.add(node);
        VariableDeclaration variableDeclaration = (VariableDeclaration) node.fragments().get(0);
        System.out.println("Testing Fielddeclarations" + node.getType().toString() + variableDeclaration.getInitializer().toString());

        return super.visit(node);
    }

    //TODO remove this method, just temp method for resolvebindings
    @Override
    public boolean visit(ClassInstanceCreation node) {
        this.node = node;
        return super.visit(node);
    }
    
    

    public ClassInstanceCreation getNode() {
        return node;
    }
    
    
    public List<FieldDeclaration> getFieldDeclarationWithValue(String value){
        List<FieldDeclaration> equalFieldDeclarations = new ArrayList<>();
        for(FieldDeclaration fieldDeclaration : fieldDeclarations){
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclaration.fragments().get(0);
            if(variableDeclaration.getInitializer().toString().equals(value)){
                System.out.println("Testing Fielddeclarations" + variableDeclaration.getInitializer().toString());
                equalFieldDeclarations.add(fieldDeclaration);
            }
        }
        return equalFieldDeclarations;
    }
    
    public boolean hasFieldName(String name){
        for(FieldDeclaration fieldDeclaration : fieldDeclarations){
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclaration.fragments().get(0);
            if(variableDeclaration.getName().toString().equals(name)){
                return true;
            }
        }
        return false;
    }
    
}
