package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

/**
 * Extract new features from a null check. All extractors with a lower level
 * will already be executed when this extractor is started.
 */
public interface IExtractor {

	/**
	 * Gets the level to operate on.
	 *
	 * @return The level
	 */
	int getLevel();

	/**
	 * Extracts new features from a null check.
	 *
	 * @param nullCheck
	 *            May have already extracted features from lower levels
	 * @return New features
	 */
	List<Feature> extract(NullCheck nullCheck);

}
