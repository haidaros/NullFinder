package ch.unibe.scg.nullfinder;

import com.github.javaparser.ast.expr.NullLiteralExpr;

public class NullCheck {

	private NullLiteralExpr node;

	public NullCheck(NullLiteralExpr node) {
		this.node = node;
	}

	public NullLiteralExpr getNode() {
		return node;
	}

}
