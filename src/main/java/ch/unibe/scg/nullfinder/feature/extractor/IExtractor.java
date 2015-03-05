package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Set;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;

public interface IExtractor {

	int getLevel();

	IFeature extract(NullCheck check, Set<IFeature> features)
			throws UnextractableException;

}
