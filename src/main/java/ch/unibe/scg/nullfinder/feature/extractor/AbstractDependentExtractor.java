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
		if (!this.meetsDependencies(check, features)) {
			throw new UnextractableException(check);
		}
		return this.safeExtract(check, features);
	}

	abstract protected boolean meetsDependencies(NullCheck check,
			Set<IFeature> features);

	abstract protected IFeature safeExtract(NullCheck check,
			Set<IFeature> features) throws UnextractableException;

}
