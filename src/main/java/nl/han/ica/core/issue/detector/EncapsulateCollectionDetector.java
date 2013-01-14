package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.*;

import java.util.Arrays;

public class EncapsulateCollectionDetector extends IssueDetector {

    private static final String STRATEGY_TITLE = "Encapsulate Collection";
    private static final String STRATEGY_DESCRIPTION = "Class contains a public collection. Make it private and provide collection accessors.";
    private static final String[] COLLECTION_TYPES = { "Collection", "List", "ArrayList", "Set", "HashSet", "Queue" };

    @Override
    public void detectIssues() {
        FieldDeclarationVisitor fieldVisitor = new FieldDeclarationVisitor();
        for (CompilationUnit compilationUnit : compilationUnits) {
            compilationUnit.accept(fieldVisitor);
        }

        for (final FieldDeclaration fieldDeclaration : fieldVisitor.getFieldDeclarations()) {
            if (Modifier.isPublic(fieldDeclaration.getModifiers()) && isCollectionType(fieldDeclaration.getType())) {
                logger.debug("Found collection type: " + fieldDeclaration);
                final Issue issue = createIssue(fieldDeclaration);

                ASTVisitor referenceVisitor = new ASTVisitor() {
                    @Override
                    public boolean visit(QualifiedName node) {
                        for (Object fragment : fieldDeclaration.fragments()) {
                            if (fragment instanceof VariableDeclarationFragment) {
                                VariableDeclarationFragment variable = (VariableDeclarationFragment) fragment;
                                if (variable.resolveBinding().equals(node.resolveBinding())) {
                                    issue.getNodes().add(node);
                                    System.out.println("Adding node to issue " + node);
                                }
                            }
                        }
                        return super.visit(node);
                    }
                };

                for (CompilationUnit compilationUnit : compilationUnits) {
                    compilationUnit.accept(referenceVisitor);
                }
            }
        }
    }

    private boolean isCollectionType(Type type) {
        if (type.isParameterizedType()) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Name name = ((SimpleType) parameterizedType.getType()).getName();
            return Arrays.asList(COLLECTION_TYPES).contains(name.getFullyQualifiedName());
        }
        return false;
    }

    @Override
    public String getTitle() {
        return STRATEGY_TITLE;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }

}
