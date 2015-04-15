package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractNameDependentExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NodeReason;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;
import ch.unibe.scg.nullfinder.jpa.entity.Reason;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AssignmentExtractor extends AbstractNameDependentExtractor {

	public static class AssignmentVisitor extends VoidVisitorAdapter<NameExpr> {

		protected static final Logger LOGGER = LogManager.getLogger();

		protected List<com.github.javaparser.ast.Node> assigments;

		public AssignmentVisitor() {
			super();
			this.assigments = new LinkedList<>();
		}

		public List<com.github.javaparser.ast.Node> getAssignments() {
			return this.assigments;
		}

		@Override
		public void visit(AssignExpr javaParserNode, NameExpr suspect) {
			super.visit(javaParserNode, suspect);
			if (!(javaParserNode.getTarget() instanceof NameExpr)) {
				LOGGER.warn(String
						.format("Assignment is not targeting NameExpr, is %1$s instead: %2$s",
								javaParserNode.getTarget().getClass()
										.toString(), javaParserNode.getTarget()
										.toString()));
				return;
			}
			NameExpr candidate = (NameExpr) javaParserNode.getTarget();
			if (!candidate.getName().equals(suspect.getName())) {
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
		Feature nameExtractorFeature = this.extractNameExtractorFeature(
				nullCheck, features);
		Reason reason = nameExtractorFeature.getReasons().iterator().next();
		NameExpr suspect = (NameExpr) ((NodeReason) reason).getNode()
				.getJavaParserNode();
		AssignmentVisitor visitor = new AssignmentVisitor();
		visitor.visit(compilationUnit.getJavaParserCompilationUnit(), suspect);
		List<com.github.javaparser.ast.Node> assignments = visitor
				.getAssignments();
		if (assignments.isEmpty()) {
			throw new UnextractableException(nullCheck);
		}
		return this.addFeature(nullCheck)
				.addFeatureReason(nameExtractorFeature)
				.addNodeReasons(assignments).getEntity();
	}

}
