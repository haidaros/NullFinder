package ch.unibe.scg.nullfinder;

import ch.unibe.scg.nullfinder.classifier.INullCheckClassifier;

public class NullCheckClassification {

	private NullCheck check;
	private INullCheckClassifier classifier;

	public NullCheckClassification(NullCheck check,
			INullCheckClassifier classifier) {
		this.check = check;
		this.classifier = classifier;
	}

	public NullCheck getNullCheck() {
		return this.check;
	}

	public INullCheckClassifier getClassifier() {
		return this.classifier;
	}

}
