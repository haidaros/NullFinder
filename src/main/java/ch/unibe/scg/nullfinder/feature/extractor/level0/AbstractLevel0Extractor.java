package ch.unibe.scg.nullfinder.feature.extractor.level0;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.AbstractExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;

import com.github.javaparser.ast.expr.BinaryExpr;

public abstract class AbstractLevel0Extractor extends AbstractExtractor {

	public AbstractLevel0Extractor() {
		super(0);
	}

	@Override
	public IFeature extract(NullCheck check, Set<IFeature> features)
			throws UnextractableException {
		assert check.getNode().getParentNode() instanceof BinaryExpr;
		BinaryExpr binary = (BinaryExpr) check.getNode().getParentNode();
		Class<?> comparand = this.getComparand();
		if (comparand.isInstance(binary.getLeft())) {
			return this.buildFeature(new NodeReason(binary.getLeft()));
		}
		if (comparand.isInstance(binary.getRight())) {
			return this.buildFeature(new NodeReason(binary.getRight()));
		}
		throw new UnextractableException(check);
	}

	abstract protected Class<?> getComparand();

}
