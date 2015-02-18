package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] arguments) throws IOException,
			NoSuchMethodException, SecurityException {
		String root = arguments[0];
		NullCheckExtractor extractor = new NullCheckExtractor();
		extractor.extract(Paths.get(root));
	}

}
