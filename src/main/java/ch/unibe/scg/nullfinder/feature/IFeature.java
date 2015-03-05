package ch.unibe.scg.nullfinder.feature;

import java.util.Set;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.feature.reason.IReason;

public interface IFeature {

	Set<IReason> getReasons();

	IExtractor getExtractor();

}
