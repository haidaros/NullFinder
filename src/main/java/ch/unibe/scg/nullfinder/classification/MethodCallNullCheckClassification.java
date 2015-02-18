package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class MethodCallNullCheckClassification implements
		INullCheckClassification {

	@Override
	public boolean accepts(NullCheck nullCheck) {
		BinaryExpr expression = (BinaryExpr) nullCheck.getNode()
				.getParentNode();
		return expression.getLeft() instanceof MethodCallExpr
				|| expression.getRight() instanceof MethodCallExpr;
	}

}
