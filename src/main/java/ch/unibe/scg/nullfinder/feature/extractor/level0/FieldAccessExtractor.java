package ch.unibe.scg.nullfinder.feature.extractor.level0;

import com.github.javaparser.ast.expr.FieldAccessExpr;

public class FieldAccessExtractor extends AbstractLevel0Extractor {

	@Override
	protected Class<?> getComparand() {
		return FieldAccessExpr.class;
	}

}
