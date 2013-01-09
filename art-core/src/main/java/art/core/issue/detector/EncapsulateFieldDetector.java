package art.core.issue.detector;

import art.core.Context;
import art.core.SourceFile;
import art.core.visitors.FieldAccessVisitor;
import art.core.visitors.FieldDeclarationVisitor;
import art.core.issue.Issue;
import art.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncapsulateFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Encapsulate Field";
    private static final String STRATEGY_DESCRIPTION = "There is a public field. Make it private and provide accessors.";
    private List<FieldDeclaration> fieldDeclarations;

    private List<QualifiedName> qualifiedNamesList;
    private Map<FieldDeclaration, List<QualifiedName>> fieldDeclarationFieldAccessHashMap;

    public EncapsulateFieldDetector() {
        fieldDeclarations = new ArrayList<>();
        qualifiedNamesList = new ArrayList<>();
        fieldDeclarationFieldAccessHashMap = new HashMap<>();
    }

    @Override
    public void internalDetectIssues(Context context) {
        //TODO REFACTOR, Because not to combine all fields with his qualifiednames yet. 
        for (SourceFile sourceFile : context.getSourceFiles()) { // TODO: Rewrite with context.visit(ASTVisitor)
            FieldAccessVisitor fieldAccessVisitor = new FieldAccessVisitor();
            sourceFile.getCompilationUnit().accept(fieldAccessVisitor);
            if (!fieldAccessVisitor.getQualifiedNameList().isEmpty()) {
                qualifiedNamesList.addAll(fieldAccessVisitor.getQualifiedNameList());
            }

            FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
            sourceFile.getCompilationUnit().accept(fieldDeclarationVisitor);
            if (!fieldDeclarationVisitor.getFieldDeclarations().isEmpty()) {
                fieldDeclarations.addAll(fieldDeclarationVisitor.getFieldDeclarations());
            }
        }

        for (QualifiedName qualifiedName : qualifiedNamesList) {
            for (FieldDeclaration declaration : fieldDeclarations) {
                if (!fieldDeclarationFieldAccessHashMap.containsKey(declaration)) {
                    fieldDeclarationFieldAccessHashMap.put(declaration, new ArrayList<QualifiedName>());
                }
                IBinding binding = ((VariableDeclarationFragment) declaration.fragments().get(0)).resolveBinding();
                if (binding.equals(qualifiedName.resolveBinding())) {
                    fieldDeclarationFieldAccessHashMap.get(declaration).add(qualifiedName);
                    break;
                }
            }
        }

        findIssues();
    }
    
    private void findIssues(){
        for (FieldDeclaration declaration : fieldDeclarationFieldAccessHashMap.keySet()){
            if (Modifier.isPublic(declaration.getModifiers()) && !Modifier.isStatic(declaration.getModifiers())) {
                Issue issue = createIssue(declaration);
                issue.getNodes().addAll(fieldDeclarationFieldAccessHashMap.get(declaration));
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
