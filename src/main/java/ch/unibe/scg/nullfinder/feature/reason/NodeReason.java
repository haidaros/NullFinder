package ch.unibe.scg.nullfinder.feature.reason;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ch.unibe.scg.nullfinder.ast.Node;
import ch.unibe.scg.nullfinder.feature.Feature;

@Entity
public class NodeReason extends Reason {

	@ManyToOne(optional = false)
	@JoinColumn(name = "nodeId", nullable = false)
	protected Node node;

	public NodeReason(Feature feature, Node node) {
		super(feature);
		this.node = node;
	}

	/**
	 * Required by Spring Data.
	 */
	protected NodeReason() {
		super();
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
