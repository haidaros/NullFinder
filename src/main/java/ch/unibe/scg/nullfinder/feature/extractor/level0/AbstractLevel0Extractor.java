package ch.unibe.scg.nullfinder.feature.extractor.level0;

import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;
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
	public Feature extract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		assert nullCheck.getNode().getJavaParserNode().getParentNode() instanceof BinaryExpr;
		BinaryExpr binary = (BinaryExpr) nullCheck.getNode().getJavaParserNode()
				.getParentNode();
		CompilationUnit compilationUnit = nullCheck.getNode().getCompilationUnit();
		Class<?> comparand = this.getComparand();
		if (comparand.isInstance(binary.getLeft())) {
			Feature feature = new Feature(nullCheck, this);
			feature.getReasons().add(
					new NodeReason(feature, new Node(compilationUnit, binary
							.getLeft())));
			return feature;
		}
		if (comparand.isInstance(binary.getRight())) {
			Feature feature = new Feature(nullCheck, this);
			feature.getReasons().add(
					new NodeReason(feature, new Node(compilationUnit, binary
							.getRight())));
			return feature;
		}
		throw new UnextractableException(nullCheck);
	}

	abstract protected Class<?> getComparand();

}
