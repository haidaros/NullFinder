package ch.unibe.scg.nullfinder.feature.reason;

import ch.unibe.scg.nullfinder.feature.IFeature;

public class FeatureReason implements IReason {

	protected IFeature feature;

	public FeatureReason(IFeature feature) {
		this.feature = feature;
	}

	public IFeature getFeature() {
		return this.feature;
	}

}
