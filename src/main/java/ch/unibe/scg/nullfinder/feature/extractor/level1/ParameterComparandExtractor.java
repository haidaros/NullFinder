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
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

/**
 * Tries to find the declaration of a parameter with the name of the variable
 * null is compared against.
 *
 * @see ParameterComparandExtractor#extract(NullCheck) For a description of the
 *      used strategy and its short-comings.
 */
public class ParameterComparandExtractor extends
		AbstractVariableComparandDependentExtractor {

	public static class ExtractingVisitor extends ConstrainedVisitor<String> {

		protected List<Parameter> parameters;
		protected BodyDeclaration bodyDeclaration;

		public ExtractingVisitor() {
			super();
			this.parameters = new ArrayList<>();
		}

		public List<Parameter> getParameters() {
			return this.parameters;
		}

		@Override
		public void visit(Parameter parameter, String name) {
			if (name.equals(parameter.getId().getName())) {
				this.parameters.add(parameter);
			}
			super.visit(parameter, name);
		}

		@Override
		protected boolean shouldAscendFrom(com.github.javaparser.ast.Node node,
				String name) {
			if (node instanceof MethodDeclaration
					|| node instanceof ConstructorDeclaration) {
				this.bodyDeclaration = (BodyDeclaration) node;
				return false;
			}
			return true;
		}

		@Override
		protected boolean shouldDescendInto(
				com.github.javaparser.ast.Node node, String name) {
			if (node instanceof BodyDeclaration) {
				return node == this.bodyDeclaration;
			}
			return false;
		}

	}

	public ParameterComparandExtractor() {
		super(1);
	}

	/**
	 * Searches for a parameter declaration for the variable null is compared
	 * against in the code above the comparison. The tree is traversed upwards
	 * until the current body is left.
	 *
	 * <NOTE>This will only match true positives, but in the case of nested
	 * bodies - e.g. inner classes or anonymous classes - it may produce false
	 * negatives.</NOTE>
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
				.getParameters()
				.stream()
				.map(parameter -> this
						.getFeatureBuilder(nullCheck,
								parameter.getClass().getName())
						.addNodeReason(parameter)
						.addFeatureReason(variableFeature).getEntity())
				.collect(Collectors.toList());
	}

}
