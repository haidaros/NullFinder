package ch.unibe.scg.nullfinder.feature.extractor.level0;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractComparandExtractor;

import com.github.javaparser.ast.expr.FieldAccessExpr;

public class FieldAccessExtractor extends AbstractComparandExtractor {

	public FieldAccessExtractor() {
		super(0);
	}

	@Override
	protected Class<?> getComparand() {
		return FieldAccessExpr.class;
	}

}
