package ch.unibe.scg.nullfinder.streamer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.reflections.Reflections;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;

public class FeatureStreamer implements IStreamer<NullCheck, IFeature> {

	protected SortedMap<Integer, Set<IExtractor>> extractors;

	public FeatureStreamer() throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.extractors = new TreeMap<>();
		Reflections reflections = new Reflections();
		Set<Class<? extends IExtractor>> extractorClasses = reflections
				.getSubTypesOf(IExtractor.class);
		for (Class<? extends IExtractor> extractorClass : extractorClasses) {
			if (Modifier.isAbstract(extractorClass.getModifiers())) {
				continue;
			}
			IExtractor extractor = extractorClass.getDeclaredConstructor()
					.newInstance();
			int level = extractor.getLevel();
			if (!this.extractors.containsKey(level)) {
				this.extractors.put(level, new HashSet<>());
			}
			this.extractors.get(level).add(extractor);
		}
	}

	@Override
	public Stream<IFeature> stream(NullCheck check)
			throws UnextractableException {
		Set<IFeature> features = new HashSet<>();
		for (Set<IExtractor> levelExtractors : this.extractors.values()) {
			for (IExtractor extractor : levelExtractors) {
				try {
					features.add(extractor.extract(check, features));
				} catch (UnextractableException e) {
					// noop
				}
			}
		}
		if (features.isEmpty()) {
			throw new UnextractableException(check);
		}
		return features.stream();
	}

}
