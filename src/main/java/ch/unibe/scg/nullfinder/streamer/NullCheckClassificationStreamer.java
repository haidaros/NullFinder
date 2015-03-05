package ch.unibe.scg.nullfinder.streamer;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.NullCheckClassification;
import ch.unibe.scg.nullfinder.UnclassifiableException;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;

public class NullCheckClassificationStreamer implements
		IStreamer<NullCheck, NullCheckClassification> {

	protected FeatureStreamer featureStreamer;

	public NullCheckClassificationStreamer(FeatureStreamer featureStreamer) {
		this.featureStreamer = featureStreamer;
	}

	@Override
	public Stream<NullCheckClassification> stream(NullCheck check)
			throws UnclassifiableException {
		try {
			Set<IFeature> features = this.featureStreamer.stream(check)
					.collect(Collectors.toSet());
			NullCheckClassification classification = new NullCheckClassification(
					check, features);
			return Stream.of(classification);
		} catch (UnextractableException exception) {
			throw new UnclassifiableException(check, exception);
		}
	}

}
