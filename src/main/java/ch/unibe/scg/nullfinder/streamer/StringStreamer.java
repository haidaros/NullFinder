package ch.unibe.scg.nullfinder.streamer;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.NullCheckClassification;

public class StringStreamer implements
		IStreamer<NullCheckClassification, String> {

	@Override
	public Stream<String> stream(NullCheckClassification classification) {
		NullCheck check = classification.getNullCheck();
		return Stream.of(CSVFormat.DEFAULT.format(
				check.getPath().toString(),
				check.getNode().getBeginLine(),
				check.getNode().getBeginColumn(),
				check.getNode().getParentNode().toString(),
				classification
						.getFeatures()
						.stream()
						.map(feature -> feature.getExtractor().getClass()
								.getName()).collect(Collectors.toSet())
						.toString()));
	}

}
