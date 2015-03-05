package ch.unibe.scg.nullfinder.feature.extractor;

import ch.unibe.scg.nullfinder.NullCheck;

public class UnextractableException extends Exception {

	private static final long serialVersionUID = 1L;

	protected NullCheck check;

	public UnextractableException(NullCheck check) {
		super();
		this.check = check;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(), this.check.toString());
	}

}
