package ch.unibe.scg.nullfinder.classification;

import ch.unibe.scg.nullfinder.NullCheck;

public abstract class AbstractNullCheckClassification implements
		INullCheckClassification {

	private NullCheck check;

	public AbstractNullCheckClassification(NullCheck check) {
		this.check = check;
	}

	@Override
	public NullCheck getNullCheck() {
		return this.check;
	}

}
