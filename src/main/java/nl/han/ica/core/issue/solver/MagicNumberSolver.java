package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Delta;
import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.ast.visitors.FieldDeclarationVisitor;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.MagicNumberDetector;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Solver for the Replace Magic Number with Constant violation.
 */
public class MagicNumberSolver extends IssueSolver {

    private static final String PARAMETER_CONSTANT_NAME = "Constant name";

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof MagicNumberDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters) {
        SolutionBuilder solutionBuilder = new SolutionBuilder(issue, this, parameters);
        return solutionBuilder.build();
    }

    @Override
    protected Map<String, Parameter> defaultParameters() {
        Map<String, Parameter> parameters = new HashMap<>();
        Parameter constantNameParameter = new Parameter(PARAMETER_CONSTANT_NAME, "CONSTANT_NAME");
        constantNameParameter.getConstraints().add(new Parameter.Constraint() {
            @Override
            public boolean isValid(Object value) {
                return ((String) value).matches("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
            }
        });
        parameters.put(PARAMETER_CONSTANT_NAME, constantNameParameter);
        return parameters;
    }

    protected class SolutionBuilder {
        private Issue issue;
        private IssueSolver issueSolver;
        private Map<String, Parameter> parameters;
        private ASTRewrite rewrite;
        private NumberLiteral literal;
        private TypeDeclaration literalClass;
        private Logger logger;

        public SolutionBuilder(Issue issue, IssueSolver issueSolver, Map<String, Parameter> parameters) {
            logger = Logger.getLogger(getClass());

            this.issue = issue;
            this.issueSolver = issueSolver;
            this.parameters = parameters;
        }

        public Solution build() {
            literal = (NumberLiteral) issue.getNodes().get(0);
            literalClass = ASTUtil.parent(TypeDeclaration.class, literal);
            rewrite = ASTRewrite.create(literalClass.getAST());

            String name = (String) parameters.get(PARAMETER_CONSTANT_NAME).getValue();
            String value = literal.getToken();

            if (!existingConstantExists(name)) {
                createConstant(name, value);
            }
            replaceMagicNumberWithConstant(name);
            return buildSolution();
        }

        private boolean existingConstantExists(final String name) {
            FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
            literalClass.accept(visitor);
            return visitor.hasFieldName(name);
        }

        private void createConstant(final String name, final String value) {
            AST ast = literalClass.getAST();

            ListRewrite listRewrite = rewrite.getListRewrite(literalClass, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
            VariableDeclarationFragment variable = ast.newVariableDeclarationFragment();
            variable.setName(ast.newSimpleName(name));
            variable.setInitializer(ast.newNumberLiteral(value));

            FieldDeclaration field = ast.newFieldDeclaration(variable);
            field.setType(ast.newPrimitiveType(PrimitiveType.INT));
            field.modifiers().addAll(ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL));

            listRewrite.insertFirst(field, null);
	}

        protected void replaceMagicNumberWithConstant(final String name) {
            SimpleName constantReference = literal.getAST().newSimpleName(name);
            rewrite.replace(literal, constantReference, null);
        }

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

}
