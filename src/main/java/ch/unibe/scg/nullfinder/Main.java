package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] arguments) throws IOException,
			NoSuchMethodException, SecurityException {
		String root = arguments[0];
		NullCheckExtractor extractor = new NullCheckExtractor();
		long before = System.currentTimeMillis();
		extractor.extract(Paths.get(root)).forEach(System.out::println);
		long after = System.currentTimeMillis();
		System.out.println(String.format("DONE in %d seconds",
				(after - before) / 1000));
	}

}
