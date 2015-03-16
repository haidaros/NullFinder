package ch.unibe.scg.nullfinder.collector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.reflections.Reflections;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;

public class FeatureCollector implements ICollector<NullCheck, List<Feature>> {

	protected SortedMap<Integer, Set<IExtractor>> extractors;

	public FeatureCollector() throws NoSuchMethodException, SecurityException,
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
	public List<Feature> collect(NullCheck nullCheck) throws UnextractableException {
		List<Feature> features = new LinkedList<>();
		for (Set<IExtractor> levelExtractors : this.extractors.values()) {
			for (IExtractor extractor : levelExtractors) {
				try {
					features.add(extractor.extract(nullCheck, features));
				} catch (UnextractableException e) {
					// noop
				}
			}
		}
		if (features.isEmpty()) {
			throw new UnextractableException(nullCheck);
		}
		return features;
	}

}
