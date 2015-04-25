package ch.unibe.scg.nullfinder.feature.extractor.level3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractDependentExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level2.VariableComparandAssignmentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;

/**
 * Extracts the types of values used in the assignments to the variable with the
 * name of the variable null is compared against.
 */
public class AssignedValueExtractor extends AbstractDependentExtractor {

	public AssignedValueExtractor() {
		super(3);
	}

	/**
	 * Extracts the features that represent an assignment to the variable that
	 * null is compared against. The null check must meet the dependencies.
	 *
	 * @param nullCheck
	 *            The null check to examine
	 * @return The features
	 *
	 * @see AssignedValueExtractor#meetsDependencies(NullCheck)
	 */
	protected List<Feature> extractAssignmentFeatures(NullCheck nullCheck) {
		assert this.meetsDependencies(nullCheck);
		return nullCheck
				.getFeatures()
				.stream()
				.filter(this::isExtractedByVariableComparandAssignmentExtractor)
				.collect(Collectors.toList());
	}

	/**
	 * Extracts the nodes that represent an assignment to the variable that null
	 * is compared against. The feature must meet the dependencies.
	 *
	 * @param feature
	 *            The features to examine
	 * @return The nodes
	 *
	 * @see AssignedValueExtractor#isExtractedByVariableComparandAssignmentExtractor(Feature)
	 */
	protected List<Node> extractAssignmentNodes(Feature feature) {
		assert this.isExtractedByVariableComparandAssignmentExtractor(feature);
		return feature.getReasons().stream()
				.filter(reason -> reason instanceof NodeReason)
				.map(reason -> (NodeReason) reason)
				.map(nodeReason -> nodeReason.getNode())
				.collect(Collectors.toList());
	}

	/**
	 * Checks if the specified feature describes an assignment to the variable
	 * that null is compared against. This means it was extracted by a variable
	 * comparand extractor.
	 *
	 * @param feature
	 *            The feature to examine
	 * @return true if it does, false otherwise
	 */
	protected boolean isExtractedByVariableComparandAssignmentExtractor(
			Feature feature) {
		return feature.getExtractor() instanceof VariableComparandAssignmentExtractor;
	}

	/**
	 * Extracts a stream over new features representing the types of values
	 * assigned to the variable that null is compared against. The feature must
	 * meet the dependencies.
	 *
	 * @param feature
	 *            The feature to examine
	 * @return A stream over the new features
	 *
	 * @see AssignedValueExtractor#isExtractedByVariableComparandAssignmentExtractor(Feature)
	 */
	protected Stream<Feature> safeSingleExtractStream(Feature feature) {
		return this
				.extractAssignmentNodes(feature)
				.stream()
				.map(this::getAssignedValue)
				.map(value -> this
						.getFeatureBuilder(feature.getNullCheck(),
								value.getClass().getName())
						.addFeatureReason(feature).addNodeReason(value)
						.getEntity());
	}

	protected com.github.javaparser.ast.Node getAssignedValue(Node node) {
		com.github.javaparser.ast.Node javaParserNode = node
				.getJavaParserNode();
		if (javaParserNode instanceof AssignExpr) {
			return ((AssignExpr) javaParserNode).getValue();
		}
		if (javaParserNode instanceof VariableDeclarator) {
			return ((VariableDeclarator) javaParserNode).getInit();
		}
		throw new RuntimeException(
				"VariableComparandAssignmentExtractor extracted non-compatible node type. Should be one of AssignExpr or VariableDeclarator, was "
						+ javaParserNode.getClass().getName());
	}

	/**
	 * Meets the dependencies if at least one feature describes an assignment to
	 * the variable that null is compared against.
	 */
	@Override
	protected boolean meetsDependencies(NullCheck nullCheck) {
		return nullCheck
				.getFeatures()
				.stream()
				.filter(this::isExtractedByVariableComparandAssignmentExtractor)
				.count() > 0;
	}

	/**
	 * Extract a new feature for every assignment feature.
	 */
	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck) {
		return this.extractAssignmentFeatures(nullCheck).stream()
				.flatMap(this::safeSingleExtractStream)
				.collect(Collectors.toList());
	}

}
