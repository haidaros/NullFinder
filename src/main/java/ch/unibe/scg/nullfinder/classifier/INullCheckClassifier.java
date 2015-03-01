package ch.unibe.scg.nullfinder.classifier;

import ch.unibe.scg.nullfinder.NullCheck;

public interface INullCheckClassifier {

	/**
	 * Checks whether or not the specified null check can be classified.
	 * 
	 * @param check
	 * @return true if the specified null check can be classified, false
	 *         otherwise
	 */
	boolean accepts(NullCheck check);

}
