package ch.unibe.scg.nullfinder.collector;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NullCheckCollector implements ICollector<Path, List<NullCheck>> {

	public static class NullCheckVisitor extends VoidVisitorAdapter<Path> {

		protected List<NullCheck> checks;

		public NullCheckVisitor() {
			super();
			this.checks = new LinkedList<>();
		}

		public List<NullCheck> getNullChecks() {
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
	public List<NullCheck> collect(Path path) throws ParseException,
			IOException {
		CompilationUnit compilationUnit = JavaParser.parse(path.toFile());
		NullCheckVisitor visitor = new NullCheckVisitor();
		visitor.visit(compilationUnit, path);
		return visitor.getNullChecks();
	}

}
