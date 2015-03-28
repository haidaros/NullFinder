package ch.unibe.scg.nullfinder.feature.extractor.level0;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractComparandExtractor;

import com.github.javaparser.ast.expr.NameExpr;

public class NameExtractor extends AbstractComparandExtractor {

	public NameExtractor() {
		super(0);
	}

	@Override
	protected Class<?> getComparand() {
		return NameExpr.class;
	}

}
