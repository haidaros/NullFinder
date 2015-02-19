package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;

import com.github.javaparser.TokenMgrError;

public class NullCheckExtractor {

	private NullCheckSelector selector;
	private NullCheckClassifier classifier;
	private NullCheckClassificationStringifier stringifier;

	public NullCheckExtractor() throws NoSuchMethodException, SecurityException {
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
		} catch (Exception exception) {
			System.err.println(String.format("ERROR %s while selecting %s",
					exception.toString(), path.toString()));
		} catch (TokenMgrError error) {
			System.err.println(String.format("ERROR %s while selecting %s",
					error.toString(), path.toString()));
		}
		return Stream.of();
	}

	private Stream<INullCheckClassification> classifyAll(NullCheck check) {
		try {
			return this.classifier.classifyAll(check);
		} catch (Exception exception) {
			System.err.println(String.format("ERROR %s while classifying %s",
					exception.toString(), check.toString()));
		}
		return Stream.of();
	}

	private String stringify(INullCheckClassification classification) {
		return this.stringifier.stringify(classification);
	}

}
