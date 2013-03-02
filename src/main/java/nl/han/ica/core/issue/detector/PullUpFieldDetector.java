/**
 * Copyright 2013 
 * 
 * HAN University of Applied Sciences
 * Maik Diepenbroek
 * Wouter Konecny
 * Sjoerd van den Top
 * Teun van Vegchel
 * Niek Versteege
 *
 * See the file MIT-license.txt for copying permission.
 */


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
