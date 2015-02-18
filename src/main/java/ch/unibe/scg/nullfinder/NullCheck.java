package ch.unibe.scg.nullfinder;

import com.github.javaparser.ast.expr.NullLiteralExpr;

public class NullCheck {

	private NullLiteralExpr node;

	public NullLiteralExpr getNode() {
		return node;
	}

	public NullCheck(NullLiteralExpr node) {
		this.node = node;
	}

}
