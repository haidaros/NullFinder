package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.ast.ConstrainedVisitor;
import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;

/**
 * Tries to find assignments to the variable with the name of the variable null
 * is compared against.
 *
 * @see VariableComparandAssignmentExtractor#extract(NullCheck) For a
 *      description of the used strategy and its short-comings.
 */
public class VariableComparandAssignmentExtractor extends
		AbstractVariableComparandDependentExtractor {

	public static class ExtractingVisitor extends ConstrainedVisitor<String> {

		protected com.github.javaparser.ast.Node stop;
		protected List<com.github.javaparser.ast.Node> assignments;

		public ExtractingVisitor(com.github.javaparser.ast.Node stop) {
			super();
			this.stop = stop;
			this.assignments = new ArrayList<>();
		}

		public List<com.github.javaparser.ast.Node> getAssignments() {
			return this.assignments;
		}

		@Override
		public void visit(VariableDeclarator variableDeclarator, String name) {
			if (variableDeclarator.getInit() != null
					&& name.equals(variableDeclarator.getId().getName())) {
				this.assignments.add(variableDeclarator);
			}
			super.visit(variableDeclarator, name);
		}

		@Override
		public void visit(AssignExpr assignment, String name) {
			if (name.equals(assignment.getTarget().toString())) {
				this.assignments.add(assignment);
			}
			super.visit(assignment, name);
		}

		@Override
		protected boolean shouldAscendFrom(com.github.javaparser.ast.Node node,
				String name) {
			return !(node instanceof BodyDeclaration);
		}

		@Override
		protected boolean shouldDescendInto(
				com.github.javaparser.ast.Node node, String name) {
			return node != this.stop;
		}

	}

	public VariableComparandAssignmentExtractor() {
		super(2);
	}

	/**
	 * Searches for a assignments to for the variable null is compared against
	 * in the code above the comparison. The tree is traversed upwards until the
	 * current body is left.
	 *
	 * <NOTE>This will only match true positives, but in the case of nested
	 * bodies - e.g. inner classes or anonymous classes - it may produce false
	 * negatives</NOTE>
	 */
	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck) {
		Feature variableFeature = this.extractVariableFeature(nullCheck);
		Node variableNode = this.extractVariableNode(nullCheck);
		String variableName = this.getVariableName(variableNode);
		com.github.javaparser.ast.Node stop = nullCheck.getNode()
				.getJavaParserNode();
		ExtractingVisitor extractingVisitor = new ExtractingVisitor(stop);
		extractingVisitor.startOn(stop, variableName);
		return extractingVisitor
				.getAssignments()
				.stream()
				.map(assignment -> this
						.getFeatureBuilder(nullCheck,
								assignment.getClass().getName())
						.addNodeReason(assignment)
						.addFeatureReason(variableFeature).getEntity())
				.collect(Collectors.toList());
	}

}
