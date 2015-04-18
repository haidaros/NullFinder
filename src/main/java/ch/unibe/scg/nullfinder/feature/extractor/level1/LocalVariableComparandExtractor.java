package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;

public class LocalVariableComparandExtractor extends
		AbstractVariableComparandDependentExtractor {

	public LocalVariableComparandExtractor() {
		super(1);
	}

	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck,
			List<Feature> features) {
		// TODO there is some dirty stuff going on here...
		Feature variableFeature = this.extractVariableFeature(nullCheck,
				features);
		Node variableNode = this.extractVariableNode(nullCheck, features);
		com.github.javaparser.ast.Node current = nullCheck.getNode()
				.getJavaParserNode().getParentNode();
		com.github.javaparser.ast.Node stop = nullCheck.getNode()
				.getJavaParserNode();
		// haha, a null nullCheck!
		while (current != null) {
			List<com.github.javaparser.ast.Node> children = current
					.getChildrenNodes();
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
			for (com.github.javaparser.ast.Node child : children) {
				if (child == stop) {
					break;
				}
				if (child instanceof VariableDeclarationExpr) {
					try {
						VariableDeclarator variableDeclarator = this
								.findDeclaration(
										(VariableDeclarationExpr) child,
										variableNode);
						return this
								.getFeatures(this
										.getFeatureBuilder(
												nullCheck,
												VariableDeclarationExpr.class
														.getName())
										.addNodeReason(variableDeclarator)
										.addFeatureReason(variableFeature)
										.getEntity());
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
											variableNode);
							return this.getFeatures(this
									.getFeatureBuilder(nullCheck,
											ExpressionStmt.class.getName())
									.addNodeReason(variableDeclarator)
									.addFeatureReason(variableFeature)
									.getEntity());
						} catch (DeclarationNotFoundException exception) {
							// noop
						}
					}
				}
			}
			stop = current;
			current = current.getParentNode();
		}
		return Collections.emptyList();
	}

	protected VariableDeclarator findDeclaration(
			VariableDeclarationExpr declaration, Node variableExtractorNode)
			throws DeclarationNotFoundException {
		for (VariableDeclarator variable : declaration.getVars()) {
			if (this.getVariableName(variableExtractorNode).equals(
					variable.getId().getName())) {
				return variable;
			}
		}
		throw new DeclarationNotFoundException(variableExtractorNode);
	}

}
