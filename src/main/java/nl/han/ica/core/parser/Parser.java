/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


package nl.han.ica.core.parser;

import nl.han.ica.core.SourceFile;
import nl.han.ica.core.util.FileUtil;
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
    private Set<SourceFile> sourceFiles;
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
     * @param sourceFiles The SourceFiles to parse.
     * @return
     */
    public Set<CompilationUnit> parse(Set<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;

        astParser.setEnvironment(FileUtil.directoryPaths(sourceFiles), null, null, true);
        astParser.setUnitName("Refactor-Tool"); // TODO: Check what this does (and whether it can be useful);

        String[] bindings = new String[0]; //TODO: see todo above
        ASTRequestor astRequestor = new ASTRequestor(sourceFiles);
        astParser.createASTs(FileUtil.filePaths(sourceFiles), null, bindings, astRequestor, null);
        return astRequestor.getCompilationUnits();
    }

}
