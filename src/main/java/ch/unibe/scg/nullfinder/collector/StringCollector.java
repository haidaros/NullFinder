package ch.unibe.scg.nullfinder.collector;

import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.NullCheckClassification;

public class StringCollector implements
		ICollector<NullCheckClassification, String> {

	@Override
	public String collect(NullCheckClassification classification) {
		NullCheck check = classification.getNullCheck();
		return CSVFormat.DEFAULT.format(
				check.getPath().toString(),
				check.getNode().getBeginLine(),
				check.getNode().getBeginColumn(),
				check.getNode().getParentNode().toString(),
				classification
						.getFeatures()
						.stream()
						.map(feature -> feature.getExtractor().getClass()
								.getName()).collect(Collectors.toSet())
						.toString());
	}

}
