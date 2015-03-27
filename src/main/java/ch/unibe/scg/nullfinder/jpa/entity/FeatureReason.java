package ch.unibe.scg.nullfinder.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	 * Required by Spring Data.
	 */
	protected FeatureReason() {
		super();
	}

	public Feature getReasonFeature() {
		return this.reasonFeature;
	}

	public void setReasonFeature(Feature reasonFeature) {
		this.reasonFeature = reasonFeature;
	}

}
