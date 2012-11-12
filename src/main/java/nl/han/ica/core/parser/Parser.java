package nl.han.ica.core.parser;

import nl.han.ica.core.SourceFile;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Set;

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

        astParser.setEnvironment(directoryPaths(sourceFiles), null, null, false);
        astParser.setUnitName("test"); // TODO: Check what this does (and whether it can be useful);

        String[] bindings = new String[0];
        ASTRequestor astRequestor = new ASTRequestor(sourceFiles);
        astParser.createASTs(filePaths(sourceFiles), null, bindings, astRequestor, null);
        return astRequestor.getCompilationUnits();
    }

    private String[] filePaths(Set<SourceFile> files) {
        String[] filePaths = new String[files.size()];
        int i = 0;
        for (SourceFile file : files) {
            filePaths[i] = file.getFile().getPath();
            i++;
        }
        return filePaths;
    }

    private String[] directoryPaths(Set<SourceFile> files) {
        String[] filePaths = new String[files.size()];
        int i = 0;
        for (SourceFile file : files) {
            filePaths[i] = file.getFile().getParentFile().getPath();
            i++;
        }
        return filePaths;
    }

}
