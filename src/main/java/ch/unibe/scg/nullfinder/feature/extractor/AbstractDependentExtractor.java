package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Collections;
import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

/**
 * Depends on a specific null checks, e.g. that have already had extracted a
 * specific feature.
 */
public abstract class AbstractDependentExtractor extends AbstractExtractor {

	public AbstractDependentExtractor(int level) {
		super(level);
	}

	/**
	 * Answers an empty list if the dependencies are not met.
	 */
	@Override
	public List<Feature> extract(NullCheck nullCheck) {
		if (!this.meetsDependencies(nullCheck)) {
			return Collections.emptyList();
		}
		return this.safeExtract(nullCheck);
	}

	/**
	 * Checks if the specified null check meets the necessary dependencies.
	 *
	 * @param nullCheck
	 *            The null check to examine
	 * @return true if it does, false otherwise
	 */
	abstract protected boolean meetsDependencies(NullCheck nullCheck);

	/**
	 * Extracts new features from a null check, that meets the dependencies.
	 *
	 * @param nullCheck
	 *            May have already extracted features from lower levels
	 * @return New features
	 */
	abstract protected List<Feature> safeExtract(NullCheck nullCheck);

}
