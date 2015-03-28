package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public abstract class AbstractNameDependentExtractor extends
		AbstractDependentExtractor {

	public AbstractNameDependentExtractor(int level) {
		super(level);
	}

	protected Feature extractNameExtractorFeature(NullCheck nullCheck,
			List<Feature> features) {
		assert this.meetsDependencies(nullCheck, features);
		return features.stream().filter(this::isExtractedByNameExtractor)
				.findFirst().get();
	}

	@Override
	protected boolean meetsDependencies(NullCheck nullCheck,
			List<Feature> features) {
		return features.stream().filter(this::isExtractedByNameExtractor)
				.count() == 1;
	}

	protected boolean isExtractedByNameExtractor(Feature feature) {
		return feature.getExtractor() instanceof NameExtractor;
	}

}
