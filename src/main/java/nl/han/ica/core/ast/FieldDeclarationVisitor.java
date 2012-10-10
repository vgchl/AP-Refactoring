/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 *
 * @author Corne
 */
public class FieldDeclarationVisitor extends VoidVisitorAdapter {

    private int numberOfFields = 0;
    
    @Override
    public void visit(FieldDeclaration n, Object arg) {
        //change super call when needed
        super.visit(n, arg);
        numberOfFields++;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }
    
}
