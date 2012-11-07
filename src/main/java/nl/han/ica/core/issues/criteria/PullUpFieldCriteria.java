package nl.han.ica.core.issues.criteria;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.strategies.Strategy;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 9-11-12
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
public class PullUpFieldCriteria extends Criteria {


    // Key value map for finding classes that have two or more subclasses
    // key = superclass, value = list of subclasses
    // return values that have two elements or more and check them for duplicate fields
    // create an issue if they contain duplicate fields

    private Map<Type, List<ASTNode>> subclassesPerSuperClass;

    @Override
    public boolean visit(TypeDeclaration node) {

        Type superclass = node.getSuperclassType();

        if (superclass != null) {
            ASTNode parent = node.getParent();

            if (subclassesPerSuperClass.containsKey(superclass)) {
                subclassesPerSuperClass.get(superclass).add(parent);
            } else {
                ArrayList<ASTNode> subclasses = new ArrayList<ASTNode>();
                subclasses.add(parent);
                subclassesPerSuperClass.put(superclass, subclasses);
            }
        }

        filterTwoOrMoreSubclasses();

        return false;
    }

    /**
     * Filter the subclassesPerSuperClass field for values that have more than one element.
     * We need at least two classes to see if there are duplicate fields.
     * This method calls checkForDuplicateFields for every list that contains more than one element.
     * If the classes have duplicate fields, a new Issue is created.
     */
    private void filterTwoOrMoreSubclasses() {

        for (List<ASTNode> listOfSubclasses : subclassesPerSuperClass.values()) {
            if (listOfSubclasses.size() > 1) {

                List<ASTNode> classesWithDuplicateFields = getClassesWithDuplicateFields(listOfSubclasses);
                if (!classesWithDuplicateFields.isEmpty()) {
                    // TODO: create an issue with the list of subclasses and the superclass.
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

        for (ASTNode node : listOfSubclasses) {
            node.accept(visitor);
            List<FieldDeclaration> fieldDeclarations = visitor.getFieldDeclarations();


        }

        return classesWithDuplicateFields;
    }

    @Override
    public Strategy getStrategy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
