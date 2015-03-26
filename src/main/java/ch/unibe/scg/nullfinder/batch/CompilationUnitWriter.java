package ch.unibe.scg.nullfinder.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;
import ch.unibe.scg.nullfinder.feature.reason.Reason;
import ch.unibe.scg.nullfinder.jpa.repository.NodeRepository;
import ch.unibe.scg.nullfinder.jpa.repository.NullCheckRepository;

@Component
public class CompilationUnitWriter implements
		ItemWriter<Entry<CompilationUnit, List<NullCheck>>> {

	@Autowired
	protected NodeRepository nodeRepository;
	@Autowired
	protected NullCheckRepository nullCheckRepository;

	@Override
	public void write(
			List<? extends Entry<CompilationUnit, List<NullCheck>>> entries)
			throws Exception {
		for (Entry<CompilationUnit, List<NullCheck>> entry : entries) {
			this.nodeRepository.save(entry.getKey());
			// TODO this connection is really ugly
			this.connect(entry.getValue());
			for (NullCheck nullCheck : entry.getValue()) {
				this.nullCheckRepository.save(nullCheck);
			}
		}
	}

	protected void connect(List<NullCheck> nullChecks) {
		List<Node> knownNodes = new ArrayList<>();
		for (NullCheck nullCheck : nullChecks) {
			this.connect(nullCheck, knownNodes);
		}
	}

	protected void connect(NullCheck nullCheck, List<Node> knownNodes) {
		nullCheck.setNode(this.getExistingOrNewNode(nullCheck.getNode(),
				knownNodes));
		for (Feature feature : nullCheck.getFeatures()) {
			for (Reason reason : feature.getReasons()) {
				// TODO ouch!
				if (reason instanceof NodeReason) {
					NodeReason nodeReason = (NodeReason) reason;
					nodeReason.setNode(this.getExistingOrNewNode(
							nodeReason.getNode(), knownNodes));
				}
			}
		}
	}

	protected Node getExistingOrNewNode(Node node, List<Node> knownNodes) {
		final Node possiblyPersistedNode = this.getExistingOrNewNode(node);
		Optional<Node> match = knownNodes
				.stream()
				.filter(candidate -> candidate
						.equalsJavaParserNode(possiblyPersistedNode
								.getJavaParserNode())).findFirst();
		if (match.isPresent()) {
			return match.get();
		}
		knownNodes.add(possiblyPersistedNode);
		return possiblyPersistedNode;
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
