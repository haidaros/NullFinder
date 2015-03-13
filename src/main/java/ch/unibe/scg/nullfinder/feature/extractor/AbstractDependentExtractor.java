package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;

public abstract class AbstractDependentExtractor extends AbstractExtractor {

	public AbstractDependentExtractor(int level) {
		super(level);
	}

	@Override
	public Feature extract(NullCheck check, Set<Feature> features)
			throws UnextractableException {
		if (!this.meetsDependencies(check, features)) {
			throw new UnextractableException(check);
		}
		return this.safeExtract(check, features);
	}

	abstract protected boolean meetsDependencies(NullCheck check,
			Set<Feature> features);

	abstract protected Feature safeExtract(NullCheck check,
			Set<Feature> features) throws UnextractableException;

}
