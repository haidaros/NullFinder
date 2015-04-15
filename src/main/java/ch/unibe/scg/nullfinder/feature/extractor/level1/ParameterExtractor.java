package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableDependentExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.Node;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

public class ParameterExtractor extends AbstractVariableDependentExtractor {

	public ParameterExtractor() {
		super(1);
	}

	@Override
	protected Feature safeExtract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		// TODO there is some dirty stuff going on here...
		Feature variableExtractorFeature = this
				.extractVariableExtractorFeature(nullCheck, features);
		Node variableExtractorNode = this.extractVariableExtractorNode(
				nullCheck, features);
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
								method.getParameters(), variableExtractorNode);
						return this.addFeature(nullCheck)
								.addNodeReason(parameter)
								.addFeatureReason(variableExtractorFeature)
								.getEntity();
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
								constructor.getParameters(),
								variableExtractorNode);
						return this.addFeature(nullCheck)
								.addNodeReason(parameter)
								.addFeatureReason(variableExtractorFeature)
								.getEntity();
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				}
			}
			current = current.getParentNode();
		}
		throw new UnextractableException(nullCheck);
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
