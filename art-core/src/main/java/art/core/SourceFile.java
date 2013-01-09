package art.core;

import art.core.util.FileUtil;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import java.io.File;
import java.io.IOException;

/**
 * Wraps around a {@link java.io.File} and its {@link org.eclipse.jdt.core.dom.CompilationUnit}.
 */
public class SourceFile {

    public static final String SOURCE_FILE_PROPERTY = "art.core.source_file.source_file_property";

    private File file;
    private CompilationUnit compilationUnit;

    /**
     * Instantiate a new SourceFile for a certain {@link java.io.File}.
     *
     * @param file The actual file.
     */
    public SourceFile(File file) {
        this.file = file;
    }

    /**
     * Gets the {@link java.io.File} this SourceFile wraps around.
     *
     * @return the File this SourceFile wraps around.
     */
    public File getFile() {
        return file;
    }

    /**
     * Set the SourceFile's File.
     *
     * @param file The File this SourceFile wraps around.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Set the CompilationUnit that belongs to the content of the SourceFile's file.
     *
     * @param compilationUnit The compilation unit.
     */
    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
        this.compilationUnit.setProperty(SOURCE_FILE_PROPERTY, this);
    }

    /**
     * Get the compilation unit that belongs to the content of the source file's file.
     *
     * @return The source file's compilation unit
     */
    public CompilationUnit getCompilationUnit() { // @TODO: Check if this method should be replaced by Context
        return compilationUnit;
    }

    /**
     * Create a document based on this source file's file.
     *
     * @return Document based on the source file's file.
     * @throws java.io.IOException Thrown when the file's contents could not be read.
     */
    public IDocument toDocument() throws IOException {
        return new Document(FileUtil.getFileContent(file));
    }

}
