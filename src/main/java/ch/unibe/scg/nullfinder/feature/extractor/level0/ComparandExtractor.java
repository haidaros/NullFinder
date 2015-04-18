package ch.unibe.scg.nullfinder.feature.extractor.level0;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;

public class ComparandExtractor extends AbstractExtractor {

	public ComparandExtractor() {
		super(0);
	}

	@Override
	public List<Feature> extract(NullCheck nullCheck, List<Feature> features) {
		assert nullCheck.getNode().getJavaParserNode().getParentNode() instanceof BinaryExpr;
		BinaryExpr binary = (BinaryExpr) nullCheck.getNode()
				.getJavaParserNode().getParentNode();
		com.github.javaparser.ast.Node comparand = (binary.getLeft() == nullCheck
				.getNode().getJavaParserNode()) ? binary.getRight() : binary
				.getLeft();
		return this.getFeatures(this
				.getFeatureBuilder(nullCheck, comparand.getClass().getName())
				.addNodeReason(comparand).getEntity());

	}
}
