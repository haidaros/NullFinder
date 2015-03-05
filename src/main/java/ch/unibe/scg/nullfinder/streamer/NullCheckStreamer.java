package ch.unibe.scg.nullfinder.streamer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NullCheckStreamer implements IStreamer<Path, NullCheck> {

	public static class NullCheckVisitor extends VoidVisitorAdapter<Path> {

		protected Collection<NullCheck> checks;

		public NullCheckVisitor() {
			super();
			this.checks = new ArrayList<>();
		}

		public Collection<NullCheck> getNullChecks() {
			return this.checks;
		}

		@Override
		public void visit(NullLiteralExpr node, Path path) {
			if (node.getParentNode() instanceof BinaryExpr) {
				this.checks.add(new NullCheck(path, node));
			}
		}

	}

	@Override
	public Stream<NullCheck> stream(Path path) throws ParseException,
			IOException {
		CompilationUnit compilationUnit = JavaParser.parse(path.toFile());
		NullCheckVisitor visitor = new NullCheckVisitor();
		visitor.visit(compilationUnit, path);
		return visitor.getNullChecks().stream();
	}

}
