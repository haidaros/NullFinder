package ch.unibe.scg.nullfinder.jpa.entity.builder;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public class NullCheckBuilder extends AbstractEntityBuilder<NullCheck> {

	protected EntityConnector entityConnector;

	public NullCheckBuilder(EntityConnector entityConnector, NullCheck nullCheck) {
		super(nullCheck);
		this.entityConnector = entityConnector;
	}

	public NullCheckBuilder(NullCheck nullCheck) {
		this(EntityConnector.INSTANCE, nullCheck);
	}

	public FeatureBuilder addFeature(IExtractor extractor) {
		Feature feature = this.entityConnector.createAndConnectFeature(
				this.entity, extractor);
		return new FeatureBuilder(this.entityConnector, feature);
	}

}
