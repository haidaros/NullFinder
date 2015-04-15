package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.feature.extractor.level0.ArrayAccessExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.FieldAccessExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;

public abstract class AbstractVariableDependentExtractor extends
		AbstractDependentExtractor {

	public AbstractVariableDependentExtractor(int level) {
		super(level);
	}

	protected Feature extractVariableExtractorFeature(NullCheck nullCheck,
			List<Feature> features) {
		assert this.meetsDependencies(nullCheck, features);
		return features.stream().filter(this::isExtractedByVariableExtractor)
				.findFirst().get();
	}

	protected Node extractVariableExtractorNode(NullCheck nullCheck,
			List<Feature> features) {
		assert this.meetsDependencies(nullCheck, features);
		List<Node> variableExtractorNodes = this
				.extractVariableExtractorFeature(nullCheck, features)
				.getReasons().stream()
				.filter(reason -> reason instanceof NodeReason)
				.map(reason -> (NodeReason) reason)
				.map(nodeReason -> nodeReason.getNode())
				.collect(Collectors.toList());
		assert variableExtractorNodes.size() == 1;
		return variableExtractorNodes.get(0);
	}

	protected String getVariableName(Node node) {
		assert node.getJavaParserNode() instanceof NameExpr
				|| node.getJavaParserNode() instanceof FieldAccessExpr
				|| node.getJavaParserNode() instanceof ArrayAccessExpr;
		return node.getJavaParserNode().toString();
	}

	protected boolean isExtractedByVariableExtractor(Feature feature) {
		return feature.getExtractor() instanceof NameExtractor
				|| feature.getExtractor() instanceof FieldAccessExtractor
				|| feature.getExtractor() instanceof ArrayAccessExtractor;
	}

	@Override
	protected boolean meetsDependencies(NullCheck nullCheck,
			List<Feature> features) {
		return features.stream().filter(this::isExtractedByVariableExtractor)
				.count() == 1;
	}

}
