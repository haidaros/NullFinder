package ch.unibe.scg.nullfinder.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class JavaSourceReader implements ItemReader<Path> {

	protected List<Path> javaSources;

	public JavaSourceReader() throws IOException {
		// TODO make root configurable
		Path root = Paths
				.get("../intellij-community/java/debugger/impl/src/com/intellij/debugger");
		this.javaSources = this.process(root).collect(Collectors.toList());
	}

	public Stream<Path> process(Path root) throws IOException {
		return Files.walk(root).filter(this::isJavaSource);
	}

	protected boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

	@Override
	public Path read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		if (this.javaSources.isEmpty()) {
			return null;
		}
		return this.javaSources.get(0);
	}

}
