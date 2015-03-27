package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public interface IExtractor {

	int getLevel();

	void setLevel(int level);

	Feature extract(NullCheck nullCheck, List<Feature> features)
			throws UnextractableException;

}
