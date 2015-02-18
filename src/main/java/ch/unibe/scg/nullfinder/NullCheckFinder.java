package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NullCheckFinder {

	public static class NullCheckVisitor extends VoidVisitorAdapter<Object> {

		Collection<NullCheck> checks;

		public NullCheckVisitor() {
			this.checks = new ArrayList<NullCheck>();
		}

		public Collection<NullCheck> getNullChecks() {
			return this.checks;
		}

		@Override
		public void visit(NullLiteralExpr node, Object argument) {
			if (node.getParentNode() instanceof BinaryExpr) {
				this.checks.add(new NullCheck(node));
			}
		}

	}

	public Collection<NullCheck> find(Path path) throws ParseException,
			IOException {
		CompilationUnit compilationUnit = JavaParser.parse(path.toFile());
		NullCheckVisitor visitor = new NullCheckVisitor();
		visitor.visit(compilationUnit, null);
		return visitor.getNullChecks();
	}

}
