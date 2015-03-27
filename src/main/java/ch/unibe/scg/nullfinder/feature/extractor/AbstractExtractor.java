package ch.unibe.scg.nullfinder.feature.extractor;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.FeatureReason;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public abstract class AbstractExtractor implements IExtractor {

	protected int level;

	public AbstractExtractor(int level) {
		this.level = level;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	protected Node createAndConnectNode(NullCheck nullCheck,
			com.github.javaparser.ast.Node javaParserNode) {
		CompilationUnit compilationUnit = nullCheck.getNode()
				.getCompilationUnit();
		Node node = Node.getCachedNode(compilationUnit, javaParserNode);
		return node;
	}

	protected Feature createAndConnectFeature(NullCheck nullCheck) {
		Feature feature = new Feature(nullCheck, this);
		nullCheck.getFeatures().add(feature);
		return feature;
	}

	protected NodeReason createAndConnectNodeReason(Feature feature, Node node) {
		NodeReason nodeReason = new NodeReason(feature, node);
		node.getNodeReasons().add(nodeReason);
		feature.getReasons().add(nodeReason);
		return nodeReason;
	}

	protected FeatureReason createAndConnectFeatureReason(Feature feature,
			Feature reasonFeature) {
		FeatureReason featureReason = new FeatureReason(feature, reasonFeature);
		reasonFeature.getFeatureReasons().add(featureReason);
		feature.getReasons().add(featureReason);
		return featureReason;
	}

}
