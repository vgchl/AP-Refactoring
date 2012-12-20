package nl.han.ica.core.issue.detector;

import nl.han.ica.core.ast.visitors.TypeDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import org.apache.log4j.Logger;

// TODO: Check whether field can be moved up (parent class shouldn't have a field with the same type and name).
public class PullUpFieldDetector extends IssueDetector {

    private static final String STRATEGY_NAME = "Pull up Duplicate Fields";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when they can be placed in a shared parent class.";

    private Map<ITypeBinding, TypeDeclaration> typeDeclarations;
    private Map<ITypeBinding, Node> typeNodes;
    private Set<Node> rootTypeNodes;
    private Logger logger = Logger.getLogger(getClass());

    public PullUpFieldDetector() {
        typeDeclarations = new HashMap<>();
        typeNodes = new HashMap<>();
        rootTypeNodes = new HashSet<>();
    }

    @Override
    public void detectIssues() {
        reset();
        collectTypeDeclarations();
        buildTypeHierarchy();

        for (Node node : rootTypeNodes) {
            Queue<ITypeBinding> remainingTypes = new LinkedList<>();
            Set<ITypeBinding> allChildTypes = node.getAllChildTypes();
            remainingTypes.addAll(allChildTypes);

            while (!remainingTypes.isEmpty()) {
                ITypeBinding typeA = remainingTypes.poll();
                for (ITypeBinding typeB : remainingTypes) {
                    if (areSiblings(typeA, typeB)) {
                        detectDuplicateFields(typeA, typeB);
                    }
                }
            }
        }
    }

    private void collectTypeDeclarations() {
        TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
        for (CompilationUnit compilationUnit : compilationUnits) {
            compilationUnit.accept(visitor);
        }
        for (TypeDeclaration typeDeclaration : visitor.getTypeDeclarations()) {
            typeDeclarations.put(typeDeclaration.resolveBinding(), typeDeclaration);
        }
    }

    private void buildTypeHierarchy() {
        for (TypeDeclaration typeDeclaration : typeDeclarations.values()) {
            ITypeBinding typeBinding = typeDeclaration.resolveBinding();
            Type superclassType = typeDeclaration.getSuperclassType();
            ITypeBinding supertypeBinding = null;
            if (null != superclassType) {
                supertypeBinding = superclassType.resolveBinding();
            }
            Node node = findOrCreateNode(typeBinding);
            if (null != superclassType && null != supertypeBinding) {
                Node superNode = findOrCreateNode(supertypeBinding);
                superNode.addChild(node);
            } else {
                rootTypeNodes.add(node);
            }
        }
    }

    private void detectDuplicateFields(ITypeBinding typeA, ITypeBinding typeB) {        
        for (FieldDeclaration fieldA : typeDeclarations.get(typeA).getFields()) {
            for (FieldDeclaration fieldB : typeDeclarations.get(typeB).getFields()) {
                if (fieldA.getType().resolveBinding() == fieldB.getType().resolveBinding()) {
                    detectDuplicateVariables(typeA, typeB, fieldA, fieldB);
                }
            }
        }
    }

    private void detectDuplicateVariables(ITypeBinding typeA, ITypeBinding typeB, FieldDeclaration fieldA, FieldDeclaration fieldB) {
        for (Object fragmentA : fieldA.fragments()) {
            VariableDeclarationFragment variableA = (VariableDeclarationFragment) fragmentA;
            for (Object fragmentB : fieldB.fragments()) {
                VariableDeclarationFragment variableB = (VariableDeclarationFragment) fragmentB;
                if (variableA.getName().getIdentifier().equals(variableB.getName().getIdentifier())) {
                    ITypeBinding parent = findNearestParent(typeA, typeB);
                    if (null != parent) {
                        createIssue(variableA, variableB, typeDeclarations.get(parent));
                    }
                }
            }
        }
    }

    private boolean areSiblings(ITypeBinding typeA, ITypeBinding typeB) {
        Set<ITypeBinding> childTypesA = typeNodes.get(typeA).getAllChildTypes();
        Set<ITypeBinding> childTypesB = typeNodes.get(typeB).getAllChildTypes();
        return !(childTypesA.contains(typeB) || childTypesB.contains(typeA));
    }

    private ITypeBinding findNearestParent(ITypeBinding typeA, ITypeBinding typeB) {
        Set<ITypeBinding> parentsA = typeNodes.get(typeA).getAllParentTypes();
        Node parent = typeNodes.get(typeB).getParent();
        while (null != parent) {
            if (parentsA.contains(parent.getTypeBinding())) {
                break;
            }
            parent = parent.getParent();
        }
        return (null != parent) ? parent.getTypeBinding() : null;
    }

    private void createIssue(VariableDeclarationFragment variableA, VariableDeclarationFragment variableB, TypeDeclaration parentTypeDeclaration) {
        Issue issue = createIssue();
        issue.getNodes().add(variableA);
        issue.getNodes().add(variableB);
        issue.getNodes().add(parentTypeDeclaration);
        issues.add(issue);
    }

    private Node findOrCreateNode(ITypeBinding typeBinding) {
        Node superNode;
        if (!typeNodes.containsKey(typeBinding)) {
            superNode = new Node(typeBinding);
            typeNodes.put(typeBinding, superNode);
        } else {
            superNode = typeNodes.get(typeBinding);
        }
        return superNode;
    }

    @Override
    public void reset() {
        typeDeclarations.clear();
        super.reset();
    }

    @Override
    public String getTitle() {
        return STRATEGY_NAME;
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;
    }

    private class Node {

        private ITypeBinding typeBinding;
        private Node parent;
        private Set<Node> childNodes;

        public Node(ITypeBinding typeBinding) {
            childNodes = new HashSet<>();
            this.typeBinding = typeBinding;
        }

        public void addChild(Node node) {
            node.setParent(this);
            childNodes.add(node);
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getParent() {
            return parent;
        }

        public ITypeBinding getTypeBinding() {
            return typeBinding;
        }

        public Set<Node> getChildNodes() {
            return Collections.unmodifiableSet(childNodes);
        }

        public Set<ITypeBinding> getAllChildTypes() {
            Set<Node> nodes = new HashSet<>(childNodes);
            Queue<Node> remainingNodes = new LinkedList<>(childNodes);
            while (!remainingNodes.isEmpty()) {
                Node childNode = remainingNodes.poll();
                nodes.add(childNode);
                remainingNodes.addAll(childNode.getChildNodes());
            }
            Set<ITypeBinding> typeBindings = new HashSet<>();
            for (Node node : nodes) {
                if(node.getTypeBinding() != null){
                    typeBindings.add(node.getTypeBinding());
                }
            }
            return typeBindings;
        }

        public Set<ITypeBinding> getAllParentTypes() {
            Set<ITypeBinding> types = new HashSet<>();
            Node tempParent = getParent();
            while (null != tempParent) {
                if(tempParent.getTypeBinding() != null){
                    types.add(tempParent.getTypeBinding());
                }
                tempParent = tempParent.getParent();
            }
            return types;
        }

    }

}
