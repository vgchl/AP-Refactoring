package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.ClassWithTwoSubclassesVisitor;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import java.util.*;

/**
 *
 */
public class PullUpFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Pull up Duplicate Fields.";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    private Logger logger = Logger.getLogger(getClass().getName());

    private ClassWithTwoSubclassesVisitor visitor;

    public PullUpFieldDetector() {
        visitor = new ClassWithTwoSubclassesVisitor();
    }

    /**
     * Check the given list of classes if they contain duplicate fields.
     *
     * @param listOfSubclasses
     * @return
     */
    private Set<FieldDeclaration> getDuplicateFields(List<ASTNode> listOfSubclasses) {

        FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();

        List<FieldDeclaration> allFieldDeclarations = new ArrayList<FieldDeclaration>();

        Set<FieldDeclaration> returnValues = new HashSet<FieldDeclaration>();

        for (ASTNode node : listOfSubclasses) {

            node.accept(visitor);
            allFieldDeclarations.addAll(visitor.getFieldDeclarations());
        }

        for (int i = 0; i < allFieldDeclarations.size(); i++) {

            FieldDeclaration field = allFieldDeclarations.get(i);

            for (int j = i; j < allFieldDeclarations.size(); j++) {

                FieldDeclaration anotherField = allFieldDeclarations.get(j);
                if (field.getType().toString().equals(anotherField.getType().toString())) {
                    returnValues.add(field);
                    returnValues.add(anotherField);
                }
            }
        }


        return returnValues;
    }

    @Override
    public Set<Issue> detectIssues() {

        visitor.clear();
        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
        }

        Map<String, List<ASTNode>> subclassesPerSuperclass = visitor.getSubclassesPerSuperClass();

        for (String type : subclassesPerSuperclass.keySet()) {
            List<ASTNode> listOfSubclasses = subclassesPerSuperclass.get(type);

            Set<FieldDeclaration> duplicateFields = getDuplicateFields(listOfSubclasses);
            logger.debug("Duplicate fields found: " + duplicateFields.size());

            if (duplicateFields.size() > 0) {
                logger.debug("Duplicate fields found: creating an issue.");
                Issue issue = new Issue(this);

                issue.setNodes(new ArrayList<ASTNode>(duplicateFields));

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
