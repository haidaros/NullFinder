package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;

public interface IExtractor {

	int getLevel();

	Feature extract(NullCheck check, Set<Feature> features)
			throws UnextractableException;

}
