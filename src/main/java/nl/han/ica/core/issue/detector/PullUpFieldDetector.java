package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.ClassWithTwoSubclassesVisitor;
import org.apache.log4j.Logger;
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

    private final Logger log = Logger.getLogger(getClass().getName());

    private static final String STRATEGY_NAME = "Pull up duplicate Fields";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    private ClassWithTwoSubclassesVisitor visitor;

    public PullUpFieldDetector() {
        visitor = new ClassWithTwoSubclassesVisitor();
        log.info("Test");
    }

    /**
     * Check the given list of classes if they contain duplicate fields.
     *
     * @param listOfSubclasses
     * @return
     */
    private boolean hasDuplicateFields(List<ASTNode> listOfSubclasses) {

        log.debug("Checking for duplicates in list of " + listOfSubclasses.size());
        boolean duplicatesPresent = false;
        FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();

        List<FieldDeclaration> allFieldDeclarations = new ArrayList<FieldDeclaration>();

        for (ASTNode node : listOfSubclasses) {

            node.accept(visitor);
            List<FieldDeclaration> fieldDeclarations = visitor.getFieldDeclarations();

            for (FieldDeclaration field : fieldDeclarations) {

                if (allFieldDeclarations.contains(field)) {
                    duplicatesPresent = true;
                    log.debug("Field is already present.");
                } else {
                    allFieldDeclarations.add(field);
                    log.debug("Adding new field.");
                }
            }
        }

        return duplicatesPresent;
    }

    @Override
    public Set<Issue> detectIssues() {

        /* Clear everything */
        visitor.clear();
        issues.clear();

        /* Find classes that have two subclasses or more. */
        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
        }

        /* Get a map of superclass keys and a list of subclasses as value. */
        Map<Type, List<ASTNode>> subclassesPerSuperclass = visitor.getSubclassesPerSuperClass();

        /* For every key, check if the value has two subclasses or more. If so, create an issue. */
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
