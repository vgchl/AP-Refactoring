package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldAccessVisitor;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class EncapsulateFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Encapsulate Field";
    private static final String STRATEGY_DESCRIPTION = "There is a public field. Make it private and provide accessors.";
    private ArrayList<FieldDeclaration> fieldDeclarations;
    private ArrayList<FieldAccess> fieldAccesses;

    public EncapsulateFieldDetector() {
        fieldDeclarations = new ArrayList<>();
        fieldAccesses = new ArrayList<>();
    }

    @Override
    public void detectIssues() {
        for (CompilationUnit compilationUnit : compilationUnits) {
            FieldAccessVisitor fieldAccessVisitor = new FieldAccessVisitor();
            compilationUnit.accept(fieldAccessVisitor);
            fieldAccesses.addAll(fieldAccessVisitor.getFieldAccessList());

            FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
            compilationUnit.accept(fieldDeclarationVisitor);
            fieldDeclarations.addAll(fieldDeclarationVisitor.getFieldDeclarations());
        }

        for (FieldDeclaration declaration : fieldDeclarations) {
            if ((declaration.getModifiers() & Modifier.PUBLIC) != 0) {
                createIssue(declaration);
            }
        }
    }

    @Override
    public String getTitle() {
        return STRATEGY_NAME;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }
}
