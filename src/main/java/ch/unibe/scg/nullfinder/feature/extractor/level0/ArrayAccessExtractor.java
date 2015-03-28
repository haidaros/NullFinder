package ch.unibe.scg.nullfinder.feature.extractor.level0;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractComparandExtractor;

import com.github.javaparser.ast.expr.ArrayAccessExpr;

public class ArrayAccessExtractor extends AbstractComparandExtractor {

	public ArrayAccessExtractor() {
		super(0);
	}

	@Override
	protected Class<?> getComparand() {
		return ArrayAccessExpr.class;
	}

}
