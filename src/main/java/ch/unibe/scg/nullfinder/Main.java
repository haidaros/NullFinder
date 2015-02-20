package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] arguments) throws IOException,
			NoSuchMethodException, SecurityException {
		long before = System.currentTimeMillis();
		Stream.of(arguments).map(Paths::get).flatMap(Main::extractAll)
				.forEach(System.out::println);
		long after = System.currentTimeMillis();
		System.out.println(String.format("DONE in %d seconds",
				(after - before) / 1000));
	}

	private static Stream<String> extractAll(Path path) {
		try {
			NullCheckExtractor extractor = new NullCheckExtractor();
			return extractor.extract(path);
		} catch (NoSuchMethodException | SecurityException | IOException exception) {
			System.err.println(String.format("ERROR %s while extracting %s",
					exception.toString(), path.toString()));
		}
		return Stream.of();
	}

}
