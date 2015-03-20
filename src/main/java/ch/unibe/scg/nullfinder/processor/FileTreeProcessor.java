package ch.unibe.scg.nullfinder.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class FileTreeProcessor {

	public Stream<Path> process(Path root) throws IOException {
		return Files.walk(root).filter(this::isJavaSource);
	}

	protected boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

}
