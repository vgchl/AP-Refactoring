/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

/**
 *
 * @author Corne
 */
public class FieldDeclarationVisitor extends ASTVisitor {

    private List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
    
    @Override
    public boolean visit(FieldDeclaration node) {
        fieldDeclarations.add(node);
        return super.visit(node);
    }
    
    public List<FieldDeclaration> getFieldDeclarationWithValue(String value){
        List<FieldDeclaration> equalFieldDeclarations = new ArrayList<>();
        for(FieldDeclaration fieldDeclaration : fieldDeclarations){
            VariableDeclaration variableDeclaration = (VariableDeclaration) fieldDeclaration.fragments().get(0);
            if(variableDeclaration.getInitializer().toString().equals(value)){
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
