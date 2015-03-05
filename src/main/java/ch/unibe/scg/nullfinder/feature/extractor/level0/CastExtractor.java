package ch.unibe.scg.nullfinder.feature.extractor.level0;

import com.github.javaparser.ast.expr.CastExpr;

public class CastExtractor extends AbstractLevel0Extractor {

	@Override
	protected Class<?> getComparand() {
		return CastExpr.class;
	}

}
