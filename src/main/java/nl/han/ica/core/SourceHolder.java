/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.han.ica.core.util.FileUtil;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

/**
 *
 * @author Corne
 */
public class SourceHolder {
    
    private File file;    
    private IDocument document;    
    private CompilationUnit compilationUnit;
    

    public IDocument getDocument() {
        return document;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        setDocument(file);
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    } 
    
    private void setDocument(File file){
        try {
            String fileContent = FileUtil.getFileContent(file);
            document = new Document(fileContent);
        } catch (IOException ex) {
            Logger.getLogger(SourceHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
