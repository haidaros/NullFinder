package ch.unibe.scg.nullfinder.feature.reason;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ch.unibe.scg.nullfinder.feature.Feature;

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

}
