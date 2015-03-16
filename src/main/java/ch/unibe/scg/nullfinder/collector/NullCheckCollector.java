package ch.unibe.scg.nullfinder.collector;

import java.util.LinkedList;
import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NullCheckCollector implements
		ICollector<CompilationUnit, List<NullCheck>> {

	public static class NullCheckNodeVisitor extends
			VoidVisitorAdapter<CompilationUnit> {

		protected List<NullCheck> nullChecks;

		public NullCheckNodeVisitor() {
			super();
			this.nullChecks = new LinkedList<>();
		}

		public List<NullCheck> getChecks() {
			return this.nullChecks;
		}

		@Override
		public void visit(NullLiteralExpr javaParserNode,
				CompilationUnit compilationUnit) {
			if (javaParserNode.getParentNode() instanceof BinaryExpr) {
				this.nullChecks.add(new NullCheck(new Node(compilationUnit,
						javaParserNode)));
			}
		}

	}

	@Override
	public List<NullCheck> collect(CompilationUnit compilationUnit) {
		NullCheckNodeVisitor visitor = new NullCheckNodeVisitor();
		visitor.visit(compilationUnit.getJavaParserCompilationUnit(),
				compilationUnit);
		return visitor.getChecks();
	}

}
