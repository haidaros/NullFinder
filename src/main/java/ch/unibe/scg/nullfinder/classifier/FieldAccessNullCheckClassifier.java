package ch.unibe.scg.nullfinder.classifier;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;

public class FieldAccessNullCheckClassifier implements INullCheckClassifier {

	@Override
	public boolean accepts(NullCheck check) {
		BinaryExpr expression = (BinaryExpr) check.getNode().getParentNode();
		return expression.getLeft() instanceof FieldAccessExpr
				|| expression.getRight() instanceof FieldAccessExpr;
	}

}
