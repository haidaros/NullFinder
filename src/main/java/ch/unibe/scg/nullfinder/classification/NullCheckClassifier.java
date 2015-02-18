package ch.unibe.scg.nullfinder.classification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.reflections.Reflections;

import ch.unibe.scg.nullfinder.NullCheck;

public class NullCheckClassifier {

	public INullCheckClassification classify(NullCheck nullCheck)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			UnclassifiableNullCheckException {
		Reflections reflections = new Reflections();
		Set<Class<? extends INullCheckClassification>> classificationClasses = reflections
				.getSubTypesOf(INullCheckClassification.class);
		for (Class<? extends INullCheckClassification> classificationClass : classificationClasses) {
			Constructor<? extends INullCheckClassification> constructor = classificationClass
					.getDeclaredConstructor();
			INullCheckClassification classification = constructor.newInstance();
			if (classification.accepts(nullCheck)) {
				return classification;
			}
		}
		throw new UnclassifiableNullCheckException();
	}

}
