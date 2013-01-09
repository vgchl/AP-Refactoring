package art.core.parser;

import art.core.Context;
import art.core.SourceFile;
import art.core.util.FileUtil;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Set;

/**
 * Parses SourceFiles and constructs their AST trees.
 */
public class Parser {

    private ASTParser astParser;
    private Logger logger;

    /**
     * Instantiate a new Parser.
     */
    public Parser() {
        logger = Logger.getLogger(getClass().getName());

        initializeASTParser();
    }

    private void initializeASTParser() {
        astParser = ASTParser.newParser(AST.JLS3);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setCompilerOptions(JavaCore.getOptions());
    }

    /**
     * Parse a set of {@link SourceFile}s.
     *
     * @param context The SourceFiles to parse.
     * @return
     */
    public Set<CompilationUnit> parse(Context context) {
        astParser.setEnvironment(FileUtil.directoryPaths(context.getSourceFiles()), null, null, true);
        astParser.setUnitName("Refactor-Tool");

        String[] bindings = new String[0];
        ASTRequestor astRequestor = new ASTRequestor(context.getSourceFiles());

        logger.debug("Parsing context: " + context);
        astParser.createASTs(FileUtil.filePaths(context.getSourceFiles()), null, bindings, astRequestor, null);

        return astRequestor.getCompilationUnits();
    }

}
