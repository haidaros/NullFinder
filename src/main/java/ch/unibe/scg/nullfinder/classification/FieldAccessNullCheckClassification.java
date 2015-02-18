package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;

public class FieldAccessNullCheckClassification implements
		INullCheckClassification {

	@Override
	public boolean accepts(NullCheck nullCheck) {
		BinaryExpr expression = (BinaryExpr) nullCheck.getNode()
				.getParentNode();
		return expression.getLeft() instanceof FieldAccessExpr
				|| expression.getRight() instanceof FieldAccessExpr;
	}

}
