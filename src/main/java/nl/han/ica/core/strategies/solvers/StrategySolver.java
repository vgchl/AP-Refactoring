package nl.han.ica.core.strategies.solvers;


import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.pmd.RuleViolation;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public abstract class StrategySolver  {


    protected CompilationUnit compilationUnit = null;
    protected RuleViolation ruleViolation = null;

    
    public StrategySolver(RuleViolation ruleViolation){
        this.ruleViolation = ruleViolation;
    }

    public abstract void rewriteAST();

    public void buildAST(File file)  {
        BufferedReader in = null;
        try {
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            in = new BufferedReader(new FileReader(file));
            final StringBuffer buffer = new StringBuffer();
            String line = null;
            while (null != (line = in.readLine())) {
                buffer.append(line).append("\n");
            }
            
            parser.setSource(buffer.toString().toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            parser.setResolveBindings(true);
            compilationUnit = (CompilationUnit) parser.createAST(null);

        } catch (IOException ex) {
            Logger.getLogger(StrategySolver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(StrategySolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    


    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public RuleViolation getRuleViolation() {
        return ruleViolation;
    }

    public void setRuleViolation(RuleViolation ruleViolation) {
        this.ruleViolation = ruleViolation;
    }

}
