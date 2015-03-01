package ch.unibe.scg.nullfinder.classifier;

import java.util.ArrayList;
import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;

public class LocalVariableNameNullCheckClassifier extends
		AbstractNameNullCheckClassifier {

	@Override
	public boolean accepts(NullCheck check) {
		return super.accepts(check)
				&& this.checksNullAgainstLocalVariable(check.getNode());
	}

	private boolean checksNullAgainstLocalVariable(NullLiteralExpr node) {
		assert node.getParentNode() instanceof BinaryExpr;
		assert node.getParentNode().getChildrenNodes().stream()
				.anyMatch(candidate -> candidate instanceof NameExpr);
		BinaryExpr binary = (BinaryExpr) node.getParentNode();
		NameExpr suspect = (NameExpr) ((binary.getLeft() instanceof NameExpr) ? binary
				.getLeft() : binary.getRight());
		Node current = binary;
		Node stop = node;
		// haha, a null check!
		while (current != null) {
			List<Node> children = current.getChildrenNodes();
			if (current instanceof ForStmt) {
				// reorder children
				ForStmt forStatement = (ForStmt) current;
				children = new ArrayList<>();
				children.addAll(forStatement.getInit());
				children.add(forStatement.getCompare());
				children.addAll(forStatement.getUpdate());
				children.add(forStatement.getBody());
			}
			for (Node child : children) {
				if (child == stop) {
					break;
				}
				if (child instanceof VariableDeclarationExpr) {
					if (this.declares((VariableDeclarationExpr) child,
							suspect.getName())) {
						return true;
					}
				} else if (child instanceof ExpressionStmt) {
					Expression expression = ((ExpressionStmt) child)
							.getExpression();
					if (expression instanceof VariableDeclarationExpr) {
						if (this.declares((VariableDeclarationExpr) expression,
								suspect.getName())) {
							return true;
						}
					} else if (expression instanceof AssignExpr) {
						AssignExpr assign = (AssignExpr) expression;
						if (assign.getTarget() instanceof NameExpr) {
							NameExpr name = (NameExpr) assign.getTarget();
							if (suspect.getName().equals(name.getName())) {
								return true;
							}
						}
					}
				}
			}
			stop = current;
			current = current.getParentNode();
		}
		return false;
	}

	private boolean declares(VariableDeclarationExpr declaration, String name) {
		for (VariableDeclarator variable : declaration.getVars()) {
			if (name.equals(variable.getId().getName())) {
				return true;
			}
		}
		return false;
	}

}
