package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;

/**
 * Tries to find assignments to the variable with the name of the variable null
 * is compared against.
 *
 * @see VariableComparandAssignmentExtractor#extract(NullCheck) For a
 *      description of the used strategy and its short-comings.
 */
public class VariableComparandAssignmentExtractor extends
		AbstractVariableComparandDependentExtractor {

	public VariableComparandAssignmentExtractor() {
		super(2);
	}

	/**
	 * Searches for a assignments to for the variable null is compared against
	 * in the code above the comparison. The tree is traversed upwards until the
	 * current body is left.
	 *
	 * <NOTE>This will only match true positives, but in the case of nested
	 * bodies - e.g. inner classes or anonymous classes - it may produce false
	 * negatives</NOTE>
	 */
	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck) {
		Feature variableFeature = this.extractVariableFeature(nullCheck);
		Node variableNode = this.extractVariableNode(nullCheck);
		com.github.javaparser.ast.Node current = nullCheck.getNode()
				.getJavaParserNode().getParentNode();
		com.github.javaparser.ast.Node stop = nullCheck.getNode()
				.getJavaParserNode();
		List<AssignExpr> assignments = new ArrayList<>();
		while (current != null) {
			List<com.github.javaparser.ast.Node> children = current
					.getChildrenNodes();
			for (com.github.javaparser.ast.Node child : children) {
				if (child == stop) {
					break;
				}
				if (child instanceof AssignExpr) {
					AssignExpr assignment = (AssignExpr) child;
					if (assignment.getTarget().toString()
							.equals(this.getVariableName(variableNode))) {
						assignments.add(assignment);
					}
				}
			}
			if (current instanceof MethodDeclaration
					|| current instanceof ConstructorDeclaration) {
				break;
			}
			stop = current;
			current = current.getParentNode();
		}
		// TODO is there a useful non-empty manifestation?
		return assignments
				.stream()
				.map(assignment -> this.getFeatureBuilder(nullCheck, "")
						.addFeatureReason(variableFeature)
						.addNodeReason(assignment).getEntity())
				.collect(Collectors.toList());
	}

}
