package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.BinaryExpr;

public class ArrayAccessNullCheckClassification implements
		INullCheckClassification {

	@Override
	public boolean accepts(NullCheck nullCheck) {
		BinaryExpr expression = (BinaryExpr) nullCheck.getNode()
				.getParentNode();
		return expression.getLeft() instanceof ArrayAccessExpr
				|| expression.getRight() instanceof ArrayAccessExpr;
	}

}
