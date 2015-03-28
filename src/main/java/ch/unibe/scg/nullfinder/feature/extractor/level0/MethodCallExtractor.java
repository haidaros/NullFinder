package ch.unibe.scg.nullfinder.feature.extractor.level0;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractComparandExtractor;

import com.github.javaparser.ast.expr.MethodCallExpr;

public class MethodCallExtractor extends AbstractComparandExtractor {

	public MethodCallExtractor() {
		super(0);
	}

	@Override
	protected Class<?> getComparand() {
		return MethodCallExpr.class;
	}

}
