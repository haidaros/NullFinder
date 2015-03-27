package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public abstract class AbstractDeclarationExtractor extends
		AbstractLevel1Extractor {

	protected Feature extractNameExtractorFeature(NullCheck nullCheck,
			List<Feature> features) {
		assert this.meetsDependencies(nullCheck, features);
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.findFirst().get();
	}

	@Override
	protected boolean meetsDependencies(NullCheck nullCheck, List<Feature> features) {
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.count() == 1;
	}

}
