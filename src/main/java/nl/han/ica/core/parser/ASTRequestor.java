package nl.han.ica.core.parser;

import nl.han.ica.core.SourceFile;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.util.HashSet;
import java.util.Set;

public class ASTRequestor extends FileASTRequestor {

    private Set<CompilationUnit> compilationUnits;
    private Set<SourceFile> sourceFiles;

    public ASTRequestor(Set<SourceFile> sourceFiles) {
        compilationUnits = new HashSet<>();
        this.sourceFiles = sourceFiles;
    }

    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        super.acceptAST(sourceFilePath, ast);
        compilationUnits.add(ast);
        for (SourceFile sourceFile : sourceFiles) {
            if (sourceFile.getFile().getAbsolutePath().equals(sourceFilePath)) {
                sourceFile.setCompilationUnit(ast);
                ast.setProperty(SourceFile.SOURCE_FILE_PROPERTY, sourceFile);
            }
        }
    }

    public Set<CompilationUnit> getCompilationUnits() {
        return compilationUnits;
    }

}
