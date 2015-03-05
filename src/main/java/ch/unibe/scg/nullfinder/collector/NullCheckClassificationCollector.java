package ch.unibe.scg.nullfinder.collector;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.NullCheckClassification;
import ch.unibe.scg.nullfinder.UnclassifiableException;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;

public class NullCheckClassificationCollector implements
		ICollector<NullCheck, NullCheckClassification> {

	protected FeatureCollector featureCollector;

	public NullCheckClassificationCollector(FeatureCollector featureCollector) {
		this.featureCollector = featureCollector;
	}

	@Override
	public NullCheckClassification collect(NullCheck check)
			throws UnclassifiableException {
		try {
			Set<IFeature> features = this.featureCollector.collect(check);
			return new NullCheckClassification(check, features);
		} catch (UnextractableException exception) {
			throw new UnclassifiableException(check, exception);
		}
	}

}
