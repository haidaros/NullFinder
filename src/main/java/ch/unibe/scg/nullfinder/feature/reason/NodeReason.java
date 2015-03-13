package ch.unibe.scg.nullfinder.feature.reason;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import ch.unibe.scg.nullfinder.feature.Feature;

import com.github.javaparser.ast.Node;

@Entity
public class NodeReason extends Reason {

	@Columns(columns = { @Column(name = "className", nullable = false),
			@Column(name = "beginLine", nullable = false),
			@Column(name = "beginColumn", nullable = false),
			@Column(name = "endLine", nullable = false),
			@Column(name = "endColumn", nullable = false) })
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.NodeType")
	protected Node node;

	public NodeReason(Feature feature, Node node) {
		super(feature);
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}

	/**
	 * Spring Data needs this.
	 */
	protected NodeReason() {
		super();
	}

}
