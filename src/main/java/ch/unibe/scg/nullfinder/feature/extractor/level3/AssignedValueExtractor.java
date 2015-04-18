package ch.unibe.scg.nullfinder.feature.extractor.level3;

import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractDependentExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level2.VariableComparandAssignmentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.AssignExpr;

public class AssignedValueExtractor extends AbstractDependentExtractor {

	public AssignedValueExtractor() {
		super(3);
	}

	protected Feature extractAssignmentFeature(NullCheck nullCheck,
			List<Feature> features) {
		assert this.meetsDependencies(nullCheck, features);
		return features
				.stream()
				.filter(this::isExtractedByVariableComparandAssignmentExtractor)
				.findFirst().get();
	}

	protected Node extractAssignmentNode(NullCheck nullCheck,
			List<Feature> features) {
		assert this.meetsDependencies(nullCheck, features);
		List<Node> assignmentNodes = this
				.extractAssignmentFeature(nullCheck, features).getReasons()
				.stream().filter(reason -> reason instanceof NodeReason)
				.map(reason -> (NodeReason) reason)
				.map(nodeReason -> nodeReason.getNode())
				.collect(Collectors.toList());
		assert assignmentNodes.size() == 1;
		return assignmentNodes.get(0);
	}

	protected boolean isExtractedByVariableComparandAssignmentExtractor(
			Feature feature) {
		return feature.getExtractor() instanceof VariableComparandAssignmentExtractor;
	}

	@Override
	protected boolean meetsDependencies(NullCheck nullCheck,
			List<Feature> features) {
		return features
				.stream()
				.filter(this::isExtractedByVariableComparandAssignmentExtractor)
				.count() == 1;
	}

	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck,
			List<Feature> features) {
		Feature assignmentFeature = this.extractAssignmentFeature(nullCheck,
				features);
		com.github.javaparser.ast.Node value = ((AssignExpr) this
				.extractAssignmentNode(nullCheck, features).getJavaParserNode())
				.getValue();
		return this.getFeatures(this
				.getFeatureBuilder(nullCheck, value.getClass().getName())
				.addFeatureReason(assignmentFeature).addNodeReason(value)
				.getEntity());
	}

}
