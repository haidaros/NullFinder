package ch.unibe.scg.nullfinder.batch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.reflections.Reflections;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

@Component
public class NullCheckProcessor implements
		ItemProcessor<NullCheck, List<Feature>> {

	protected SortedMap<Integer, Set<IExtractor>> extractors;

	public NullCheckProcessor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
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
	public List<Feature> process(NullCheck nullCheck) {
		List<Feature> features = new ArrayList<>();
		for (Set<IExtractor> levelExtractors : this.extractors.values()) {
			for (IExtractor extractor : levelExtractors) {
				features.addAll(extractor.extract(nullCheck, features));
			}
		}
		return features;
	}

}
