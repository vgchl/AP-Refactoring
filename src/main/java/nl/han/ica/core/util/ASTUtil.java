package nl.han.ica.core.util;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Helper functionality for common operations involving {@link ASTNode}s.
 */
public final class ASTUtil {

    /**
     * Private constructor to prevent class initialization.
     */
    private ASTUtil() {}

    /**
     * Find the nearest parent node of a certain type for an {@link ASTNode}.
     *
     * @param klass The type class of the parent node to find. Must be derived from ASTNode.
     * @param node  The node to find a parent node for.
     * @param <T>   The ASTNode derived type of the parent node.
     * @return The found parent, or null if no such parent exists.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ASTNode> T parent(final Class<T> klass, final ASTNode node) {
        ASTNode parent = node;
        do {
            parent = parent.getParent();
            if (parent == null) {
                return null;
            }
        } while (parent.getClass() != klass);
        return (T) parent;
    }

    public static int getAnnotationsSize(IBinding binding) {
        if (binding.getAnnotations() != null) {
            return binding.getAnnotations().length;
        }
        return 0;
    }

    public static boolean isMainMethod(MethodDeclaration methodDeclaration) {
        return (methodDeclaration.getName().toString().equals("main"));
    }

}
