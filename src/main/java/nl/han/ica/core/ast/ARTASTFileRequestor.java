/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast;

import java.util.ArrayList;
import java.util.List;
import nl.han.ica.core.SourceHolder;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IBinding;

/**
 *
 * @author Corne
 */
public class ARTASTFileRequestor extends FileASTRequestor {

    private List<CompilationUnit> compilationUnits = new ArrayList<>();;

    public ARTASTFileRequestor() {

    }
    
    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        super.acceptAST(sourceFilePath, ast);
        compilationUnits.add(ast);
    }
    
    @Override
    public void acceptBinding(String bindingKey, IBinding binding) {
        super.acceptBinding(bindingKey, binding);
    }

    public List<CompilationUnit> getCompilationUnits() {
        return compilationUnits;
    }
    
    
}
