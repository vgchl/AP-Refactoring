/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.ast.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;

/**
 *
 * @author Corne
 */
public class NumberLiteralVisitor extends ASTVisitor  {

    private CompilationUnit compilationUnit;
    private List<NumberLiteral> numberLiterals;

    public NumberLiteralVisitor(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
        numberLiterals = new ArrayList<>();
    }

    @Override
    public boolean visit(NumberLiteral node) {
        numberLiterals.add(node);
        return super.visit(node);
    }


    /**
     * Get the number literal that violated the rules
     * @param beginLine beginline of the ruleviolation
     * @param beginColumn begincolumn of the ruleviolation
     * @return numberliteral that starts on same beginline and begincolumn
     */
    public NumberLiteral getLiteralViolation(int beginLine, int beginColumn) {
        NumberLiteral literalViolation = null;

        for(NumberLiteral numberLiteral : numberLiterals){
            if(compilationUnit.getColumnNumber(numberLiteral.getStartPosition()) == beginColumn-1 &&
                    compilationUnit.getLineNumber(numberLiteral.getStartPosition()) == beginLine){
                literalViolation = numberLiteral;
            }
        }
        return literalViolation;
    }

}
