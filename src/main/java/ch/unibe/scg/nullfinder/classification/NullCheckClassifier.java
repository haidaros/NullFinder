package ch.unibe.scg.nullfinder.classification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import ch.unibe.scg.nullfinder.NullCheck;

public class NullCheckClassifier {

	Set<Constructor<? extends INullCheckClassification>> constructors;

	public NullCheckClassifier() throws NoSuchMethodException,
			SecurityException {
		this.constructors = new HashSet<>();
		Reflections reflections = new Reflections();
		Set<Class<? extends INullCheckClassification>> classificationClasses = reflections
				.getSubTypesOf(INullCheckClassification.class);
		for (Class<? extends INullCheckClassification> classificationClass : classificationClasses) {
			this.constructors.add(classificationClass.getDeclaredConstructor());
		}
	}

	public INullCheckClassification classify(NullCheck nullCheck)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			UnclassifiableNullCheckException {
		for (Constructor<? extends INullCheckClassification> constructor : this.constructors) {
			INullCheckClassification classification = constructor.newInstance();
			if (classification.accepts(nullCheck)) {
				return classification;
			}
		}
		throw new UnclassifiableNullCheckException();
	}

}
