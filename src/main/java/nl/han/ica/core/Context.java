package nl.han.ica.core;

import org.eclipse.jdt.core.dom.ASTVisitor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Context {

    private Set<SourceFile> sourceFiles;

    public Context() {
        sourceFiles = new HashSet<>();
    }

    public Context(final Set<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public void accept(ASTVisitor visitor) {
        for (SourceFile sourceFile : sourceFiles) {
            sourceFile.getCompilationUnit().accept(visitor);
        }
    }

    public void clear() {
        sourceFiles.clear();
    }

    public Set<SourceFile> getSourceFiles() {
        return Collections.unmodifiableSet(sourceFiles);
    }

    public void addSourceFile(final SourceFile sourceFile) {
        sourceFiles.add(sourceFile);
    }

    public void removeSourceFile(final SourceFile sourceFile) {
        sourceFiles.remove(sourceFile);
    }

    public boolean hasSourceFiles() {
        return sourceFiles.size() > 0;
    }

}
