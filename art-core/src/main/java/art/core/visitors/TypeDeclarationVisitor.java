package art.core.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TypeDeclarationVisitor extends ASTVisitor {

    private Set<TypeDeclaration> typeDeclarations;

    public TypeDeclarationVisitor() {
        typeDeclarations = new HashSet<>();
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        typeDeclarations.add(node);
        return super.visit(node);
    }

    public Set<TypeDeclaration> getTypeDeclarations() {
        return Collections.unmodifiableSet(typeDeclarations);
    }

}

