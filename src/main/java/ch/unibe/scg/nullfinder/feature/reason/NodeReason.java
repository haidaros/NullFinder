package ch.unibe.scg.nullfinder.feature.reason;

import com.github.javaparser.ast.Node;

public class NodeReason implements IReason {

	protected Node node;

	public NodeReason(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}

}
