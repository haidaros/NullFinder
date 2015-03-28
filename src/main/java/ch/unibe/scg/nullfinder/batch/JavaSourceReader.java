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
import org.springframework.stereotype.Component;

@Component
public class JavaSourceReader implements ItemReader<Path> {

	protected String root;
	protected List<Path> javaSources;

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
		return this.javaSources.remove(0);
	}

	public String getRoot() {
		return this.root;
	}

	public void setRoot(String root) throws IOException {
		this.root = root;
		this.javaSources = this.process(Paths.get(this.root)).collect(
				Collectors.toList());
	}

}
