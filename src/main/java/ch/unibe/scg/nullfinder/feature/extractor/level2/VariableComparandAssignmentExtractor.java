package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VariableComparandAssignmentExtractor extends
		AbstractVariableComparandDependentExtractor {

	public static class AssignmentVisitor extends VoidVisitorAdapter<String> {

		protected List<com.github.javaparser.ast.Node> assigments;

		public AssignmentVisitor() {
			super();
			this.assigments = new LinkedList<>();
		}

		public List<com.github.javaparser.ast.Node> getAssignments() {
			return this.assigments;
		}

		@Override
		public void visit(AssignExpr assignment, String name) {
			super.visit(assignment, name);
			if (!assignment.getTarget().toString().equals(name)) {
				return;
			}
			this.assigments.add(assignment);
		}

	}

	public VariableComparandAssignmentExtractor() {
		super(2);
	}

	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck,
			List<Feature> features) {
		CompilationUnit compilationUnit = nullCheck.getNode()
				.getCompilationUnit();
		Feature variableFeature = this.extractVariableFeature(nullCheck,
				features);
		Node variableNode = this.extractVariableNode(nullCheck, features);
		AssignmentVisitor visitor = new AssignmentVisitor();
		visitor.visit(compilationUnit.getJavaParserCompilationUnit(),
				this.getVariableName(variableNode));
		// TODO is there a useful non-empty manifestation?
		return visitor
				.getAssignments()
				.stream()
				.map(assignment -> this.getFeatureBuilder(nullCheck, "")
						.addFeatureReason(variableFeature)
						.addNodeReason(assignment).getEntity())
				.collect(Collectors.toList());
	}

}
