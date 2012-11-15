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

import java.util.*;

/**
 *
 */
public class PullUpFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Pull up duplicate fields.";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    private Logger logger = Logger.getLogger(getClass().getName());

    private ClassWithTwoSubclassesVisitor visitor;

    public PullUpFieldDetector() {
        visitor = new ClassWithTwoSubclassesVisitor();
        logger.debug("Test");
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
        Set<FieldDeclaration> fieldDeclarationSet = new HashSet<>();

        for (FieldDeclaration fieldDeclaration : allFieldDeclarations) {
            fieldDeclarationSet.add(fieldDeclaration);
        }

        return fieldDeclarationSet.size() < allFieldDeclarations.size();
    }

    @Override
    public Set<Issue> detectIssues() {

        visitor.clear();
        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
            logger.debug("comptest");
        }

        //visitor.filterTwoOrMoreSubclasses();

        Map<Type, List<ASTNode>> subclassesPerSuperclass = visitor.getSubclassesPerSuperClass();

        logger.debug("Test map for content!! "+subclassesPerSuperclass.toString());

        for (Type type : subclassesPerSuperclass.keySet()) {
            List<ASTNode> listOfSubclasses = subclassesPerSuperclass.get(type);
            logger.debug("blup");
            if (hasDuplicateFields(listOfSubclasses)) {
                logger.debug("blaat");
                Issue issue = new Issue(this);
                listOfSubclasses.add(0, type);
                // TODO: remove the classes from the list that do not have duplicate fields
                issue.setNodes(listOfSubclasses);

                issues.add(issue);
            }
        }

        logger.debug(issues.toString());

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
