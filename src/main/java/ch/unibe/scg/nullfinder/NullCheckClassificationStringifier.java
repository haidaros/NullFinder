package ch.unibe.scg.nullfinder;

import org.apache.commons.csv.CSVFormat;

public class NullCheckClassificationStringifier {

	public String stringify(NullCheckClassification classification) {
		NullCheck check = classification.getNullCheck();
		return CSVFormat.DEFAULT.format(check.getPath().toString(), check
				.getNode().getBeginLine(), check.getNode().getBeginColumn(),
				check.getNode().getParentNode().toString(), classification
						.getClass().getName());
	}

}
