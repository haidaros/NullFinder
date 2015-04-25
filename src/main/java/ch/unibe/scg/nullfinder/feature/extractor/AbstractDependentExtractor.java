package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Collections;
import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public abstract class AbstractDependentExtractor extends AbstractExtractor {

	public AbstractDependentExtractor(int level) {
		super(level);
	}

	@Override
	public List<Feature> extract(NullCheck nullCheck) {
		if (!this.meetsDependencies(nullCheck)) {
			return Collections.emptyList();
		}
		return this.safeExtract(nullCheck);
	}

	abstract protected boolean meetsDependencies(NullCheck nullCheck);

	abstract protected List<Feature> safeExtract(NullCheck nullCheck);

}
