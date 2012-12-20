package nl.han.ica.core.issue.solver.magicliteral;

import java.io.IOException;
import java.util.Map;

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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public abstract class LiteralSolutionBuilder {
	protected Issue issue;
	private IssueSolver issueSolver;
	protected Map<String, Parameter> parameters;
	private ASTRewrite rewrite;
	protected ASTNode literal;
	private TypeDeclaration literalClass;
	private Logger logger;
	protected String parameterConstantName;

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

	public abstract Solution build();

	protected boolean existingConstantExists(final String name) {
		FieldDeclarationVisitor visitor = new FieldDeclarationVisitor();
		literalClass.accept(visitor);
		return visitor.hasFieldName(name);
	}

	@SuppressWarnings("unchecked")
	protected void createConstant(final String name, final String value) {
		AST ast = literalClass.getAST();

		ListRewrite listRewrite = rewrite.getListRewrite(literalClass,
				TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		VariableDeclarationFragment variable = ast
				.newVariableDeclarationFragment();
		variable.setName(ast.newSimpleName(name));

		variable.setInitializer(getInitializerExpression(value, ast));

		FieldDeclaration field = ast.newFieldDeclaration(variable);
		field.setType(getType(ast));
		field.modifiers().addAll(
				ast.newModifiers(Modifier.PRIVATE | Modifier.STATIC
						| Modifier.FINAL));

		listRewrite.insertFirst(field, null);
	}

	protected abstract Expression getInitializerExpression(
			final String value, AST ast);

	protected abstract Type getType(AST ast);

	protected void replaceMagicLiteralWithConstant(final String name) {
		SimpleName constantReference = literal.getAST().newSimpleName(name);
		rewrite.replace(literal, constantReference, null);
	}

	protected Solution buildSolution() {
		SourceFile sourceFile = (SourceFile) literalClass.getRoot()
				.getProperty(SourceFile.SOURCE_FILE_PROPERTY);
		Solution solution = new Solution(issue, issueSolver, parameters);
		IDocument document = null;
		try {
			document = sourceFile.toDocument();
		} catch (IOException e) {
			logger.fatal("Could not read the source file.", e);
		}

		Delta delta = solution.createDelta(sourceFile);
		delta.setBefore(document.get());

		TextEdit textEdit = rewrite.rewriteAST(document,
				JavaCore.getOptions());
		try {
			textEdit.apply(document);
		} catch (MalformedTreeException | BadLocationException e) {
			logger.fatal("Could not rewrite the AST tree.", e);
		}

		delta.setAfter(document.get());

		return solution;
	}

}
