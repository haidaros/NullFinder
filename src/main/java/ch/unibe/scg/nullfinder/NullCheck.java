package ch.unibe.scg.nullfinder;

import java.nio.file.Path;

import com.github.javaparser.ast.expr.NullLiteralExpr;

public class NullCheck {

	private Path path;
	private NullLiteralExpr node;

	public NullCheck(Path path, NullLiteralExpr node) {
		this.path = path;
		this.node = node;
	}

	public Path getPath() {
		return this.path;
	}

	public NullLiteralExpr getNode() {
		return node;
	}

	@Override
	public String toString() {
		return String.format("%s line %d column %d %s", this.path.toString(),
				this.node.getBeginLine(), this.node.getBeginColumn(), this.node
						.getParentNode().toString());
	}

}
