package ch.unibe.scg.nullfinder.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Reason {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "featureId", nullable = false)
	protected Feature feature;

	public Reason(Feature feature) {
		this.feature = feature;
	}

	/**
	 * Required by Spring Data.
	 */
	protected Reason() {

	}

	public Feature getFeature() {
		return this.feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

}
