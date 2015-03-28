package ch.unibe.scg.nullfinder.feature.extractor.level0;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractComparandExtractor;

import com.github.javaparser.ast.expr.CastExpr;

public class CastExtractor extends AbstractComparandExtractor {

	public CastExtractor() {
		super(0);
	}

	@Override
	protected Class<?> getComparand() {
		return CastExpr.class;
	}

}
