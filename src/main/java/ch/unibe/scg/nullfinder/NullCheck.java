package ch.unibe.scg.nullfinder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ch.unibe.scg.nullfinder.ast.Node;

@Entity
public class NullCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "nodeId", nullable = false)
	protected Node node;

	public NullCheck(Node node) {
		this.node = node;
	}

	/**
	 * Required by Spring Data.
	 */
	protected NullCheck() {

	}

	public Node getNode() {
		return node;
	}

}
