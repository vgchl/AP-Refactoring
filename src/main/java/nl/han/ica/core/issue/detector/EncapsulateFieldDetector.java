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
            if(!fieldAccessVisitor.getQualifiedNameList().isEmpty()){
                qualifiedNamesList.addAll(fieldAccessVisitor.getQualifiedNameList());
            }

            FieldDeclarationVisitor fieldDeclarationVisitor = new FieldDeclarationVisitor();
            compilationUnit.accept(fieldDeclarationVisitor);
            if(!fieldDeclarationVisitor.getFieldDeclarations().isEmpty()){
                fieldDeclarations.addAll(fieldDeclarationVisitor.getFieldDeclarations());
            }

            log.info(qualifiedNamesList);
            log.info(fieldDeclarations);

        }


        for(QualifiedName qualifiedName : qualifiedNamesList){
            for(FieldDeclaration declaration : fieldDeclarations){
                if(!fieldDeclarationFieldAccessHashMap.containsKey(declaration)){
                    fieldDeclarationFieldAccessHashMap.put(declaration, new ArrayList<QualifiedName>());
                }
                IBinding binding = ((VariableDeclarationFragment) declaration.fragments().get(0)).resolveBinding();
                log.fatal("Declaration " + declaration);
                log.fatal("Declaration binding " + ((VariableDeclarationFragment) declaration.fragments().get(0)).resolveBinding());
                log.fatal("Qualified Name " + qualifiedName);
                log.fatal("Qualified Name binding " + qualifiedName.resolveBinding());
                if(binding.equals(qualifiedName.resolveBinding())){
                    fieldDeclarationFieldAccessHashMap.get(declaration).add(qualifiedName);
                    break;
                }
            }
        }

        for (FieldDeclaration declaration : fieldDeclarationFieldAccessHashMap.keySet()){
            if (Modifier.isPublic(declaration.getModifiers())) {
                Issue issue = createIssue(declaration);
                issue.getNodes().addAll(fieldDeclarationFieldAccessHashMap.get(declaration));
                log.info(fieldDeclarationFieldAccessHashMap.get(declaration));
            }
        }


        /*for (FieldDeclaration declaration : fieldDeclarations) {


            IBinding binding = ((VariableDeclarationFragment) declaration.fragments().get(0)).resolveBinding();


            if (Modifier.isPublic(declaration.getModifiers())) {
                Issue issue = createIssue(declaration);
                issue.getNodes().addAll(fieldDeclarationFieldAccessHashMap.get(declaration));
                log.info(fieldDeclarationFieldAccessHashMap.get(declaration));
            }
        }*/

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
