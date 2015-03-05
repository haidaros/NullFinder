package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import ch.unibe.scg.nullfinder.streamer.FeatureStreamer;
import ch.unibe.scg.nullfinder.streamer.NullCheckClassificationStreamer;
import ch.unibe.scg.nullfinder.streamer.NullCheckStreamer;
import ch.unibe.scg.nullfinder.streamer.StringStreamer;

public class NullCheckExtractor {

	protected NullCheckStreamer checkStreamer;
	protected NullCheckClassificationStreamer classificationStreamer;
	protected StringStreamer stringStreamer;

	public NullCheckExtractor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.checkStreamer = new NullCheckStreamer();
		this.classificationStreamer = new NullCheckClassificationStreamer(
				new FeatureStreamer());
		this.stringStreamer = new StringStreamer();
	}

	public Stream<String> extract(Path root) throws IOException,
			NoSuchMethodException, SecurityException {
		return Files.walk(root).filter(this::isJavaSource)
				.flatMap(this::collectNullChecks).flatMap(this::collectNullCheckClassifications)
				.flatMap(this::collectStrings);
	}

	protected boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

	protected Stream<NullCheck> collectNullChecks(Path path) {
		try {
			return this.checkStreamer.stream(path);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected Stream<NullCheckClassification> collectNullCheckClassifications(NullCheck check) {
		try {
			return this.classificationStreamer.stream(check);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected Stream<String> collectStrings(NullCheckClassification classification) {
		return this.stringStreamer.stream(classification);
	}

}
