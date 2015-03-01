package ch.unibe.scg.nullfinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reflections.Reflections;

import ch.unibe.scg.nullfinder.classifier.INullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.UnclassifiableNullCheckException;

public class NullCheckClassifier {

	Set<INullCheckClassifier> classifiers;

	public NullCheckClassifier() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.classifiers = new HashSet<>();
		Reflections reflections = new Reflections();
		Set<Class<? extends INullCheckClassifier>> classifierClasses = reflections
				.getSubTypesOf(INullCheckClassifier.class);
		for (Class<? extends INullCheckClassifier> classificatierClass : classifierClasses) {
			if (!Modifier.isAbstract(classificatierClass.getModifiers())) {
				this.classifiers.add(classificatierClass
						.getDeclaredConstructor().newInstance());
			}
		}
	}

	public Stream<NullCheckClassification> classifyAll(NullCheck check)
			throws UnclassifiableNullCheckException {
		Set<NullCheckClassification> classifications = this.classifiers
				.stream()
				.filter(classifier -> classifier.accepts(check))
				.map(classifier -> new NullCheckClassification(check,
						classifier)).collect(Collectors.toSet());
		if (classifications.isEmpty()) {
			throw new UnclassifiableNullCheckException(check);
		}
		return classifications.stream();
	}

}
