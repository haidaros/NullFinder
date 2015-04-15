package ch.unibe.scg.nullfinder.feature.extractor;

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

	protected FeatureBuilder addFeature(NullCheck nullCheck) {
		return (new NullCheckBuilder(nullCheck)).addFeature(this);
	}

}
