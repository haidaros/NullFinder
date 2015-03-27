package ch.unibe.scg.nullfinder.feature.extractor.level0;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;

public abstract class AbstractLevel0Extractor extends AbstractExtractor {

	public AbstractLevel0Extractor() {
		super(0);
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
