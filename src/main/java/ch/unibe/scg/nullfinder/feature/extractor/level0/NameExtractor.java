package ch.unibe.scg.nullfinder.feature.extractor.level0;

import com.github.javaparser.ast.expr.NameExpr;

public class NameExtractor extends AbstractLevel0Extractor {

	@Override
	protected Class<?> getComparand() {
		return NameExpr.class;
	}

}
