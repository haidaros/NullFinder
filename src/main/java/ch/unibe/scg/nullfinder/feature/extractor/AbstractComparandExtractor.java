package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;

public abstract class AbstractComparandExtractor extends AbstractExtractor {

	public AbstractComparandExtractor(int level) {
		super(level);
	}

	@Override
	public Feature extract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		assert nullCheck.getNode().getJavaParserNode().getParentNode() instanceof BinaryExpr;
		BinaryExpr binary = (BinaryExpr) nullCheck.getNode()
				.getJavaParserNode().getParentNode();
		Class<?> comparand = this.getComparand();
		if (comparand.isInstance(binary.getLeft())) {
			Node node = this.createAndConnectNode(nullCheck, binary.getLeft());
			Feature feature = this.createAndConnectFeature(nullCheck);
			this.createAndConnectNodeReason(feature, node);
			return feature;
		}
		if (comparand.isInstance(binary.getRight())) {
			Node node = this.createAndConnectNode(nullCheck, binary.getRight());
			Feature feature = this.createAndConnectFeature(nullCheck);
			this.createAndConnectNodeReason(feature, node);
		}
		throw new UnextractableException(nullCheck);
	}

	abstract protected Class<?> getComparand();

}
