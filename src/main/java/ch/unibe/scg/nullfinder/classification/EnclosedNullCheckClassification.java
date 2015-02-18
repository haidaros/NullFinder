package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;

public class EnclosedNullCheckClassification implements
		INullCheckClassification {

	@Override
	public boolean accepts(NullCheck check) {
		BinaryExpr expression = (BinaryExpr) check.getNode().getParentNode();
		return expression.getLeft() instanceof EnclosedExpr
				|| expression.getRight() instanceof EnclosedExpr;
	}

}
