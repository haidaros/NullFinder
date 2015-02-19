package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.BinaryExpr;

public class ArrayAccessNullCheckClassification extends
		AbstractNullCheckClassification {

	public ArrayAccessNullCheckClassification(NullCheck check) {
		super(check);
	}

	@Override
	public boolean accepts(NullCheck check) {
		BinaryExpr expression = (BinaryExpr) check.getNode().getParentNode();
		return expression.getLeft() instanceof ArrayAccessExpr
				|| expression.getRight() instanceof ArrayAccessExpr;
	}

}
