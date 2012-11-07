/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.SourceFile;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IBinding;

/**
 *
 * @author Corne
 */
public class ARTASTFileRequestor extends FileASTRequestor {

    private List<SourceFile> sourceFiles = new ArrayList<>();
 
    public ARTASTFileRequestor() {

    }
    
    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        super.acceptAST(sourceFilePath, ast);
        addSourceHolder(new File(sourceFilePath), ast);      
    }
    
    private void addSourceHolder(File file, CompilationUnit compilationUnit){
        SourceFile sourceFile = new SourceFile();
        sourceFile.setCompilationUnit(compilationUnit);
        sourceFile.setFile(file);
        sourceFiles.add(sourceFile);
    }
    
    @Override
    public void acceptBinding(String bindingKey, IBinding binding) {
        super.acceptBinding(bindingKey, binding);
    }

    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }
    
    
}
