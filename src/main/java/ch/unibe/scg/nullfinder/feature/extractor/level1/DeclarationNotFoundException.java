package ch.unibe.scg.nullfinder.feature.extractor.level1;

import com.github.javaparser.ast.expr.NameExpr;

public class DeclarationNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeclarationNotFoundException(NameExpr suspect) {
		super(suspect.toString());
	}

}