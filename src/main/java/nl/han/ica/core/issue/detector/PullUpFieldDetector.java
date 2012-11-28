package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.ast.visitors.TypeDeclarationVisitor;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

/**
 *
 */
public class PullUpFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Pull up Duplicate Fields.";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    private Logger logger = Logger.getLogger(getClass().getName());

    private TypeDeclarationVisitor visitor;

    public PullUpFieldDetector() {
        visitor = new TypeDeclarationVisitor();
    }

    /**
     * Check the given list of classes if they contain duplicate fields.
     *
     * @param listOfSubclasses
     * @return
     */
    private List<FieldDeclaration> getDuplicateFields(List<TypeDeclaration> listOfSubclasses) {

        FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();

        List<FieldDeclaration> allFieldDeclarations = new ArrayList<FieldDeclaration>();

        List<FieldDeclaration> returnValues = new ArrayList<FieldDeclaration>();

        for (TypeDeclaration node : listOfSubclasses) {

            node.accept(visitor);
        }
        allFieldDeclarations.addAll(visitor.getFieldDeclarations());

        FieldDeclarationSet fieldDeclarationSet = new FieldDeclarationSet();

        logger.debug("Found " + allFieldDeclarations.size() + " field declarations.");
        for (FieldDeclaration fieldDeclaration : allFieldDeclarations) {

            FieldDeclaration duplicate = fieldDeclarationSet.getDuplicate(fieldDeclaration);

            if (duplicate != null) {
                returnValues.add(fieldDeclaration);
                returnValues.add(duplicate);
            }
        }


        return returnValues;
    }

    /**
     * Custom set class so we can check for duplicate FieldDeclarations. This set checks the name (toString) and doesn't use the equals.
     */
    private class FieldDeclarationSet extends HashSet<FieldDeclaration> {

        private Set<FieldDeclaration> fields = new HashSet<FieldDeclaration>();

        @Override
        public boolean add(FieldDeclaration fieldDeclaration) {
            for (FieldDeclaration field : fields) {
                if (field.toString().equals(fieldDeclaration.toString())) {
                    return false;
                }
            }

            return fields.add(fieldDeclaration);
        }

        public FieldDeclaration getDuplicate(FieldDeclaration fieldDeclaration) {
            FieldDeclaration retVal = null;

            for (FieldDeclaration field : fields) {
                if (field.toString().equals(fieldDeclaration.toString())) {
                    return field;
                }
            }

            fields.add(fieldDeclaration);
            return retVal;
        }
    }

    @Override
    public void detectIssues() {

        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
        }

        List<TypeDeclaration> classes = visitor.getTypeDeclarations();
        List<TypeDeclaration> superClasses = new ArrayList<>();
        List<TypeDeclaration> subClasses = new ArrayList<>();
        HashMap<TypeDeclaration, List<TypeDeclaration>> subClassesPerSuperclass = new HashMap<TypeDeclaration, List<TypeDeclaration>>();

        for (TypeDeclaration type : classes) {
            if (type.getSuperclassType() != null) {
                subClasses.add(type);
            } else {
                superClasses.add(type);
            }
        }

        for (TypeDeclaration subClass : subClasses) {
            for (TypeDeclaration superclass : superClasses) {
                if (subClass.resolveBinding().getSuperclass().equals(superclass.resolveBinding())) {
                    if (subClassesPerSuperclass.containsKey(superclass)) {
                        subClassesPerSuperclass.get(superclass).add(subClass);
                    } else {
                        ArrayList<TypeDeclaration> subClassesList = new ArrayList<TypeDeclaration>();
                        subClassesList.add(subClass);
                        subClassesPerSuperclass.put(superclass, subClassesList);
                    }
                }
            }
        }

        for (TypeDeclaration superClass : subClassesPerSuperclass.keySet()) {
            List<FieldDeclaration> duplicateFields = new ArrayList<FieldDeclaration>();

            if (subClassesPerSuperclass.get(superClass).size() > 1) {
                logger.debug("Found more than one subclass.");
                duplicateFields = getDuplicateFields(subClassesPerSuperclass.get(superClass));
            }
            logger.debug("Duplicate fields found: " + duplicateFields.size());

            if (duplicateFields.size() > 0) {
                logger.debug("Duplicate fields found: creating an issue.");
                Issue issue = new Issue(this);

                logger.debug("print duplicate fields: " + duplicateFields.toString());

                ArrayList<ASTNode> nodes = new ArrayList<ASTNode>(duplicateFields);
                /* We also need the superclass at the first position. */
                nodes.add(0, superClass);
                issue.setNodes(nodes);

                issues.add(issue);
            }
        }

        logger.debug(issues.toString());
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
