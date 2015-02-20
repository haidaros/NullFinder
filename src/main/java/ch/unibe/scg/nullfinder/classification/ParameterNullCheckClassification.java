package ch.unibe.scg.nullfinder.classification;

import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;

public class ParameterNullCheckClassification extends
		AbstractNameNullCheckClassification {

	public ParameterNullCheckClassification(NullCheck check) {
		super(check);
	}

	@Override
	public boolean accepts(NullCheck check) {
		return super.accepts(check)
				&& this.checksNullAgainstParameter(check.getNode());
	}

	private boolean checksNullAgainstParameter(NullLiteralExpr node) {
		assert node.getParentNode() instanceof BinaryExpr;
		assert node.getParentNode().getChildrenNodes().stream()
				.anyMatch(candidate -> candidate instanceof NameExpr);
		BinaryExpr binary = (BinaryExpr) node.getParentNode();
		NameExpr suspect = (NameExpr) ((binary.getLeft() instanceof NameExpr) ? binary
				.getLeft() : binary.getRight());
		Node current = binary;
		// haha, a null check!
		while (current != null) {
			if (current instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) current;
				// parameters are null for methods taking no arguments
				if (method.getParameters() != null
						&& this.declares(method.getParameters(),
								suspect.getName())) {
					return true;
				}
			} else if (current instanceof ConstructorDeclaration) {
				ConstructorDeclaration constructor = (ConstructorDeclaration) current;
				if (this.declares(constructor.getParameters(),
						suspect.getName())) {
					return true;
				}
			}
			current = current.getParentNode();
		}
		return false;
	}

	private boolean declares(List<Parameter> parameters, String name) {
		for (Parameter parameter : parameters) {
			if (name.equals(parameter.getId().getName())) {
				return true;
			}
		}
		return false;
	}

}
