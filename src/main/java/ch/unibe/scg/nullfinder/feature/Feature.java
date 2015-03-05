package ch.unibe.scg.nullfinder.feature;

import java.util.Set;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.feature.reason.IReason;

public class Feature implements IFeature {

	protected IExtractor extractor;
	protected Set<IReason> reasons;

	public Feature(IExtractor extractor, Set<IReason> reasons) {
		this.extractor = extractor;
		this.reasons = reasons;
	}

	@Override
	public IExtractor getExtractor() {
		return this.extractor;
	}

	@Override
	public Set<IReason> getReasons() {
		return this.reasons;
	}

}
