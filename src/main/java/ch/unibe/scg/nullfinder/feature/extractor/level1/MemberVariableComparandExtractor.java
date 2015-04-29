package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.ast.ConstrainedVisitor;
import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

/**
 * Tries to find the declaration of a member variable with the name of the
 * variable null is compared against.
 *
 * @see MemberVariableComparandExtractor#extract(NullCheck) For a description of
 *      the used strategy and its short-comings.
 */
public class MemberVariableComparandExtractor extends
		AbstractVariableComparandDependentExtractor {

	public static class ExtractingVisitor extends ConstrainedVisitor<String> {

		protected List<VariableDeclarator> variableDeclarators;
		protected TypeDeclaration typeDeclaration;

		public ExtractingVisitor() {
			super();
			this.variableDeclarators = new ArrayList<>();
		}

		public List<VariableDeclarator> getVariableDeclarators() {
			return this.variableDeclarators;
		}

		@Override
		public void visit(VariableDeclarator variableDeclarator, String name) {
			if (variableDeclarator.getParentNode() instanceof FieldDeclaration
					&& name.equals(variableDeclarator.getId().getName())) {
				this.variableDeclarators.add(variableDeclarator);
			}
			super.visit(variableDeclarator, name);
		}

		@Override
		protected boolean shouldAscendFrom(com.github.javaparser.ast.Node node,
				String name) {
			if (node instanceof TypeDeclaration) {
				this.typeDeclaration = (TypeDeclaration) node;
				return false;
			}
			return true;
		}

		@Override
		protected boolean shouldDescendInto(
				com.github.javaparser.ast.Node node, String name) {
			if (node instanceof TypeDeclaration) {
				return node == this.typeDeclaration;
			}
			return true;
		}

	}

	public MemberVariableComparandExtractor() {
		super(1);
	}

	/**
	 * Searches for a field declaration for the variable null is compared
	 * against in the code above the comparison. The tree is traversed upwards
	 * until the current body is left.
	 *
	 * <NOTE>This will only match true positives, but in the case of nested
	 * bodies - e.g. inner classes or anonymous classes - it may produce false
	 * negatives. Especially inherited member variables will not be
	 * matched.</NOTE>
	 */
	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck) {
		Feature variableFeature = this.extractVariableFeature(nullCheck);
		Node variableNode = this.extractVariableNode(nullCheck);
		String variableName = this.getVariableName(variableNode);
		com.github.javaparser.ast.Node stop = nullCheck.getNode()
				.getJavaParserNode();
		ExtractingVisitor extractingVisitor = new ExtractingVisitor();
		extractingVisitor.startOn(stop, variableName);
		return extractingVisitor
				.getVariableDeclarators()
				.stream()
				.map(variableDeclarator -> this
						.getFeatureBuilder(nullCheck,
								variableDeclarator.getClass().getName())
						.addNodeReason(variableDeclarator)
						.addFeatureReason(variableFeature).getEntity())
				.collect(Collectors.toList());
	}

}
