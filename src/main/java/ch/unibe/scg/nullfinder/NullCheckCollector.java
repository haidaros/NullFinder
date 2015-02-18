package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;

public class NullCheckCollector {

	private OutputStream stream;

	public NullCheckCollector(OutputStream stream) {
		this.stream = stream;
	}

	public void collect(Path path, NullCheck check,
			INullCheckClassification classification) throws IOException {
		String line = String.format("%s\t%d\t%d\t%s\t%s\n", path.toString(),
				check.getNode().getBeginLine(), check.getNode()
						.getBeginColumn(), check.getNode().getParentNode()
						.toString(), classification.getClass().getName());
		this.stream.write(line.getBytes());
	}
}
