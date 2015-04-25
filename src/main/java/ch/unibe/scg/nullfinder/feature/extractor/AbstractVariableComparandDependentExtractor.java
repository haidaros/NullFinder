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

public abstract class AbstractVariableComparandDependentExtractor extends
		AbstractDependentExtractor {

	public AbstractVariableComparandDependentExtractor(int level) {
		super(level);
	}

	protected Feature extractVariableFeature(NullCheck nullCheck) {
		assert this.meetsDependencies(nullCheck);
		return nullCheck.getFeatures().stream()
				.filter(this::isExtractedByVariableComparandExtractor)
				.findFirst().get();
	}

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

	protected String getVariableName(Node node) {
		assert node.getJavaParserNode() instanceof NameExpr
				|| node.getJavaParserNode() instanceof FieldAccessExpr
				|| node.getJavaParserNode() instanceof ArrayAccessExpr;
		return node.getJavaParserNode().toString();
	}

	protected boolean isExtractedByVariableComparandExtractor(Feature feature) {
		return feature.getExtractor() instanceof ComparandExtractor
				&& (feature.getManifestation().equals(NameExpr.class.getName())
						|| feature.getManifestation().equals(
								FieldAccessExpr.class.getName()) || feature
						.getManifestation().equals(
								ArrayAccessExpr.class.getName()));
	}

	@Override
	protected boolean meetsDependencies(NullCheck nullCheck) {
		return nullCheck.getFeatures().stream()
				.filter(this::isExtractedByVariableComparandExtractor).count() == 1;
	}

}
