package ch.unibe.scg.nullfinder.feature.extractor.level0;

import com.github.javaparser.ast.expr.EnclosedExpr;

public class EnclosedExtractor extends AbstractLevel0Extractor {

	@Override
	protected Class<?> getComparand() {
		return EnclosedExpr.class;
	}

}
