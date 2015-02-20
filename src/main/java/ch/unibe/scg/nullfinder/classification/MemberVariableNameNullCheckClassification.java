package ch.unibe.scg.nullfinder.classification;

import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

public class MemberVariableNameNullCheckClassification extends
		AbstractNameNullCheckClassification {

	public MemberVariableNameNullCheckClassification(NullCheck check) {
		super(check);
	}

	@Override
	public boolean accepts(NullCheck check) {
		return super.accepts(check)
				&& this.checksNullAgainstMemberVariable(check.getNode());
	}

	private boolean checksNullAgainstMemberVariable(NullLiteralExpr node) {
		assert node.getParentNode() instanceof BinaryExpr;
		assert node.getParentNode().getChildrenNodes().stream()
				.anyMatch(candidate -> candidate instanceof NameExpr);
		BinaryExpr expression = (BinaryExpr) node.getParentNode();
		NameExpr suspect = (NameExpr) ((expression.getLeft() instanceof NameExpr) ? expression
				.getLeft() : expression.getRight());
		Node current = expression;
		while (current != null) {
			if (current instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) current;
				if (this.declares(clazz.getMembers(), suspect.getName())) {
					return true;
				}
			} else if (current instanceof ObjectCreationExpr) {
				ObjectCreationExpr objectCreation = (ObjectCreationExpr) current;
				if (objectCreation.getAnonymousClassBody() != null
						&& this.declares(
								objectCreation.getAnonymousClassBody(),
								suspect.getName())) {
					return true;
				}
			}
			current = current.getParentNode();
		}
		return false;
	}

	private boolean declares(List<BodyDeclaration> bodies, String name) {
		for (BodyDeclaration body : bodies) {
			if (body instanceof FieldDeclaration) {
				FieldDeclaration field = (FieldDeclaration) body;
				for (VariableDeclarator variable : field.getVariables()) {
					if (name.equals(variable.getId().getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
