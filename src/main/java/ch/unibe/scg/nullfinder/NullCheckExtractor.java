package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;
import ch.unibe.scg.nullfinder.classification.NullCheckClassifier;
import ch.unibe.scg.nullfinder.classification.UnclassifiableNullCheckException;

import com.github.javaparser.ParseException;

public class NullCheckExtractor {

	public class NullCheckVisitor extends SimpleFileVisitor<Path> {

		private NullCheckFinder finder;
		private NullCheckClassifier classifier;
		private NullCheckCollector collector;

		public NullCheckVisitor(NullCheckFinder finder,
				NullCheckClassifier classifier, NullCheckCollector collector) {
			super();
			this.finder = finder;
			this.classifier = classifier;
			this.collector = collector;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
				throws IOException {
			if (!path.getFileName().toString().endsWith(".java")) {
				return FileVisitResult.CONTINUE;
			}
			try {
				Collection<NullCheck> checks = this.finder.find(path);
				for (NullCheck check : checks) {
					try {
						INullCheckClassification classification = this.classifier
								.classify(check);
						this.collector.collect(path, check, classification);
					} catch (InstantiationException | IllegalAccessException
							| IllegalArgumentException
							| InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnclassifiableNullCheckException e) {
						System.err.println(String.format(
								"UnclassifiableNullCheckException %s:%d:%d %s",
								path.toString(),
								check.getNode().getBeginLine(), check.getNode()
										.getBeginLine(), check.getNode()
										.getParentNode().toString()));
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return FileVisitResult.CONTINUE;
		}

	}

	public void extract(Path file) throws IOException, NoSuchMethodException,
			SecurityException {
		NullCheckFinder finder = new NullCheckFinder();
		NullCheckClassifier classifier = new NullCheckClassifier();
		NullCheckCollector collector = new NullCheckCollector(System.out);
		NullCheckVisitor visitor = new NullCheckVisitor(finder, classifier,
				collector);
		Files.walkFileTree(file, visitor);
	}
}
