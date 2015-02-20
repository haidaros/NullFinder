package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NameExpr;

public abstract class AbstractNameNullCheckClassification extends
		AbstractNullCheckClassification {

	public AbstractNameNullCheckClassification(NullCheck check) {
		super(check);
	}

	@Override
	public boolean accepts(NullCheck check) {
		BinaryExpr expression = (BinaryExpr) check.getNode().getParentNode();
		return expression.getLeft() instanceof NameExpr
				|| expression.getRight() instanceof NameExpr;
	}

}
