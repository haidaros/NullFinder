package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;

public abstract class AbstractDependentExtractor extends AbstractExtractor {

	public AbstractDependentExtractor(int level) {
		super(level);
	}

	@Override
	public IFeature extract(NullCheck check, Set<IFeature> features)
			throws UnextractableException {
		Set<IFeature> dependingFeatures = this
				.extractDependingFeatures(features);
		if (!this.meetsDependencies(check, dependingFeatures)) {
			throw new UnextractableException(check);
		}
		return this.safeExtract(check, dependingFeatures);
	}

	abstract protected Set<IFeature> extractDependingFeatures(
			Set<IFeature> features);

	abstract protected boolean meetsDependencies(NullCheck check,
			Set<IFeature> dependingFeatures);

	abstract protected IFeature safeExtract(NullCheck check,
			Set<IFeature> dependingFeatures) throws UnextractableException;

}
