package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.List;
import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.reason.FeatureReason;
import ch.unibe.scg.nullfinder.feature.reason.IReason;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.NameExpr;

public class ParameterExtractor extends AbstractDeclarationExtractor {

	@Override
	protected IFeature safeExtract(NullCheck check, Set<IFeature> features)
			throws UnextractableException {
		// TODO there is some dirty stuff going on here...
		IFeature feature = this.extractNameExtractorFeature(check, features);
		IReason reason = feature.getReasons().iterator().next();
		NameExpr suspect = (NameExpr) ((NodeReason) reason).getNode();
		Node current = check.getNode().getParentNode();
		// haha, a null check!
		while (current != null) {
			if (current instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) current;
				// parameters are null for methods taking no arguments
				if (method.getParameters() != null) {
					try {
						Parameter parameter = this.findDeclaration(
								method.getParameters(), suspect);
						return this.buildFeature(new FeatureReason(feature),
								new NodeReason(parameter));
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
								constructor.getParameters(), suspect);
						return this.buildFeature(new FeatureReason(feature),
								new NodeReason(parameter));
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				}
			}
			current = current.getParentNode();
		}
		throw new UnextractableException(check);
	}

	protected Parameter findDeclaration(List<Parameter> parameters,
			NameExpr suspect) throws DeclarationNotFoundException {
		for (Parameter parameter : parameters) {
			if (suspect.getName().equals(parameter.getId().getName())) {
				return parameter;
			}
		}
		throw new DeclarationNotFoundException(suspect);
	}

}
