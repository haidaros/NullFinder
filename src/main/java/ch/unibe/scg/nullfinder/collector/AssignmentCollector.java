package ch.unibe.scg.nullfinder.collector;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AssignmentCollector {

	public static class AssignmentVisitor extends VoidVisitorAdapter<NameExpr> {

		private List<AssignExpr> assignments;

		public AssignmentVisitor() {
			super();
			this.assignments = new ArrayList<>();
		}

		public List<AssignExpr> getAssignments() {
			return this.assignments;
		}

		@Override
		public void visit(AssignExpr assignment, NameExpr suspect) {
			if (!(assignment.getTarget() instanceof NameExpr)) {
				return;
			}
			NameExpr name = (NameExpr) assignment.getTarget();
			if (!suspect.getName().equals(name.getName())) {
				return;
			}
			// TODO this
			this.assignments.add(assignment);
		}

	}

	public List<AssignExpr> collect(NameExpr name) {
		AssignmentVisitor visitor = new AssignmentVisitor();
		CompilationUnit root = this.getCompilationUnit(name);
		visitor.visit(root, name);
		return visitor.getAssignments();
	}

	private CompilationUnit getCompilationUnit(Node node) {
		while (node.getParentNode() != null) {
			node = node.getParentNode();
		}
		return (CompilationUnit) node;
	}

}
