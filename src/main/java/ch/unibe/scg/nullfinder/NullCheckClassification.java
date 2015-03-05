package ch.unibe.scg.nullfinder;

import java.util.Set;

import ch.unibe.scg.nullfinder.feature.IFeature;

public class NullCheckClassification {

	protected NullCheck check;
	protected Set<IFeature> features;

	public NullCheckClassification(NullCheck check, Set<IFeature> features) {
		this.check = check;
		this.features = features;
	}

	public NullCheck getNullCheck() {
		return this.check;
	}

	public Set<IFeature> getFeatures() {
		return this.features;
	}

}
