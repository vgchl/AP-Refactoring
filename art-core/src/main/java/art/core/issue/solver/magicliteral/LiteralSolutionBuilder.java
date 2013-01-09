package art.core.issue.solver.magicliteral;

import art.core.SourceFile;
import art.core.visitors.FieldDeclarationVisitor;
import art.core.issue.Issue;
import art.core.issue.IssueSolver;
import art.core.solution.Delta;
import art.core.solution.Parameter;
import art.core.solution.Solution;
import art.core.util.ASTUtil;
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

        literal = issue.getNodes().get(0);
        literalClass = ASTUtil.parent(TypeDeclaration.class, literal);
        rewrite = ASTRewrite.create(literalClass.getAST());
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

        ListRewrite listRewrite = rewrite.getListRewrite(literalClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        VariableDeclarationFragment variable = ast.newVariableDeclarationFragment();
        variable.setName(ast.newSimpleName(name));
        variable.setInitializer(getInitializerExpression(value, ast));

        FieldDeclaration field = ast.newFieldDeclaration(variable);
        field.setType(getType(ast));
        field.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL));

        listRewrite.insertFirst(field, null);
    }

    /**
     * Create a constant with the given name and replace the magic literal with
     * a reference to the constant.
     *
     * @param name
     */
    protected void replaceMagicLiteralWithConstant(final String name) {
        SimpleName constantReference = literal.getAST().newSimpleName(name);
        rewrite.replace(literal, constantReference, null);
    }

    /**
     * Create a new Solution and set it's Detla's. This method is called by the
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
