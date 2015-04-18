package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Arrays;
import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;
import ch.unibe.scg.nullfinder.jpa.entity.builder.FeatureBuilder;
import ch.unibe.scg.nullfinder.jpa.entity.builder.NullCheckBuilder;

public abstract class AbstractExtractor implements IExtractor {

	protected int level;

	public AbstractExtractor(int level) {
		this.level = level;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	protected FeatureBuilder getFeatureBuilder(NullCheck nullCheck,
			String manifestation) {
		return (new NullCheckBuilder(nullCheck))
				.addFeature(this, manifestation);
	}

	protected List<Feature> getFeatures(Feature... features) {
		return Arrays.asList(features);

	}

}
