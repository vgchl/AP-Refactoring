package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sjoerd van den Top
 * Date: 7-11-12
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class TypeDeclarationVisitor extends ASTVisitor {

    @Override
    public boolean visit(TypeDeclaration node){
        FieldDeclaration[] fields = node.getFields();
        for(FieldDeclaration field : fields){
            System.out.println(field.getType().toString());
        }
        TypeDeclaration[] types = node.getTypes();
        for(TypeDeclaration type : types){
            System.out.println(type.toString());
        }
        return super.visit(node);
    }
}
