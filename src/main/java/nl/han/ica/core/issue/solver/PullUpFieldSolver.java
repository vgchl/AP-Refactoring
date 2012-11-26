package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.PullUpFieldDetector;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.hamcrest.core.IsInstanceOf;
import org.mockito.internal.matchers.InstanceOf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PullUpFieldSolver extends IssueSolver {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof PullUpFieldDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {

        Solution solution = new Solution(issue, this, parameters);
        List<ASTNode> duplicateFields = issue.getNodes();
        log.debug("Print Duplicate fields: " + duplicateFields.toString());

        // TODO: make a delta for every class, not every field that moved? This is only applicable when one class has multiple fields to remove.
        TypeDeclaration superClass = null;
        FieldDeclaration field = null;
        ASTRewrite rewrite = null;

        for (ASTNode node : duplicateFields) {
            if (node instanceof TypeDeclaration){
                superClass = (TypeDeclaration) node;
            }
            else{
                field = (FieldDeclaration) node;

                SourceFile sourceFile = (SourceFile) node.getParent().getRoot().getProperty(SourceFile.SOURCE_FILE_PROPERTY);

                IDocument document = null;

                try {
                    document = sourceFile.toDocument();
                } catch (IOException e) {
                    log.error("Error retrieving Document from SourceFile.", e);
                }

                Delta delta = solution.createDelta(sourceFile);
                delta.setBefore(document.get());

                log.debug("Print subclass Before: " + document.getNumberOfLines());

                rewrite = ASTRewrite.create(node.getAST());


                rewrite.remove(node, null);

                TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());

                try {
                    textEdit.apply(document);
                } catch (MalformedTreeException | BadLocationException e) {
                    log.error("Error removing field declaration.", e);
                }

                log.debug("print Subclass after: " + document.getNumberOfLines());

                delta.setAfter(document.get());

                solution.getDeltas().add(delta);
            }
        }

        solution.getDeltas().add(buildSuperClassDelta(superClass, field, solution));
        // get fields to remove from Issue
        // get containing class from fields
        // get superclass from containing classes

        // add a field to the superclass
        // remove fields from subclasses

        return solution;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Delta buildSuperClassDelta(TypeDeclaration superClass, FieldDeclaration field, Solution solution){
        SourceFile sourceFile = (SourceFile) superClass.getRoot().getProperty(SourceFile.SOURCE_FILE_PROPERTY);
        IDocument document = null;

        try {
            document = sourceFile.toDocument();
        } catch (IOException e) {
            log.error("Error retrieving Document from SourceFile.", e);
        }

        Delta delta = solution.createDelta(sourceFile);
        delta.setBefore(document.get());

        log.debug("Print superclass before: " + document.getNumberOfLines());

        ASTRewrite rewrite = ASTRewrite.create(superClass.getAST());
        ListRewrite listRewrite = rewrite.getListRewrite(superClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(field, null);

        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());
        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            log.fatal("Could not rewrite the AST tree.", e);
        }

        log.debug("Print Superclass after: " + document.getNumberOfLines());

        delta.setAfter(document.get());

        return delta;
    }

    /**
     * Get the superclass for the given FieldDeclaration nodes. So it will get the class the field is in, and get the superclass for that.
     *
     * @param duplicateFields
     * @return
     */
    private ASTNode getSuperClass(List<ASTNode> duplicateFields) {

        TypeDeclaration classWithDuplicateField = (TypeDeclaration) duplicateFields.get(0).getParent();

        /* Get parent to get the class. Getparent on class to get superclass.*/
        // TODO: get superclass type

        return null;
    }
}
