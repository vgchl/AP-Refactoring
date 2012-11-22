package nl.han.ica.core.util;

import nl.han.ica.core.SourceFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Helper functionality for common operations involving {@link ASTNode}s.
 */
public class ASTUtil {

    public static CompilationUnit compilationUnitForASTNode(final ASTNode node) {
        return (CompilationUnit) node.getRoot();
    }

    public static SourceFile sourceFileForCompilationUnit(final CompilationUnit compilationUnit) {
        return (SourceFile) compilationUnit.getProperty(SourceFile.SOURCE_FILE_PROPERTY);
    }

    public static TypeDeclaration getTypeDeclarationForNode(ASTNode node) {
        ASTNode parentNode = node.getParent();
        if (parentNode instanceof TypeDeclaration) {
            return (TypeDeclaration) parentNode;
        }
        return getTypeDeclarationForNode(parentNode);
    }
    
    /**
     * Find the nearest parent node of a certain type for an {@link ASTNode}.
     *
     * @param klass The type class of the parent node to find. Must be derived from ASTNode.
     * @param node  The node to find a parent node for.
     * @param <T>   The ASTNode derived type of the parent node.
     * @return The found parent, or null if no such parent exists.
     */
    public static <T extends ASTNode> T parent(final Class<T> klass, final ASTNode node) {
        ASTNode parent = node;
        do {
            parent = parent.getParent();
        } while (parent.getClass() != klass);
        return (T) parent;
    }

}
