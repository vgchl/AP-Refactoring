/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Corne
 */
public class FieldDeclarationVisitor extends ASTVisitor {

    private List<FieldDeclaration> fieldDeclarations = new ArrayList<>();

    @Override
    public boolean visit(FieldDeclaration node) {
        System.out.println("fdv: " + node);
        if(((VariableDeclarationFragment) node.fragments().get(0)).resolveBinding() != null){
            fieldDeclarations.add(node);
        }
        return super.visit(node);
    }

    public final List<FieldDeclaration> getFieldDeclarations() {
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
