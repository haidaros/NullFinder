package ch.unibe.scg.nullfinder.feature.extractor.level1;

import java.util.Set;
import java.util.stream.Collectors;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;

public abstract class AbstractDeclarationExtractor extends
		AbstractLevel1Extractor {

	@Override
	protected Set<IFeature> extractDependingFeatures(Set<IFeature> features) {
		return features
				.stream()
				.filter(feature -> feature.getExtractor() instanceof NameExtractor)
				.collect(Collectors.toSet());
	}

	@Override
	protected boolean meetsDependencies(NullCheck check,
			Set<IFeature> dependingFeatures) {
		return dependingFeatures.size() == 1;
	}

}
