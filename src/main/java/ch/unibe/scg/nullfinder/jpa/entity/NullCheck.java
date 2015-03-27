package ch.unibe.scg.nullfinder.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class NullCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "nullCheck")
	protected List<Feature> features;
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "nodeId", unique = true, nullable = false)
	protected Node node;

	public NullCheck(Node node) {
		this.features = new ArrayList<>();
		this.node = node;
	}

	/**
	 * Required by Spring Data.
	 */
	protected NullCheck() {

	}

	public List<Feature> getFeatures() {
		return this.features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
