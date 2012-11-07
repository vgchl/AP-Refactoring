package nl.han.ica.core.issue;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

public abstract class IssueSolver {

    public abstract boolean canSolve(Issue issue);

    public Solution createSolution(Issue issue, Map<String, Parameter> parameters) {
        if (!canSolve(issue)) {
            throw new IllegalArgumentException("Cannot solve issue. This solver does not know how to solve that kind of issue.");
        }
        return internalSolve(issue, parameters);
    }

    public void applySolution(Solution solution) {
        if (solution.getIssueSolver() != this) {
            throw new IllegalArgumentException("Cannot apply solution. The solution was made by a different solver.");
        }
        internalApplySolution(solution);
    }

    protected abstract Solution internalSolve(Issue issue, Map<String, Parameter> parameters);

    protected abstract void internalApplySolution(Solution solution);

    protected CompilationUnit compilationUnitForASTNode(ASTNode node) {
        return (CompilationUnit) node.getRoot();
    }

    protected SourceFile sourceFileForCompilationUnit(CompilationUnit compilationUnit) {
        return (SourceFile) compilationUnit.getProperty(SourceFile.SOURCE_FILE_PROPERTY);
    }

}
