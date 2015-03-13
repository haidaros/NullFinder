package ch.unibe.scg.nullfinder.feature.extractor;


public abstract class AbstractExtractor implements IExtractor {

	protected int level;

	public AbstractExtractor(int level) {
		this.level = level;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

}
