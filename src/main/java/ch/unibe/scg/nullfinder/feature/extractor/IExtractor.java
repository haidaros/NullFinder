package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;

public interface IExtractor {

	int getLevel();

	void setLevel(int level);

	Feature extract(NullCheck check, List<Feature> features)
			throws UnextractableException;

}
