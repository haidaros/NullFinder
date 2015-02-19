package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

public class NullNullCheckClassification extends
		AbstractNullCheckClassification {

	public NullNullCheckClassification(NullCheck check) {
		super(check);
	}

	@Override
	public boolean accepts(NullCheck check) {
		return false;
	}

}
