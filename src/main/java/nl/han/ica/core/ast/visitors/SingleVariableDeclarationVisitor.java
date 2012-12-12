package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sjoerd van den Top
 * Date: 6-12-12
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public class SingleVariableDeclarationVisitor extends ASTVisitor {

    private List<VariableDeclarationFragment> singleVariableDeclarationlist = new ArrayList<>();

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        singleVariableDeclarationlist.add(node);
        return super.visit(node);
    }

    public List<VariableDeclarationFragment> getSingleVariableDeclarationlist() {
        return singleVariableDeclarationlist;
    }
}
