package nl.han.ica.core.strategies.solvers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.pmd.RuleViolation;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

public abstract class StrategySolver  {

    
    protected CompilationUnit compilationUnit;
    protected IRuleViolation ruleViolation;
    protected RuleViolation ruleViolation;
    protected ASTParser astParser;
    private Parameters parameters;
    
    public StrategySolver(IRuleViolation ruleViolation){
        this.ruleViolation = ruleViolation;
    }

    public StrategySolver(IRuleViolation ruleViolation, Parameters parameters) {
        this(ruleViolation);
        this.parameters = parameters;
    }

    public abstract void rewriteAST();

    public void buildAST(File file) {

        astParser = ASTParser.newParser(AST.JLS3);

        String existingFile = getExistingFileContents(file);
        document = new Document(existingFile);

        astParser.setSource(existingFile.toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);

        Map options = JavaCore.getOptions();
        astParser.setCompilerOptions(options);
        compilationUnit = (CompilationUnit) astParser.createAST(null);
        compilationUnit.recordModifications();

    }
    
    private String getExistingFileContents(File file) {
        
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            char[] buffer = new char[1024];
            int returnedBytes = br.read(buffer);
            while (returnedBytes != -1) {
                sb.append(buffer, 0, returnedBytes);
                returnedBytes = br.read(buffer);
            }

        return sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(StrategySolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    protected TypeDeclaration getTopLevelTypeDeclaration(){
        Iterator iter = compilationUnit.types().iterator();
        while (iter.hasNext()) {
            TypeDeclaration td = (TypeDeclaration) iter.next();
            if (td.getParent().equals(compilationUnit)
                    && (td.getModifiers() & Modifier.PUBLIC) > 0) {
                return td;
            }
        }
        return null;      
    }
    
    protected void refreshCompilationUnit(){
        astParser.setSource(document.get().toCharArray());
        compilationUnit = (CompilationUnit) astParser.createAST(null);
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public IDocument getDocument() {
        return document;
    }

    public IRuleViolation getRuleViolation() {

        return ruleViolation;
    }

    public void setRuleViolation(IRuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Parameters getDefaultParameters() {
        return new Parameters();
    }
}
