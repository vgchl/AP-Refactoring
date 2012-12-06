package nl.han.ica.core.ast.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

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

