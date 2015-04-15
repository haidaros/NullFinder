package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;

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
		Class<?> comparandClass = this.getComparandClass();
		if (comparandClass.isInstance(binary.getLeft())) {
			return this.getCreatedAndConnectedFeature(nullCheck,
					binary.getLeft());
		}
		if (comparandClass.isInstance(binary.getRight())) {
			return this.getCreatedAndConnectedFeature(nullCheck,
					binary.getRight());
		}
		throw new UnextractableException(nullCheck);
	}

	protected Feature getCreatedAndConnectedFeature(NullCheck nullCheck,
			Expression expression) {
		Node node = this.createAndConnectNode(nullCheck, expression);
		Feature feature = this.createAndConnectFeature(nullCheck);
		this.createAndConnectNodeReason(feature, node);
		return feature;
	}

	abstract protected Class<?> getComparandClass();

}
