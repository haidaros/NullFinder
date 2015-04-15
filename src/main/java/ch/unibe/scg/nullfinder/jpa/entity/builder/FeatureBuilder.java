package ch.unibe.scg.nullfinder.jpa.entity.builder;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;

public class FeatureBuilder extends AbstractEntityBuilder<Feature> {

	protected EntityConnector entityConnector;

	public FeatureBuilder(EntityConnector entityConnector, Feature feature) {
		super(feature);
		this.entityConnector = entityConnector;
	}

	public FeatureBuilder(Feature feature) {
		this(EntityConnector.INSTANCE, feature);
	}

	public FeatureBuilder addNodeReasons(
			List<com.github.javaparser.ast.Node> javaParserNodes) {
		for (com.github.javaparser.ast.Node javaParserNode : javaParserNodes) {
			this.addNodeReason(javaParserNode);
		}
		return this;
	}

	public FeatureBuilder addNodeReason(
			com.github.javaparser.ast.Node javaParserNode) {
		Node node = this.entityConnector.createAndConnectNode(
				this.entity.getNullCheck(), javaParserNode);
		return this.addNodeReason(node);
	}

	public FeatureBuilder addNodeReason(Node node) {
		this.entityConnector.createAndConnectNodeReason(this.entity, node);
		return this;
	}

	public FeatureBuilder addFeatureReasons(List<Feature> reasonFeatures) {
		for (Feature reasonFeature : reasonFeatures) {
			this.addFeatureReason(reasonFeature);
		}
		return this;
	}

	public FeatureBuilder addFeatureReason(Feature reasonFeature) {
		this.entityConnector.createAndConnectFeatureReason(this.entity,
				reasonFeature);
		return this;
	}

}
