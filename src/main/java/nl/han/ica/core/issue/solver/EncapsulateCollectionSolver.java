package nl.han.ica.core.issue.solver;

import nl.han.ica.core.Parameter;
import nl.han.ica.core.Solution;
import nl.han.ica.core.SourceFile;
import nl.han.ica.core.ast.builders.AccessorBuilder;
import nl.han.ica.core.issue.Issue;
import nl.han.ica.core.issue.IssueSolver;
import nl.han.ica.core.issue.detector.EncapsulateCollectionDetector;
import nl.han.ica.core.util.ASTUtil;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.Map;
import java.util.Set;

public class EncapsulateCollectionSolver extends IssueSolver {

    @Override
    public boolean canSolve(Issue issue) {
        return issue.getDetector() instanceof EncapsulateCollectionDetector;
    }

    @Override
    protected Solution internalSolve(Issue issue, Map<String, Parameter> parameters, Set<SourceFile> sourceFiles) {
        SolutionBuilder solutionBuilder = new SolutionBuilder(issue, parameters, this, sourceFiles);
        return solutionBuilder.build();
    }

    private class SolutionBuilder {

        private Issue issue;
        private Map<String, Parameter> parameters;
        private IssueSolver solver;
        private Set<SourceFile> sourceFiles;

        private AST ast;
        private FieldDeclaration field;
        private ASTRewrite rewrite;
        private Solution solution;

        private SolutionBuilder(Issue issue, Map<String, Parameter> parameters, IssueSolver solver, Set<SourceFile> sourceFiles) {
            this.issue = issue;
            this.parameters = parameters;
            this.solver = solver;
            this.sourceFiles = sourceFiles;

            field = (FieldDeclaration) issue.getNodes().get(0);
            ast = field.getAST();
            rewrite = rewriteFor(ast);
        }

        public Solution build() {
            solution = new Solution(issue, solver, parameters);
            solution.getDeltas().add(createDelta(field, rewrite));

            reduceFieldVisibility();
            createAccessors();

            return solution;
        }

        private void reduceFieldVisibility() {
            ASTUtil.setVisibility(field, Modifier.ModifierKeyword.PRIVATE_KEYWORD, rewrite);
        }

        private void createAccessors() {
            MethodDeclaration method = ast.newMethodDeclaration();
            method.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
            method.setReturnType2(copyFieldType());

            for (Object fragment : field.fragments()) {
                if (fragment instanceof VariableDeclarationFragment) {
                    VariableDeclarationFragment variable = (VariableDeclarationFragment) fragment;
                    MethodDeclaration getter = createGetter(field, variable);
                    addMethod(getter);
//                    MethodDeclaration setter = createSetter(field, variable);
                    MethodDeclaration adder = createAdder(field, variable);
//                    MethodDeclaration deleter = createDeleter(field, variable);
                    replaceReferences(variable, getter);
                }
            }
        }

        private void replaceReferences(final VariableDeclarationFragment variable, final MethodDeclaration adder) {
            for (SourceFile sourceFile : sourceFiles) {
                sourceFile.getCompilationUnit().accept(new ASTVisitor() {

                    @Override
                    public boolean visit(QualifiedName node) {
                        if (variable.resolveBinding().equals(node.resolveBinding())) {
                            if (node.getParent() instanceof MethodInvocation) {
                                MethodInvocation invocation = (MethodInvocation) node.getParent();
                                switch (invocation.getName().getIdentifier()) {
                                    case "add":
                                        replaceReference(invocation, adder);
                                        break;
                                    default:
                                        System.out.println("Unsupported method: " + invocation.getName().getIdentifier());
                                }
                            }
                        }
                        return super.visit(node);
                    }

                });
            }
        }

        private void replaceReference(MethodInvocation invocation, MethodDeclaration method) {
            Expression expression = invocation.getExpression();
            if (expression instanceof QualifiedName) {
                ASTRewrite invocationRewrite = rewriteFor(invocation.getAST());
                QualifiedName qualifiedName = (QualifiedName) expression;
                invocationRewrite.set(invocation, MethodInvocation.EXPRESSION_PROPERTY, qualifiedName.getQualifier(), null);
                invocationRewrite.set(invocation, MethodInvocation.NAME_PROPERTY, ASTNode.copySubtree(invocation.getAST(), method.getName()), null);
                solution.getDeltas().add(createDelta(invocation, invocationRewrite));
            } else {
                throw new UnsupportedOperationException("No implementation for node type: " + expression.getClass().getName());
            }
        }

        private MethodDeclaration createGetter(FieldDeclaration field, VariableDeclarationFragment variable) {
            AccessorBuilder builder = new AccessorBuilder(field, variable);
            MethodDeclaration getter = builder.buildGetter();
            return getter;
        }

        private MethodDeclaration createAdder(FieldDeclaration field, VariableDeclarationFragment variable) {
            AccessorBuilder builder = new AccessorBuilder(field, variable);
            MethodDeclaration adder = builder.buildCollectionAdder();
            return adder;
        }

        private void addMethod(MethodDeclaration method) {
            ListRewrite methods = rewrite.getListRewrite(ASTUtil.parent(TypeDeclaration.class, field), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
            methods.insertLast(method, null);
        }

        private Type copyFieldType() {
            return (Type) Type.copySubtree(ast, field.getType());
        }

    }

}
