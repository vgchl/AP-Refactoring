package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.FieldAccessVisitor;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Maik Diepenbroek
 * @since 22-11-2012
 */

public class EncapsulateFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Encapsulate Field";
    private static final String STRATEGY_DESCRIPTION = "There is a public field. Make it private and provide accessors.";
    private List<FieldDeclaration> fieldDeclarations;

    private List<QualifiedName> qualifiedNamesList;
    private Map<FieldDeclaration, List<QualifiedName>> fieldDeclarationFieldAccessHashMap;
    private Logger log = Logger.getLogger(getClass());

    public EncapsulateFieldDetector() {
        fieldDeclarations = new ArrayList<>();
        qualifiedNamesList = new ArrayList<>();
        fieldDeclarationFieldAccessHashMap = new HashMap<>();
    }

    @Override
    public void detectIssues() {
        for (CompilationUnit compilationUnit : compilationUnits) {
            FieldAccessVisitor fieldAccessVisitor = new FieldAccessVisitor();
            compilationUnit.accept(fieldAccessVisitor);
            qualifiedNamesList.addAll(fieldAccessVisitor.getQualifiedNameList());

            FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
            compilationUnit.accept(fieldDeclarationVisitor);
            fieldDeclarations.addAll(fieldDeclarationVisitor.getFieldDeclarations());

            log.info(qualifiedNamesList);
            log.info(fieldDeclarations);

        }

        for (FieldDeclaration declaration : fieldDeclarations) {
            fieldDeclarationFieldAccessHashMap.put(declaration, new ArrayList<QualifiedName>());

//            log.info(fieldDeclarationFieldAccessHashMap.values());
            IBinding binding = ((VariableDeclarationFragment) declaration.fragments().get(0)).resolveBinding();

            for(QualifiedName qualifiedName : qualifiedNamesList){
                log.info("Binding 1: " + binding);
                log.info("Binding 2: " + qualifiedName.resolveBinding());
                if(binding.equals(qualifiedName.resolveBinding())){

                    fieldDeclarationFieldAccessHashMap.get(declaration).add(qualifiedName);
                }
            }



            if (Modifier.isPublic(declaration.getModifiers())) {
                Issue issue = createIssue(declaration);
                issue.getNodes().addAll(fieldDeclarationFieldAccessHashMap.get(declaration));
                log.info(fieldDeclarationFieldAccessHashMap.get(declaration));
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
