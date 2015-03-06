package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;

public abstract class AbstractDeclarationExtractor extends
		AbstractLevel1Extractor {

	protected IFeature extractNameExtractorFeature(NullCheck check,
			Set<IFeature> features) {
		assert this.meetsDependencies(check, features);
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.findFirst().get();
	}

	@Override
	protected boolean meetsDependencies(NullCheck check, Set<IFeature> features) {
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.count() == 1;
	}

}
