package ch.unibe.scg.nullfinder.jpa.entity.builder;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.FeatureReason;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public class EntityConnector {

	// TODO maybe should use spring to provide singleton
	public static final EntityConnector INSTANCE = new EntityConnector();

	public Node createAndConnectNode(NullCheck nullCheck,
			com.github.javaparser.ast.Node javaParserNode) {
		CompilationUnit compilationUnit = nullCheck.getNode()
				.getCompilationUnit();
		Node node = Node.getCachedNode(compilationUnit, javaParserNode);
		return node;
	}

	public Feature createAndConnectFeature(NullCheck nullCheck,
			IExtractor extractor, String manifestation) {
		Feature feature = new Feature(nullCheck, extractor, manifestation);
		nullCheck.getFeatures().add(feature);
		return feature;
	}

	public NodeReason createAndConnectNodeReason(Feature feature, Node node) {
		NodeReason nodeReason = new NodeReason(feature, node);
		node.getNodeReasons().add(nodeReason);
		feature.getReasons().add(nodeReason);
		return nodeReason;
	}

	public FeatureReason createAndConnectFeatureReason(Feature feature,
			Feature reasonFeature) {
		FeatureReason featureReason = new FeatureReason(feature, reasonFeature);
		reasonFeature.getFeatureReasons().add(featureReason);
		feature.getReasons().add(featureReason);
		return featureReason;
	}

}
