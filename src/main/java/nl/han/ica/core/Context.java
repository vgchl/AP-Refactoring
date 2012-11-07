/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core;

import java.io.File;
import java.util.List;
import java.util.Map;
import nl.han.ica.core.ast.ARTASTFileRequestor;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

/**
 *
 * @author Corne
 */
public class Context {
    
    private Logger logger = Logger.getLogger(getClass().getName());
    private ARTASTFileRequestor requestor;
    private List<File> files;
    
    public Context(List<File> files) {
        this.files = files;
    }
    
    private String[] getFilePaths(List<File> files){
        String[] filePaths = new String[files.size()];
        for(int i=0; i<files.size(); i++){            
            filePaths[i] = files.get(i).getPath();
        }        
        return filePaths;
    }
    
    private String[] getFolderPaths(List<File> files){
        String[] filePaths = new String[files.size()];
        for(int i=0; i<files.size(); i++){            
            filePaths[i] = files.get(i).getParentFile().getPath();
        }        
        return filePaths;
    }
    
    //TODO check if instead of files, we can get IProject or IClassFile (Eclipse classes)
    public void createCompilationUnits(){     
        ASTParser astParser = ASTParser.newParser(AST.JLS3);
        astParser.setEnvironment(getFolderPaths(files), null, null, false); 
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setUnitName("test");
        Map options = JavaCore.getOptions();
        astParser.setCompilerOptions(options);
        requestor = new ARTASTFileRequestor();
        String[] bindings = new String[0];
        astParser.createASTs(getFilePaths(files), null, bindings, requestor, null);       
    }
    
    public List<SourceFile> getSourceHolders(){
        return requestor.getSourceFiles();
    }
}
