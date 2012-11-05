/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.han.ica.core.ast.ARTASTFileRequestor;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 *
 * @author Corne
 */
public class Context {
    
    private Logger logger = Logger.getLogger(getClass().getName());
    ARTASTFileRequestor requestor;
    private List<SourceHolder> sourceHolders;
    
    public Context(List<SourceHolder> sourceHolders) {
        this.sourceHolders = sourceHolders;
    }
    
    private String[] getFilePaths(List<SourceHolder> sources){
        String[] filePaths = new String[sources.size()];
        for(int i=0; i<sources.size(); i++){            
            filePaths[i] = sources.get(i).getFile().getPath();
        }        
        return filePaths;
    }
    
    private String[] getFolderPaths(List<SourceHolder> sources){
        String[] filePaths = new String[sources.size()];
        for(int i=0; i<sources.size(); i++){            
            filePaths[i] = sources.get(i).getFile().getParentFile().getPath();
        }        
        return filePaths;
    }
    
    //TODO check if instead of files, we can get IProject or IClassFile (Eclipse classes)
    public void createCompilationUnits(){     
        ASTParser astParser = ASTParser.newParser(AST.JLS3);
        astParser.setEnvironment(getFolderPaths(sourceHolders), null, null, false); 
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setUnitName("test");
        Map options = JavaCore.getOptions();
        astParser.setCompilerOptions(options);
        requestor = new ARTASTFileRequestor();
        String[] bindings = new String[0];
        astParser.createASTs(getFilePaths(sourceHolders), null, bindings, requestor, null);       
    }

    public List<SourceHolder> getSourceHolders() {
        return sourceHolders;
    }
    
    public List<CompilationUnit> getCompilationUnits(){
        return requestor.getCompilationUnits();
    }
    
}
