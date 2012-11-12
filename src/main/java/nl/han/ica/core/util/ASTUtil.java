package nl.han.ica.core.util;

import nl.han.ica.core.SourceFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * User: Teun
 * Date: 09-11-12
 * Time: 14:14
 */
public class ASTUtil {

    public static CompilationUnit compilationUnitForASTNode(final ASTNode node) {
        return (CompilationUnit) node.getRoot();
    }

    public static SourceFile sourceFileForCompilationUnit(final CompilationUnit compilationUnit) {
        return (SourceFile) compilationUnit.getProperty(SourceFile.SOURCE_FILE_PROPERTY);
    }

    public static <T extends ASTNode> T parent(final Class<T> klass, final ASTNode node) {
        ASTNode parent = node;
        do {
            parent = parent.getParent();
        } while (parent.getClass() != klass);
        return (T) parent;
    }

}
