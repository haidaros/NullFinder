package ch.unibe.scg.nullfinder.batch;

import java.util.LinkedList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@Component
public class CompilationUnitProcessor implements
ItemProcessor<CompilationUnit, List<NullCheck>> {

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
			super.visit(javaParserNode, compilationUnit);
			if (javaParserNode.getParentNode() instanceof BinaryExpr) {
				Node node = Node.getCachedNode(compilationUnit, javaParserNode);
				NullCheck nullCheck = new NullCheck(node);
				node.setNullCheck(nullCheck);
				this.nullChecks.add(nullCheck);
			}
		}

	}

	@Override
	public List<NullCheck> process(CompilationUnit compilationUnit) {
		NullCheckNodeVisitor visitor = new NullCheckNodeVisitor();
		visitor.visit(compilationUnit.getJavaParserCompilationUnit(),
				compilationUnit);
		return visitor.getChecks();
	}

}
