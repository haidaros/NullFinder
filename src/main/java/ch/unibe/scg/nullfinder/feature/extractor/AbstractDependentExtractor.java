package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public abstract class AbstractDependentExtractor extends AbstractExtractor {

	public AbstractDependentExtractor(int level) {
		super(level);
	}

	@Override
	public Feature extract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		if (!this.meetsDependencies(nullCheck, features)) {
			throw new UnextractableException(nullCheck);
		}
		return this.safeExtract(nullCheck, features);
	}

	abstract protected boolean meetsDependencies(NullCheck nullCheck,
			List<Feature> features);

	abstract protected Feature safeExtract(NullCheck nullCheck,
			List<Feature> features) throws UnextractableException;

}
