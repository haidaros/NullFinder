package ch.unibe.scg.nullfinder.feature.extractor.level0;

import com.github.javaparser.ast.expr.MethodCallExpr;

public class MethodCallExtractor extends AbstractLevel0Extractor {

	@Override
	protected Class<?> getComparand() {
		return MethodCallExpr.class;
	}

}
