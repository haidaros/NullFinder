package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;

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
