package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.reason.FeatureReason;
import ch.unibe.scg.nullfinder.feature.reason.IReason;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;

public class LocalVariableExtractor extends AbstractDeclarationExtractor {

	@Override
	protected IFeature safeExtract(NullCheck check, Set<IFeature> features)
			throws UnextractableException {
		// TODO there is some dirty stuff going on here...
		IFeature feature = this.extractNameExtractorFeature(check, features);
		IReason reason = feature.getReasons().iterator().next();
		NameExpr suspect = (NameExpr) ((NodeReason) reason).getNode();
		Node current = check.getNode().getParentNode();
		Node stop = check.getNode();
		// haha, a null check!
		while (current != null) {
			List<Node> children = current.getChildrenNodes();
			if (current instanceof ForStmt) {
				// reorder children
				ForStmt forStatement = (ForStmt) current;
				children = new ArrayList<>();
				if (forStatement.getInit() != null) {
					// fuck you...
					children.addAll(forStatement.getInit());
				}
				children.add(forStatement.getCompare());
				if (forStatement.getUpdate() != null) {
					// ...and fuck you
					children.addAll(forStatement.getUpdate());
				}
				children.add(forStatement.getBody());
			}
			for (Node child : children) {
				if (child == stop) {
					break;
				}
				if (child instanceof VariableDeclarationExpr) {
					try {
						VariableDeclarator variableDeclarator = this
								.findDeclaration(
										(VariableDeclarationExpr) child,
										suspect);
						return this.buildFeature(new FeatureReason(feature),
								new NodeReason(variableDeclarator));
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				} else if (child instanceof ExpressionStmt) {
					Expression expression = ((ExpressionStmt) child)
							.getExpression();
					if (expression instanceof VariableDeclarationExpr) {
						try {
							VariableDeclarator variableDeclarator = this
									.findDeclaration(
											(VariableDeclarationExpr) expression,
											suspect);
							return this.buildFeature(
									new FeatureReason(feature), new NodeReason(
											variableDeclarator));
						} catch (DeclarationNotFoundException exception) {
							// noop
						}
					} else if (expression instanceof AssignExpr) {
						AssignExpr assignment = (AssignExpr) expression;
						if (assignment.getTarget() instanceof NameExpr) {
							NameExpr name = (NameExpr) assignment.getTarget();
							if (suspect.getName().equals(name.getName())) {
								return this.buildFeature(new FeatureReason(
										feature), new NodeReason(name));
							}
						}
						// TODO what if the target is not a name expression?
					}
				}
			}
			stop = current;
			current = current.getParentNode();
		}
		throw new UnextractableException(check);
	}

	protected VariableDeclarator findDeclaration(
			VariableDeclarationExpr declaration, NameExpr suspect)
			throws DeclarationNotFoundException {
		for (VariableDeclarator variable : declaration.getVars()) {
			if (suspect.getName().equals(variable.getId().getName())) {
				return variable;
			}
		}
		throw new DeclarationNotFoundException(suspect);
	}

}
