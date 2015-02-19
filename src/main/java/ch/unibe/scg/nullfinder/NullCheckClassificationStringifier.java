package ch.unibe.scg.nullfinder;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;

public class NullCheckClassificationStringifier {

	public String stringify(INullCheckClassification classification) {
		NullCheck check = classification.getNullCheck();
		return String.format("%s\t%d\t%d\t%s\t%s", check.getPath().toString(),
				check.getNode().getBeginLine(), check.getNode()
						.getBeginColumn(), check.getNode().getParentNode()
						.toString(), classification.getClass().getName());
	}

}
