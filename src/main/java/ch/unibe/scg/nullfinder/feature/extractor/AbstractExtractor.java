package ch.unibe.scg.nullfinder.feature.extractor;

import java.util.Arrays;
import java.util.HashSet;

import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.reason.IReason;

public abstract class AbstractExtractor implements IExtractor {

	protected int level;

	public AbstractExtractor(int level) {
		this.level = level;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	protected Feature buildFeature(IReason... reasons) {
		return new Feature(this, new HashSet<>(Arrays.asList(reasons)));
	}

}
