package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.Collections;
import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

public class ParameterComparandExtractor extends
		AbstractVariableComparandDependentExtractor {

	public ParameterComparandExtractor() {
		super(1);
	}

	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck) {
		// TODO there is some dirty stuff going on here...
		Feature variableFeature = this.extractVariableFeature(nullCheck);
		Node variableNode = this.extractVariableNode(nullCheck);
		com.github.javaparser.ast.Node current = nullCheck.getNode()
				.getJavaParserNode().getParentNode();
		// haha, a null nullCheck!
		while (current != null) {
			if (current instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) current;
				// parameters are null for methods taking no arguments
				if (method.getParameters() != null) {
					try {
						Parameter parameter = this.findDeclaration(
								method.getParameters(), variableNode);
						return this.getFeatures(this
								.getFeatureBuilder(nullCheck,
										MethodDeclaration.class.getName())
								.addNodeReason(parameter)
								.addFeatureReason(variableFeature).getEntity());
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				}
			} else if (current instanceof ConstructorDeclaration) {
				ConstructorDeclaration constructor = (ConstructorDeclaration) current;
				// parameters are null for constructors taking no arguments
				if (constructor.getParameters() != null) {
					try {
						Parameter parameter = this.findDeclaration(
								constructor.getParameters(), variableNode);
						return this.getFeatures(this
								.getFeatureBuilder(nullCheck,
										ConstructorDeclaration.class.getName())
								.addNodeReason(parameter)
								.addFeatureReason(variableFeature).getEntity());
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				}
			}
			if (current instanceof MethodDeclaration
					|| current instanceof ConstructorDeclaration) {
				break;
			}
			current = current.getParentNode();
		}
		return Collections.emptyList();
	}

	protected Parameter findDeclaration(List<Parameter> parameters,
			Node variableExtractorNode) throws DeclarationNotFoundException {
		for (Parameter parameter : parameters) {
			if (this.getVariableName(variableExtractorNode).equals(
					parameter.getId().getName())) {
				return parameter;
			}
		}
		throw new DeclarationNotFoundException(variableExtractorNode);
	}

}
