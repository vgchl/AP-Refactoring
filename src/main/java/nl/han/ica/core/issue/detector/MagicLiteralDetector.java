/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.han.ica.core.issue.detector;

import nl.han.ica.core.issue.IssueDetector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

import java.util.HashSet;
import java.util.Set;

public class MagicLiteralDetector extends IssueDetector {

	@Override
	public String getTitle() {
		return "Magic Literal";
	}

	@Override
	public String getDescription() {
		return "Avoid using literals in conditional statements.";
	}

	@Override
	public void detectIssues() {
		for (CompilationUnit compilationUnit : compilationUnits) {
			MagicLiteralVisitor magicLiteralVisitor = new MagicLiteralVisitor();
			compilationUnit.accept(magicLiteralVisitor);
			createIssues(magicLiteralVisitor.getMagicLiterals());
		}
	}

	private class MagicLiteralVisitor extends ASTVisitor {
		private Set<ASTNode> magicLiterals;

		public MagicLiteralVisitor() {
			magicLiterals = new HashSet<>();
		}

		@Override
		public boolean visit(NumberLiteral node) {
			addLiteralNode(node);

			return super.visit(node);
		}

		@Override
		public boolean visit(StringLiteral node) {
			addLiteralNode(node);

			return super.visit(node);
		}
		
		@Override
		public boolean visit(CharacterLiteral node) {
			
			addLiteralNode(node);
			
			return super.visit(node);
		}
		
		private void addLiteralNode(ASTNode node) {
			if (node.getParent().getNodeType() != ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
				magicLiterals.add(node);
			}
		}

		public Set<ASTNode> getMagicLiterals() {
			return magicLiterals;
		}
	}
}
