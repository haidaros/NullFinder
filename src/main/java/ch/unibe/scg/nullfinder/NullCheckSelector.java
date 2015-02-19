package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NullCheckSelector {

	public static class NullCheckVisitor extends VoidVisitorAdapter<Path> {

		private Collection<NullCheck> checks;

		public NullCheckVisitor() {
			super();
			this.checks = new ArrayList<NullCheck>();
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

	public Stream<NullCheck> selectAll(Path path) throws ParseException, IOException {
		CompilationUnit compilationUnit = JavaParser.parse(path.toFile());
		NullCheckVisitor visitor = new NullCheckVisitor();
		visitor.visit(compilationUnit, path);
		return visitor.getNullChecks().stream();
	}

}
