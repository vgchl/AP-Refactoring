package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.ClassWithTwoSubclassesVisitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class PullUpFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Pull up duplicate fields.";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    private ClassWithTwoSubclassesVisitor visitor;

    private Set<Issue> issues;

    public PullUpFieldDetector() {
        visitor = new ClassWithTwoSubclassesVisitor();
    }

    /**
     * Check the given list of classes if they contain duplicate fields.
     *
     * @param listOfSubclasses
     * @return
     */
    private boolean hasDuplicateFields(List<ASTNode> listOfSubclasses) {

        FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
        List<ASTNode> classesWithDuplicateFields = new ArrayList<ASTNode>();

        List<FieldDeclaration> allFieldDeclarations = new ArrayList<FieldDeclaration>();

        for (ASTNode node : listOfSubclasses) {

            node.accept(visitor);
            allFieldDeclarations.addAll(visitor.getFieldDeclarations());
        }

        for (FieldDeclaration fieldDeclaration : allFieldDeclarations) {
            // TODO: if duplicate: add parent (compilationunit) to the list of classes
            // TODO: remember to add the classes of both occurences of the duplicate field.
        }
        // TODO: check allFieldDeclarations for duplicates.
        // TODO: if a duplicate is found, add the class that the declaration is in to the return list.
        return false;
    }

    @Override
    public Set<Issue> detectIssues() {

        visitor.clear();
        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
        }

        Map<Type, List<ASTNode>> subclassesPerSuperclass = visitor.getSubclassesPerSuperClass();

        for (Type type : subclassesPerSuperclass.keySet()) {
            List<ASTNode> listOfSubclasses = subclassesPerSuperclass.get(type);

            if (hasDuplicateFields(listOfSubclasses)) {
                Issue issue = new Issue(this);
                listOfSubclasses.add(0, type);
                // TODO: remove the classes from the list that do not have duplicate fields
                issue.setNodes(listOfSubclasses);

                issues.add(issue);
            }
        }

        return issues;
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
