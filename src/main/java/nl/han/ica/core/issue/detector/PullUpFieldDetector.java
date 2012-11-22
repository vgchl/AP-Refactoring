package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import nl.han.ica.core.issue.detector.visitor.TypeDeclarationVisitor;
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
    private Set<FieldDeclaration> getDuplicateFields(List<TypeDeclaration> listOfSubclasses) {

        FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();

        List<FieldDeclaration> allFieldDeclarations = new ArrayList<FieldDeclaration>();

        Set<FieldDeclaration> returnValues = new HashSet<FieldDeclaration>();

        for (TypeDeclaration node : listOfSubclasses) {

            node.accept(visitor);
            allFieldDeclarations.addAll(visitor.getFieldDeclarations());
        }

        for (int i = 0; i < allFieldDeclarations.size(); i++) {

            FieldDeclaration field = allFieldDeclarations.get(i);

            for (int j = i; j < allFieldDeclarations.size(); j++) {

                FieldDeclaration anotherField = allFieldDeclarations.get(j);
                if (field.toString().equals(anotherField.toString())) {
                    logger.debug(field.toString() + " " + anotherField.toString());
                    returnValues.add(field);
                    returnValues.add(anotherField);
                }
            }
        }


        return returnValues;
    }

    @Override
    public void detectIssues() {

        for (CompilationUnit unit : compilationUnits) {
            unit.accept(visitor);
        }

        List<TypeDeclaration> classes = visitor.getTypeDeclarations();
        List<TypeDeclaration> superClasses = new ArrayList<>();
        logger.debug(classes.toString());
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

            Set<FieldDeclaration> duplicateFields = new HashSet<>();

            for (TypeDeclaration superClass : subClassesPerSuperclass.keySet()) {
                if (subClassesPerSuperclass.get(superClass).size() > 1) {
                    duplicateFields = getDuplicateFields(subClassesPerSuperclass.get(superClass));
                }
                logger.debug("Duplicate fields found: " + duplicateFields.size());

                if (duplicateFields.size() > 0) {
                    logger.debug("Duplicate fields found: creating an issue.");
                    Issue issue = new Issue(this);

                    logger.debug("print duplicate fields: " + duplicateFields.toString());

                    issue.setNodes(new ArrayList<ASTNode>(duplicateFields));

                    issues.add(issue);
                }
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
