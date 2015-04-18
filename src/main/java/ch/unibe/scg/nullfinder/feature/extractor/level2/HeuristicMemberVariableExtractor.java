package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableComparandDependentExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public class HeuristicMemberVariableExtractor extends
		AbstractVariableComparandDependentExtractor {

	public HeuristicMemberVariableExtractor() {
		super(2);
	}

	@Override
	protected List<Feature> safeExtract(NullCheck nullCheck,
			List<Feature> features) {
		Feature variableFeature = this.extractVariableFeature(nullCheck,
				features);
		// TODO is there a useful non-empty manifestation?
		return this.getFeatures(this.getFeatureBuilder(nullCheck, "")
				.addFeatureReason(variableFeature).getEntity());
	}

	@Override
	protected boolean meetsDependencies(NullCheck nullCheck,
			List<Feature> features) {
		return super.meetsDependencies(nullCheck, features)
				&& features.stream()
						.filter(this::isExtractedByDeclarationExtractor)
						.count() == 0;
	}

	protected boolean isExtractedByDeclarationExtractor(Feature feature) {
		return feature.getExtractor() instanceof AbstractVariableComparandDependentExtractor;
	}

}
