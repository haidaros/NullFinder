package ch.unibe.scg.nullfinder.classifier;

import ch.unibe.scg.nullfinder.NullCheck;

/**
 * Thrown when a null check can not be classified.
 */
public class UnclassifiableNullCheckException extends Exception {

	private static final long serialVersionUID = 1L;
	private NullCheck check;

	public UnclassifiableNullCheckException(NullCheck check) {
		super();
		this.check = check;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(), this.check.toString());
	}

}
