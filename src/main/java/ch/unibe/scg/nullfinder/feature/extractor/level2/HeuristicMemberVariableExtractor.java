package ch.unibe.scg.nullfinder.feature.extractor.level2;

import java.util.List;

import ch.unibe.scg.nullfinder.feature.extractor.AbstractVariableDependentExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public class HeuristicMemberVariableExtractor extends
		AbstractVariableDependentExtractor {

	public HeuristicMemberVariableExtractor() {
		super(2);
	}

	@Override
	protected Feature safeExtract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException {
		Feature variableExtractorFeature = this
				.extractVariableExtractorFeature(nullCheck, features);
		return this.addFeature(nullCheck)
				.addFeatureReason(variableExtractorFeature).getEntity();
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
		return feature.getExtractor() instanceof AbstractVariableDependentExtractor;
	}

}
