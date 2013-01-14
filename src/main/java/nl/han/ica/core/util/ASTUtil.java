package nl.han.ica.core.util;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * Helper functionality for common operations involving {@link ASTNode}s.
 */
public final class ASTUtil {

    /**
     * Private constructor to prevent class initialization.
     */
    private ASTUtil() {
        // Private constructor to prevent class initialization.
    }

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

    public static void setVisibility(FieldDeclaration field, Modifier.ModifierKeyword visibility, ASTRewrite rewrite) {
        ListRewrite listRewrite = rewrite.getListRewrite(field, FieldDeclaration.MODIFIERS2_PROPERTY);
        setVisibility(field, visibility, listRewrite);
    }

    public static void setVisibility(MethodDeclaration method, Modifier.ModifierKeyword visibility, ASTRewrite rewrite) {
        ListRewrite listRewrite = rewrite.getListRewrite(method, MethodDeclaration.MODIFIERS2_PROPERTY);
        setVisibility(method, visibility, listRewrite);
    }

    private static void setVisibility(BodyDeclaration body, Modifier.ModifierKeyword visibility, ListRewrite listRewrite) {
        for (Object modifier : body.modifiers()) {
            if (modifier instanceof Modifier) {
                Modifier m = (Modifier) modifier;
                if (m.isPublic() || m.isProtected() || m.isProtected()) {
                    listRewrite.remove(m, null);
                    break;
                }
            }
        }
        listRewrite.insertFirst(body.getAST().newModifier(visibility), null);
    }


}
