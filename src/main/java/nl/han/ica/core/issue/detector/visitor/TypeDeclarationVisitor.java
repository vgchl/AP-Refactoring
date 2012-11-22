package nl.han.ica.core.issue.detector.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sjoerd
 * Date: 22-11-12
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class TypeDeclarationVisitor extends ASTVisitor {

    private List<TypeDeclaration> typeDeclarations = new ArrayList<>();

    @Override
    public boolean visit(TypeDeclaration node){
        typeDeclarations.add(node);
        return super.visit(node);
    }

    public List<TypeDeclaration> getTypeDeclarations() {
        return typeDeclarations;
    }
}
