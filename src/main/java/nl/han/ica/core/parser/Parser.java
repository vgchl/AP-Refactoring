package nl.han.ica.core.parser;

import nl.han.ica.core.SourceFile;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Set;
import nl.han.ica.core.util.FileUtil;

public class Parser {

    private ASTParser astParser;
    private Set<SourceFile> sourceFiles;
    private Logger logger;

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

    public Set<CompilationUnit> parse(Set<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;

        astParser.setEnvironment(FileUtil.directoryPaths(sourceFiles), null, null, false);
        astParser.setUnitName("test"); // TODO: Check what this does (and whether it can be useful);

        String[] bindings = new String[0];
        ASTRequestor astRequestor = new ASTRequestor(sourceFiles);
        astParser.createASTs(FileUtil.filePaths(sourceFiles), null, bindings, astRequestor, null);
        return astRequestor.getCompilationUnits();
    }

}
