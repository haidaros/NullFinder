package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.LinkedList;
import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableDependentExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AssignmentExtractor extends AbstractVariableDependentExtractor {

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
		public void visit(AssignExpr javaParserNode, String name) {
			super.visit(javaParserNode, name);
			if (!javaParserNode.getTarget().toString().equals(name)) {
				return;
			}
			this.assigments.add(javaParserNode);
		}

	}

	public AssignmentExtractor() {
		super(2);
	}

	@Override
	protected Feature safeExtract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		CompilationUnit compilationUnit = nullCheck.getNode()
				.getCompilationUnit();
		Feature variableExtractorFeature = this
				.extractVariableExtractorFeature(nullCheck, features);
		Node variableExtractorNode = this.extractVariableExtractorNode(
				nullCheck, features);
		AssignmentVisitor visitor = new AssignmentVisitor();
		visitor.visit(compilationUnit.getJavaParserCompilationUnit(),
				this.getVariableName(variableExtractorNode));
		List<com.github.javaparser.ast.Node> assignments = visitor
				.getAssignments();
		if (assignments.isEmpty()) {
			throw new UnextractableException(nullCheck);
		}
		return this.addFeature(nullCheck)
				.addFeatureReason(variableExtractorFeature)
				.addNodeReasons(assignments).getEntity();
	}

}
