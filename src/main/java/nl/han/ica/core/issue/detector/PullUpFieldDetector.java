package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.ClassWithTwoSubclassesVisitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

import java.util.*;

/**
 *
 */
public class PullUpFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Pull up duplicate fields.";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    private ClassWithTwoSubclassesVisitor visitor;

    public PullUpFieldDetector() {
        visitor = new ClassWithTwoSubclassesVisitor(compilationUnits);
    }

    /**
     * Check the given list of classes if they contain duplicate fields.
     *
     * @param listOfSubclasses
     * @return
     */
    private boolean hasDuplicateFields(List<ASTNode> listOfSubclasses) {

        FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
        List<ASTNode> classesWithDuplicateFields = new ArrayList<ASTNode>();

        List<FieldDeclaration> allFieldDeclarations = new ArrayList<FieldDeclaration>();

        for (ASTNode node : listOfSubclasses) {

            node.accept(fieldDeclarationVisitor);
            allFieldDeclarations.addAll(fieldDeclarationVisitor.getFieldDeclarations());
        }
        Set<FieldDeclaration> fieldDeclarationSet = new HashSet<>();

        for (FieldDeclaration fieldDeclaration : allFieldDeclarations) {
            fieldDeclarationSet.add(fieldDeclaration);
        }

        return fieldDeclarationSet.size() < allFieldDeclarations.size();
    }

    @Override
    public void detectIssues() {

        visitor.clear();
        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
        }

        Map<String, List<ASTNode>> subclassesPerSuperclass = visitor.getSubclassesPerSuperClass();

        for (String type : subclassesPerSuperclass.keySet()) {
            List<ASTNode> listOfSubclasses = subclassesPerSuperclass.get(type);

            if (hasDuplicateFields(listOfSubclasses)) {
                Issue issue = new Issue(this);
                issue.setNodes(listOfSubclasses);

                issues.add(issue);
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
