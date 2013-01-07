package nl.han.ica.core.issue.solver.magicliteral;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.util.ASTUtil;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.IOException;
import java.util.Map;

public abstract class LiteralSolutionBuilder {

    protected Issue issue;
    private IssueSolver issueSolver;
    protected Map<String, Parameter> parameters;
    private ASTRewrite rewrite;
    protected ASTNode literal;
    private TypeDeclaration literalClass;
    private Logger logger;
    protected String parameterConstantName;

    /**
     * Constructor to set Issue, solver, parameters and a default name for the
     * new constant.
     *
     * @param issue
     * @param issueSolver
     * @param parameters
     * @param parameterConstantName
     */
    public LiteralSolutionBuilder(Issue issue, IssueSolver issueSolver,
                                  Map<String, Parameter> parameters, String parameterConstantName) {
        logger = Logger.getLogger(getClass());

        this.issue = issue;
        this.issueSolver = issueSolver;
        this.parameters = parameters;
        this.parameterConstantName = parameterConstantName;

        //TODO: Get the magic literal from the issue
        //HINT: literal = "...";

        //TODO: Get the class from the literal, we do this so that we can rewrite this class later.
        //HINT: literalClass = ASTUtil.parent(..., ....);

        //TODO: create a rewriter using the literalClass
        //HINT: rewrite = "...";
    }

    /**
     * Create a Solution. This method creates a constant if it doesn't already
     * exist.
     *
     * @return
     */
    public Solution build() {
        String name = getNameForConstant();
        if (!existingConstantExists(name)) {
            createConstant(name, getValueForConstant());
        }
        replaceMagicLiteralWithConstant(name);
        return buildSolution();
    }

    protected String getNameForConstant() {
        return (String) parameters.get(parameterConstantName).getValue();
    }

    protected abstract String getValueForConstant();

    protected abstract Expression getInitializerExpression(final String value, AST ast);

    protected abstract Type getType(AST ast);

    protected boolean existingConstantExists(final String name) {
        FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
        literalClass.accept(visitor);
        return visitor.hasFieldName(name);
    }

    @SuppressWarnings("unchecked")
    protected void createConstant(final String name, final String value) {
        AST ast = literalClass.getAST();

        //TODO: Create a ListRewrite, make sure to pass the class of the literal.
        //Hint: use rewrite to create the ListRewrite

        //TODO: Create a variable declaration fragment
        //Hint: Use ast.new... for the creation of new ast objects
        //Hint: Look at the number literal solution builder!

        //TODO: Using the variable, create a field declaration
        //Hint: make sure to set the type and mofifiers

        //TODO: insert the field in the list
        //Hint: texteditgroup == null
    }

    /**
     * Create a constant with the given name and replace the magic literal with
     * a reference to the constant.
     *
     * @param name
     */
    protected void replaceMagicLiteralWithConstant(final String name) {
        //TODO: Replace literal with name
        //Hint: use rewrite
    }

    /**
     * Create a new Solution and set it's Delta's. This method is called by the
     * build method.
     *
     * @return
     */
    protected Solution buildSolution() {
        SourceFile sourceFile = (SourceFile) literalClass.getRoot().getProperty(SourceFile.SOURCE_FILE_PROPERTY);
        Solution solution = new Solution(issue, issueSolver, parameters);
        IDocument document = null;
        try {
            document = sourceFile.toDocument();
        } catch (IOException e) {
            logger.fatal("Could not read the source file.", e);
        }

        Delta delta = solution.createDelta(sourceFile);
        delta.setBefore(document.get());

        TextEdit textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());
        try {
            textEdit.apply(document);
        } catch (MalformedTreeException | BadLocationException e) {
            logger.fatal("Could not rewrite the AST tree.", e);

        }

        delta.setAfter(document.get());
        return solution;
    }
}
