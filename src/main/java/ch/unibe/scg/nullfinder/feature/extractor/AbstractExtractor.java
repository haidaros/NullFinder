package ch.unibe.scg.nullfinder.feature.extractor;

import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;
import ch.unibe.scg.nullfinder.jpa.entity.builder.FeatureBuilder;
import ch.unibe.scg.nullfinder.jpa.entity.builder.NullCheckBuilder;

/**
 * Provides utilities for building features.
 */
public abstract class AbstractExtractor implements IExtractor {

	protected int level;

	public AbstractExtractor(int level) {
		this.level = level;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	/**
	 * Gets a new feature builder for the specified null check and
	 * manifestation.
	 *
	 * @param nullCheck
	 *            The null check a new feature is added for
	 * @param manifestation
	 *            The manifestation of the new feature
	 * @return A feature builder
	 */
	protected FeatureBuilder getFeatureBuilder(NullCheck nullCheck,
			String manifestation) {
		return (new NullCheckBuilder(nullCheck))
				.addFeature(this, manifestation);
	}

}
