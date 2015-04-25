package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.feature.extractor.level0.ComparandExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;

/**
 * Depends on null checks against variables.
 */
public abstract class AbstractVariableComparandDependentExtractor extends
		AbstractDependentExtractor {

	public AbstractVariableComparandDependentExtractor(int level) {
		super(level);
	}

	/**
	 * Extracts the feature that represents that null is compared against a
	 * variable. The null check must meet the dependencies.
	 *
	 * @param nullCheck
	 *            The null check to examine
	 * @return The feature
	 *
	 * @see AbstractVariableComparandDependentExtractor#meetsDependencies(NullCheck)
	 */
	protected Feature extractVariableFeature(NullCheck nullCheck) {
		assert this.meetsDependencies(nullCheck);
		return nullCheck.getFeatures().stream()
				.filter(this::isVariableComparison).findFirst().get();
	}

	/**
	 * Extracts the node that represents the variable null is compared against.
	 * The null check must meet the dependencies.
	 *
	 * @param nullCheck
	 *            The null check to examine
	 * @return The node
	 *
	 * @see AbstractVariableComparandDependentExtractor#meetsDependencies(NullCheck)
	 */
	protected Node extractVariableNode(NullCheck nullCheck) {
		assert this.meetsDependencies(nullCheck);
		List<Node> variableNodes = this.extractVariableFeature(nullCheck)
				.getReasons().stream()
				.filter(reason -> reason instanceof NodeReason)
				.map(reason -> (NodeReason) reason)
				.map(nodeReason -> nodeReason.getNode())
				.collect(Collectors.toList());
		assert variableNodes.size() == 1;
		return variableNodes.get(0);
	}

	/**
	 * Gets the name of the variable represented by the specified node.
	 *
	 * @param node
	 *            Must be one of NameExpr, FieldAccessExpr or ArrayAccessExpr
	 * @return The variable name
	 */
	protected String getVariableName(Node node) {
		assert node.getJavaParserNode() instanceof NameExpr
				|| node.getJavaParserNode() instanceof FieldAccessExpr
				|| node.getJavaParserNode() instanceof ArrayAccessExpr;
		return node.getJavaParserNode().toString();
	}

	/**
	 * Checks if the specified feature describes a comparison against a
	 * variable. This means it was extracted by a comparand extractor and the
	 * manifestation is one of NameExpr, FieldAccessExpr or ArrayAccessExpr
	 *
	 * @param feature
	 *            The feature to examine
	 * @return true if it does, false otherwise
	 */
	protected boolean isVariableComparison(Feature feature) {
		return feature.getExtractor() instanceof ComparandExtractor
				&& (feature.getManifestation().equals(NameExpr.class.getName())
						|| feature.getManifestation().equals(
								FieldAccessExpr.class.getName()) || feature
						.getManifestation().equals(
								ArrayAccessExpr.class.getName()));
	}

	/**
	 * Meets the dependencies if exactly one feature is a comparison against a
	 * variable.
	 */
	@Override
	protected boolean meetsDependencies(NullCheck nullCheck) {
		return nullCheck.getFeatures().stream()
				.filter(this::isVariableComparison).count() == 1;
	}

}
