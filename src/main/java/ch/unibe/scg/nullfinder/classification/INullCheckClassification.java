package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

/**
 * Must implement a constructor with a null check.
 */
public interface INullCheckClassification {

	/**
	 * Checks whether or not the specified null check can be classified.
	 * 
	 * @param check
	 * @return true if the specified null check can be classified, false
	 *         otherwise
	 */
	boolean accepts(NullCheck check);

	/**
	 * Gets the null check.
	 * 
	 * @return The null check.
	 */
	NullCheck getNullCheck();

}
