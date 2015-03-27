package ch.unibe.scg.nullfinder.feature.extractor;

import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public class UnextractableException extends Exception {

	private static final long serialVersionUID = 1L;

	protected NullCheck nullCheck;

	public UnextractableException(NullCheck nullCheck) {
		super();
		this.nullCheck = nullCheck;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(), this.nullCheck.toString());
	}

}
