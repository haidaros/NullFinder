package ch.unibe.scg.nullfinder.feature.reason;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ch.unibe.scg.nullfinder.feature.Feature;

@Entity
public class FeatureReason extends Reason {

	@ManyToOne(optional = false)
	@JoinColumn(name = "reasonFeatureId", nullable = false)
	protected Feature reasonFeature;

	public FeatureReason(Feature feature, Feature reasonFeature) {
		super(feature);
		this.reasonFeature = reasonFeature;
	}

	/**
	 * Spring Data needs this.
	 */
	protected FeatureReason() {
		super();
	}

}
