package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;

/**
 * Tries to find the declaration of a local variable with the name of the
 * variable null is compared against.
 *
 * @see LocalVariableComparandExtractor#extract(NullCheck) For a description of
 *      the used strategy and its short-comings.
 */
public class LocalVariableComparandExtractor extends
		AbstractVariableComparandDependentExtractor {

	public LocalVariableComparandExtractor() {
		super(1);
	}

	/**
	 * Searches for a declaration statement for the variable null is compared
	 * against in the code above the comparison. The tree is traversed upwards
	 * until the current body is left.
	 *
	 * <NOTE>This will only match true positives, but in the case of nested
	 * bodies - e.g. inner classes or anonymous classes - it may produce false
	 * negatives</NOTE>
	 */
	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck) {
		// TODO there is some dirty stuff going on here...
		Feature variableFeature = this.extractVariableFeature(nullCheck);
		Node variableNode = this.extractVariableNode(nullCheck);
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
						return Arrays
								.asList(this
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
							return Arrays.asList(this
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
			if (current instanceof BodyDeclaration) {
				break;
			}
			stop = current;
			current = current.getParentNode();
		}
		return Collections.emptyList();
	}

	/**
	 * Finds the declaration of the variable described by the specified node.
	 *
	 * @param declaration
	 *            The declaration to search
	 * @param variableExtractorNode
	 *            The variable described to find
	 * @return The variable declarator
	 * @throws DeclarationNotFoundException
	 *             Thrown if no declaration could be found
	 */
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
