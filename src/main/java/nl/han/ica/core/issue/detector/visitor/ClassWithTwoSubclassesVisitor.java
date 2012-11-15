package nl.han.ica.core.issue.detector.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 12-11-12
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class ClassWithTwoSubclassesVisitor extends ASTVisitor {

    private Map<String, List<ASTNode>> subclassesPerSuperClass;

    private Logger logger = Logger.getLogger(getClass().getName());

    public ClassWithTwoSubclassesVisitor() {
        subclassesPerSuperClass = new HashMap<String, List<ASTNode>>();
    }

    @Override
    public boolean visit(TypeDeclaration type) {

        Type superclass = type.getSuperclassType();

        if (superclass != null) {
            ASTNode subClass = type.getParent();
            if (subclassesPerSuperClass.containsKey(superclass.toString())) {
                subclassesPerSuperClass.get(superclass.toString()).add(subClass);
            } else {
                ArrayList<ASTNode> subclasses = new ArrayList<ASTNode>();
                subclasses.add(subClass);
                subclassesPerSuperClass.put(superclass.toString(), subclasses);
            }
        }
        return super.visit(type);
    }

    public void filterTwoOrMoreSubclasses() {

        Set<String> types = subclassesPerSuperClass.keySet();
        Set<String> keysToRemove = new HashSet<String>();


        for (String type : types) {
            if (subclassesPerSuperClass.get(type).size() < 2) {
                keysToRemove.add(type);
            }
        }
        for (String type : keysToRemove) {
            subclassesPerSuperClass.remove(type);
        }
    }

    public Map<String, List<ASTNode>> getSubclassesPerSuperClass() {
        logger.debug("GETMap Test!! "+subclassesPerSuperClass.toString());
        return subclassesPerSuperClass;
    }

    public void clear() {
        this.subclassesPerSuperClass.clear();
    }
}
