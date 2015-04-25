package ch.unibe.scg.nullfinder.feature.extractor.level1;

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

/**
 * Tries to find the declaration of a local variable with the name of the
 * variable null is compared against.
 *
 * @see LocalVariableComparandExtractor#extract(NullCheck) For a description of
 *      the used strategy and its short-comings.
 */
public class LocalVariableComparandExtractor extends
		AbstractVariableComparandDependentExtractor {

	public static class ExtractingVisitor extends ConstrainedVisitor<String> {

		protected com.github.javaparser.ast.Node stop;
		protected List<VariableDeclarator> variableDeclarators;

		public ExtractingVisitor(com.github.javaparser.ast.Node stop) {
			super();
			this.stop = stop;
			this.variableDeclarators = new ArrayList<>();
		}

		public List<VariableDeclarator> getVariableDeclarators() {
			return this.variableDeclarators;
		}

		@Override
		public void visit(VariableDeclarator variableDeclarator, String name) {
			if (name.equals(variableDeclarator.getId().getName())) {
				this.variableDeclarators.add(variableDeclarator);
			}
			super.visit(variableDeclarator, name);
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

	public LocalVariableComparandExtractor() {
		super(1);
	}

	/**
	 * Searches for a declaration statement for the variable null is compared
	 * against in the code above the comparison. The tree is traversed upwards
	 * until the current body is left.
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
