package ch.unibe.scg.nullfinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reflections.Reflections;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;
import ch.unibe.scg.nullfinder.classification.NullNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.UnclassifiableNullCheckException;

public class NullCheckClassifier {

	Set<Constructor<? extends INullCheckClassification>> constructors;

	public NullCheckClassifier() throws NoSuchMethodException,
			SecurityException {
		this.constructors = new HashSet<>();
		Reflections reflections = new Reflections();
		Set<Class<? extends INullCheckClassification>> classificationClasses = reflections
				.getSubTypesOf(INullCheckClassification.class);
		for (Class<? extends INullCheckClassification> classificationClass : classificationClasses) {
			if (!Modifier.isAbstract(classificationClass.getModifiers())) {
				this.constructors.add(classificationClass
						.getDeclaredConstructor(NullCheck.class));
			}
		}
	}

	public Stream<INullCheckClassification> classifyAll(NullCheck check)
			throws UnclassifiableNullCheckException {
		Set<INullCheckClassification> classifications = this.constructors
				.stream()
				.map(constructor -> this.instantiate(constructor, check))
				.filter(classification -> classification.accepts(check))
				.collect(Collectors.toSet());
		if (classifications.isEmpty()) {
			throw new UnclassifiableNullCheckException(check);
		}
		return classifications.stream();
	}

	private INullCheckClassification instantiate(
			Constructor<? extends INullCheckClassification> constructor,
			NullCheck check) {
		try {
			return constructor.newInstance(check);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException exception) {
			System.err.println(String.format("ERROR %s while instantiating %s",
					exception.toString(), constructor.toString()));
		}
		return new NullNullCheckClassification(check);
	}

}
