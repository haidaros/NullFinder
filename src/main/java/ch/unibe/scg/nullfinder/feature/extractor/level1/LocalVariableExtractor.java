package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.ArrayList;
import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.reason.FeatureReason;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;
import ch.unibe.scg.nullfinder.feature.reason.Reason;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;

public class LocalVariableExtractor extends AbstractDeclarationExtractor {

	@Override
	protected Feature safeExtract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		// TODO there is some dirty stuff going on here...
		CompilationUnit compilationUnit = nullCheck.getNode()
				.getCompilationUnit();
		Feature nameExtractorFeature = this.extractNameExtractorFeature(
				nullCheck, features);
		Reason reason = nameExtractorFeature.getReasons().iterator().next();
		NameExpr suspect = (NameExpr) ((NodeReason) reason).getNode()
				.getJavaParserNode();
		com.github.javaparser.ast.Node current = nullCheck.getNode()
				.getJavaParserNode().getParentNode();
		com.github.javaparser.ast.Node stop = nullCheck.getNode()
				.getJavaParserNode();
		// haha, a null nullCheck!
		while (current != null) {
			List<com.github.javaparser.ast.Node> children = current
					.getChildrenNodes();
			if (current instanceof ForStmt) {
				// reorder children
				ForStmt forStatement = (ForStmt) current;
				children = new ArrayList<>();
				if (forStatement.getInit() != null) {
					// fuck you...
					children.addAll(forStatement.getInit());
				}
				children.add(forStatement.getCompare());
				if (forStatement.getUpdate() != null) {
					// ...and fuck you
					children.addAll(forStatement.getUpdate());
				}
				children.add(forStatement.getBody());
			}
			for (com.github.javaparser.ast.Node child : children) {
				if (child == stop) {
					break;
				}
				if (child instanceof VariableDeclarationExpr) {
					try {
						VariableDeclarator variableDeclarator = this
								.findDeclaration(
										(VariableDeclarationExpr) child,
										suspect);
						Feature feature = new Feature(nullCheck, this);
						Node node = Node.getCachedNode(compilationUnit,
								variableDeclarator);
						feature.getReasons()
								.add(new FeatureReason(feature,
										nameExtractorFeature));
						feature.getReasons().add(new NodeReason(feature, node));
						return feature;
					} catch (DeclarationNotFoundException exception) {
						// noop
					}
				} else if (child instanceof ExpressionStmt) {
					Expression expression = ((ExpressionStmt) child)
							.getExpression();
					if (expression instanceof VariableDeclarationExpr) {
						try {
							VariableDeclarator variableDeclarator = this
									.findDeclaration(
											(VariableDeclarationExpr) expression,
											suspect);
							Feature feature = new Feature(nullCheck, this);
							Node node = Node.getCachedNode(compilationUnit,
									variableDeclarator);
							feature.getReasons().add(
									new FeatureReason(feature,
											nameExtractorFeature));
							feature.getReasons().add(
									new NodeReason(feature, node));
							return feature;
						} catch (DeclarationNotFoundException exception) {
							// noop
						}
					} else if (expression instanceof AssignExpr) {
						AssignExpr assignment = (AssignExpr) expression;
						if (assignment.getTarget() instanceof NameExpr) {
							NameExpr name = (NameExpr) assignment.getTarget();
							if (suspect.getName().equals(name.getName())) {
								Feature feature = new Feature(nullCheck, this);
								Node node = Node.getCachedNode(compilationUnit,
										name);
								feature.getReasons().add(
										new FeatureReason(feature,
												nameExtractorFeature));
								feature.getReasons().add(
										new NodeReason(feature, node));
								return feature;
							}
						}
						// TODO what if the target is not a name expression?
					}
				}
			}
			stop = current;
			current = current.getParentNode();
		}
		throw new UnextractableException(nullCheck);
	}

	protected VariableDeclarator findDeclaration(
			VariableDeclarationExpr declaration, NameExpr suspect)
			throws DeclarationNotFoundException {
		for (VariableDeclarator variable : declaration.getVars()) {
			if (suspect.getName().equals(variable.getId().getName())) {
				return variable;
			}
		}
		throw new DeclarationNotFoundException(suspect);
	}

}
