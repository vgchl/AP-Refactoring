package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

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


    // Key value map for finding classes that have two or more subclasses
    // key = superclass, value = list of subclasses
    // return values that have two elements or more and check them for duplicate fields
    // create an issue if they contain duplicate fields

    private Map<Type, List<ASTNode>> subclassesPerSuperClass;

    public void visit(TypeDeclaration node) {

        Type superclass = node.getSuperclassType();

        if (superclass != null) {
            /* Get the compilationunit / node from the type declaration */
            ASTNode subClass = node.getParent();

            if (subclassesPerSuperClass.containsKey(superclass)) {
                subclassesPerSuperClass.get(superclass).add(subClass);
            } else {
                ArrayList<ASTNode> subclasses = new ArrayList<ASTNode>();
                subclasses.add(subClass);
                subclassesPerSuperClass.put(superclass, subclasses);
            }
        }

        filterTwoOrMoreSubclasses();
    }

    /**
     * Filter the subclassesPerSuperClass field for values that have more than one element.
     * We need at least two classes to see if there are duplicate fields.
     * This method calls checkForDuplicateFields for every list that contains more than one element.
     * If this list of classes contains duplicate fields, it is added to violatedNodes.
     */
    private void filterTwoOrMoreSubclasses() {

        for (List<ASTNode> listOfSubclasses : subclassesPerSuperClass.values()) {
            if (listOfSubclasses.size() > 1) {

                List<ASTNode> classesWithDuplicateFields = getClassesWithDuplicateFields(listOfSubclasses);

                if (!classesWithDuplicateFields.isEmpty()) {
                    // TODO: we cannot use the violatedNodes for this
                    // TODO: we need a structure to hold the superclass + the subclasses that have duplicate fields?
                }
            }
        }
    }

    /**
     * Check the given list of classes if they contain duplicate fields.
     *
     * @param listOfSubclasses
     * @return
     */
    private List<ASTNode> getClassesWithDuplicateFields(List<ASTNode> listOfSubclasses) {

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

        return classesWithDuplicateFields;
    }

    @Override
    public Set<Issue> detectIssues() {
        return null;  //TODO
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
