package ch.unibe.scg.nullfinder.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.reason.FeatureReason;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;
import ch.unibe.scg.nullfinder.feature.reason.Reason;
import ch.unibe.scg.nullfinder.jpa.repository.IFeatureRepository;
import ch.unibe.scg.nullfinder.jpa.repository.INodeRepository;
import ch.unibe.scg.nullfinder.jpa.repository.INullCheckRepository;
import ch.unibe.scg.nullfinder.jpa.repository.IReasonRepository;

public class NullCheckWriter implements ItemWriter<CompilationUnit> {

	@Autowired
	protected INodeRepository nodeRepository;
	@Autowired
	protected INullCheckRepository nullCheckRepository;
	@Autowired
	protected IFeatureRepository featureRepository;
	@Autowired
	protected IReasonRepository reasonRepository;

	@Override
	public void write(List<? extends CompilationUnit> compilationUnits)
			throws Exception {
		// TODO should there be null checks?
		for (CompilationUnit compilationUnit : compilationUnits) {
			this.saveCompilationUnit(compilationUnit);
		}
	}

	protected void saveCompilationUnit(CompilationUnit compilationUnit) {
		this.nodeRepository.save(compilationUnit);
	}

	protected void saveNullCheck(NullCheck nullCheck) {
		this.nodeRepository.save(nullCheck.getNode());
		this.nullCheckRepository.save(nullCheck);
	}

	protected void saveFeature(Feature feature) {
		this.featureRepository.save(feature);
		for (Reason reason : feature.getReasons()) {
			this.saveReason(reason);
		}
	}

	protected void saveReason(Reason reason) {
		// TODO naughty boy!
		if (reason instanceof FeatureReason) {
			this.saveFeatureReason((FeatureReason) reason);
		}
		if (reason instanceof NodeReason) {
			this.saveNodeReason((NodeReason) reason);
		}
	}

	protected void saveFeatureReason(FeatureReason reason) {
		this.reasonRepository.save(reason);
	}

	protected void saveNodeReason(NodeReason reason) {
		reason.setNode(this.getExistingOrNewNode(reason.getNode()));
		this.nodeRepository.save(reason.getNode());
		this.reasonRepository.save(reason);
	}

	protected Node getExistingOrNewNode(Node node) {
		List<Node> nodes = this.nodeRepository
				.findByCompilationUnitAndClassNameAndBeginLineAndBeginColumnAndEndLineAndEndColumn(
						node.getCompilationUnit(), node.getClassName(),
						node.getBeginLine(), node.getBeginColumn(),
						node.getEndLine(), node.getEndColumn());
		assert nodes.size() <= 1;
		if (!nodes.isEmpty()) {
			return nodes.get(0);
		}
		return node;
	}
}
