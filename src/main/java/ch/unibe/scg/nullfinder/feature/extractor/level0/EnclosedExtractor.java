package ch.unibe.scg.nullfinder.feature.extractor.level0;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractComparandExtractor;

import com.github.javaparser.ast.expr.EnclosedExpr;

public class EnclosedExtractor extends AbstractComparandExtractor {

	public EnclosedExtractor() {
		super(0);
	}

	@Override
	protected Class<?> getComparand() {
		return EnclosedExpr.class;
	}

}
