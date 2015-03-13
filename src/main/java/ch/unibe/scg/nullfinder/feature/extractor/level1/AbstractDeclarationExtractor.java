package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;

public abstract class AbstractDeclarationExtractor extends
		AbstractLevel1Extractor {

	protected Feature extractNameExtractorFeature(NullCheck check,
			Set<Feature> features) {
		assert this.meetsDependencies(check, features);
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.findFirst().get();
	}

	@Override
	protected boolean meetsDependencies(NullCheck check, Set<Feature> features) {
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.count() == 1;
	}

}
