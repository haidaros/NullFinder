package ch.unibe.scg.nullfinder.feature.extractor.level0;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.AbstractExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;

import com.github.javaparser.ast.expr.BinaryExpr;

public abstract class AbstractLevel0Extractor extends AbstractExtractor {

	public AbstractLevel0Extractor() {
		super(0);
	}

	@Override
	public Feature extract(NullCheck check, Set<Feature> features)
			throws UnextractableException {
		assert check.getNode().getParentNode() instanceof BinaryExpr;
		BinaryExpr binary = (BinaryExpr) check.getNode().getParentNode();
		Class<?> comparand = this.getComparand();
		if (comparand.isInstance(binary.getLeft())) {
			Feature feature = new Feature(check, this);
			feature.getReasons().add(new NodeReason(feature, binary.getLeft()));
			return feature;
		}
		if (comparand.isInstance(binary.getRight())) {
			Feature feature = new Feature(check, this);
			feature.getReasons()
					.add(new NodeReason(feature, binary.getRight()));
			return feature;
		}
		throw new UnextractableException(check);
	}

	abstract protected Class<?> getComparand();

}
