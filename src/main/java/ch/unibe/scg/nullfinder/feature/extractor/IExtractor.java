package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.List;

import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

/**
 * Extract new Features from a NullCheck.
 */
public interface IExtractor {

	int getLevel();

	List<Feature> extract(NullCheck nullCheck);

}
