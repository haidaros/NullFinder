package ch.unibe.scg.nullfinder.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@Component
public class CompilationUnitProcessor implements
		ItemProcessor<CompilationUnit, List<NullCheck>> {

	public static class ConditionalVisitor extends
			VoidVisitorAdapter<CompilationUnit> {

		protected Integer conditionals;

		public ConditionalVisitor() {
			super();
			this.conditionals = 0;
		}

		public Integer getConditionals() {
			return this.conditionals;
		}

		@Override
		public void visit(IfStmt javaParserNode, CompilationUnit compilationUnit) {
			super.visit(javaParserNode, compilationUnit);
			this.conditionals = this.conditionals + 1;
		}

		@Override
		public void visit(WhileStmt javaParserNode,
				CompilationUnit compilationUnit) {
			super.visit(javaParserNode, compilationUnit);
			this.conditionals = this.conditionals + 1;
		}

		@Override
		public void visit(DoStmt javaParserNode, CompilationUnit compilationUnit) {
			super.visit(javaParserNode, compilationUnit);
			this.conditionals = this.conditionals + 1;
		}

		@Override
		public void visit(ForStmt javaParserNode,
				CompilationUnit compilationUnit) {
			super.visit(javaParserNode, compilationUnit);
			this.conditionals = this.conditionals + 1;
		}

		@Override
		public void visit(AssertStmt javaParserNode,
				CompilationUnit compilationUnit) {
			super.visit(javaParserNode, compilationUnit);
			this.conditionals = this.conditionals + 1;
		}

	}

	public static class NullLiteralVisitor extends
			VoidVisitorAdapter<CompilationUnit> {

		protected List<NullCheck> nullChecks;

		public NullLiteralVisitor() {
			super();
			this.nullChecks = new ArrayList<>();
		}

		public List<NullCheck> getNullChecks() {
			return this.nullChecks;
		}

		@Override
		public void visit(NullLiteralExpr javaParserNode,
				CompilationUnit compilationUnit) {
			super.visit(javaParserNode, compilationUnit);
			if (!(javaParserNode.getParentNode() instanceof BinaryExpr)) {
				return;
			}
			Node node = Node.getCachedNode(compilationUnit, javaParserNode);
			NullCheck nullCheck = new NullCheck(node);
			node.setNullCheck(nullCheck);
			this.nullChecks.add(nullCheck);
		}

	}

	@Override
	public List<NullCheck> process(CompilationUnit compilationUnit)
			throws UnvisitableException {
		try {
			com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit = compilationUnit
					.getJavaParserCompilationUnit();
			ConditionalVisitor conditionalVisitor = new ConditionalVisitor();
			conditionalVisitor
					.visit(javaParserCompilationUnit, compilationUnit);
			Integer conditionals = conditionalVisitor.getConditionals();
			compilationUnit.setConditionals(conditionals);
			NullLiteralVisitor nullLiteralVisitor = new NullLiteralVisitor();
			nullLiteralVisitor
					.visit(javaParserCompilationUnit, compilationUnit);
			return nullLiteralVisitor.getNullChecks();
		} catch (NullPointerException exception) {
			// sometimes this happens in the visitor
			throw new UnvisitableException(compilationUnit, exception);
		}
	}

}
