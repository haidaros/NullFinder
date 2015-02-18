package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

/**
 * Must implement an empty constructor.
 */
public interface INullCheckClassification {

	/**
	 * Checks whether or not the specified null check can be classified.
	 * 
	 * @param nullCheck
	 * @return true if the specified null check can be classified, false
	 *         otherwise
	 */
	boolean accepts(NullCheck nullCheck);

}
