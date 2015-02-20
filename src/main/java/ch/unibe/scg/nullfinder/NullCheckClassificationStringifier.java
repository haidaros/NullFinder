package ch.unibe.scg.nullfinder;

import org.apache.commons.csv.CSVFormat;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;

public class NullCheckClassificationStringifier {

	public String stringify(INullCheckClassification classification) {
		NullCheck check = classification.getNullCheck();
		return CSVFormat.DEFAULT.format(check.getPath().toString(), check
				.getNode().getBeginLine(), check.getNode().getBeginColumn(),
				check.getNode().getParentNode().toString(), classification
						.getClass().getName());
	}

}
