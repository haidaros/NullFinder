package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import ch.unibe.scg.nullfinder.collector.FeatureCollector;
import ch.unibe.scg.nullfinder.collector.NullCheckClassificationCollector;
import ch.unibe.scg.nullfinder.collector.NullCheckCollector;
import ch.unibe.scg.nullfinder.collector.StringCollector;

public class NullCheckExtractor {

	protected NullCheckCollector checkCollector;
	protected NullCheckClassificationCollector classificationCollector;
	protected StringCollector stringCollector;

	public NullCheckExtractor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.checkCollector = new NullCheckCollector();
		this.classificationCollector = new NullCheckClassificationCollector(
				new FeatureCollector());
		this.stringCollector = new StringCollector();
	}

	public Stream<String> extract(Path root) throws IOException,
			NoSuchMethodException, SecurityException {
		return Files.walk(root).filter(this::isJavaSource)
				.flatMap(this::collectNullChecks)
				.flatMap(this::collectNullCheckClassifications)
				.map(this::collectString);
	}

	protected boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

	protected Stream<NullCheck> collectNullChecks(Path path) {
		try {
			return this.checkCollector.collect(path).stream();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected Stream<NullCheckClassification> collectNullCheckClassifications(
			NullCheck check) {
		try {
			return Stream.of(this.classificationCollector.collect(check));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected String collectString(NullCheckClassification classification) {
		return this.stringCollector.collect(classification);
	}

}
