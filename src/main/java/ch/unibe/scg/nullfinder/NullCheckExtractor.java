package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class NullCheckExtractor {

	private NullCheckSelector selector;
	private NullCheckClassifier classifier;
	private NullCheckClassificationStringifier stringifier;

	public NullCheckExtractor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.selector = new NullCheckSelector();
		this.classifier = new NullCheckClassifier();
		this.stringifier = new NullCheckClassificationStringifier();
	}

	public Stream<String> extract(Path root) throws IOException,
			NoSuchMethodException, SecurityException {
		return Files.walk(root).filter(this::isJavaSource).parallel()
				.flatMap(this::selectAll).flatMap(this::classifyAll)
				.map(this::stringify);
	}

	private boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

	private Stream<NullCheck> selectAll(Path path) {
		try {
			return this.selector.selectAll(path);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	private Stream<NullCheckClassification> classifyAll(NullCheck check) {
		try {
			return this.classifier.classifyAll(check);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	private String stringify(NullCheckClassification classification) {
		return this.stringifier.stringify(classification);
	}

}
