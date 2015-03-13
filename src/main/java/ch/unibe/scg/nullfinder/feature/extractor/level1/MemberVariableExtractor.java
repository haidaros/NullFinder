package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.reason.FeatureReason;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;
import ch.unibe.scg.nullfinder.feature.reason.Reason;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

public class MemberVariableExtractor extends AbstractDeclarationExtractor {

	@Override
	protected Feature safeExtract(NullCheck check, List<Feature> features)
			throws UnextractableException {
		// TODO there is some dirty stuff going on here...
		Feature nameExtractorFeature = this.extractNameExtractorFeature(check,
				features);
		Reason reason = nameExtractorFeature.getReasons().iterator().next();
		NameExpr suspect = (NameExpr) ((NodeReason) reason).getNode();
		Node current = check.getNode().getParentNode();
		while (current != null) {
			if (current instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) current;
				try {
					VariableDeclarator variableDeclarator = this
							.findDeclaration(clazz.getMembers(), suspect);
					Feature feature = new Feature(check, this);
					feature.getReasons().add(
							new FeatureReason(feature, nameExtractorFeature));
					feature.getReasons().add(
							new NodeReason(feature, variableDeclarator));
					return feature;
				} catch (DeclarationNotFoundException exception) {
					// noop
				}
			} else if (current instanceof ObjectCreationExpr) {
				ObjectCreationExpr objectCreation = (ObjectCreationExpr) current;
				if (objectCreation.getAnonymousClassBody() != null) {
					try {
						VariableDeclarator variableDeclarator = this
								.findDeclaration(
										objectCreation.getAnonymousClassBody(),
										suspect);
						Feature feature = new Feature(check, this);
						feature.getReasons()
								.add(new FeatureReason(feature,
										nameExtractorFeature));
						feature.getReasons().add(
								new NodeReason(feature, variableDeclarator));
						return feature;
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				}
			}
			current = current.getParentNode();
		}
		throw new UnextractableException(check);
	}

	protected VariableDeclarator findDeclaration(List<BodyDeclaration> bodies,
			NameExpr suspect) throws DeclarationNotFoundException {
		for (BodyDeclaration body : bodies) {
			if (body instanceof FieldDeclaration) {
				FieldDeclaration field = (FieldDeclaration) body;
				for (VariableDeclarator variable : field.getVariables()) {
					if (suspect.getName().equals(variable.getId().getName())) {
						return variable;
					}
				}
			}
		}
		throw new DeclarationNotFoundException(suspect);
	}

}
