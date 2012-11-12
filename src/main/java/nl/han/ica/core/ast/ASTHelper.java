/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * @author Corne
 */
public class ASTHelper {
    public static TypeDeclaration getTypeDeclarationForNode(ASTNode node) {
        ASTNode parentNode = node.getParent();
        if (parentNode instanceof TypeDeclaration) {
            return (TypeDeclaration) parentNode;
        }
        return getTypeDeclarationForNode(parentNode);
    }
}
