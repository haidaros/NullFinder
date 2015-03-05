package ch.unibe.scg.nullfinder.feature.extractor.level0;

import com.github.javaparser.ast.expr.ArrayAccessExpr;

public class ArrayAccessExtractor extends AbstractLevel0Extractor {

	@Override
	protected Class<?> getComparand() {
		return ArrayAccessExpr.class;
	}

}
