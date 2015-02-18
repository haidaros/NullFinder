package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Collections;

import ch.unibe.scg.nullfinder.classification.INullCheckClassification;
import ch.unibe.scg.nullfinder.classification.NullCheckClassifier;
import ch.unibe.scg.nullfinder.classification.UnclassifiableNullCheckException;

import com.github.javaparser.ParseException;
import com.github.javaparser.TokenMgrError;

public class NullCheckExtractor {

	public static class NullCheckVisitor extends SimpleFileVisitor<Path> {

		private NullCheckFinder finder;
		private NullCheckClassifier classifier;
		private NullCheckCollector collector;
		private int filesAccessed;
		private int filesProcessed;

		public NullCheckVisitor(NullCheckFinder finder,
				NullCheckClassifier classifier, NullCheckCollector collector) {
			super();
			this.finder = finder;
			this.classifier = classifier;
			this.collector = collector;
			this.filesAccessed = 0;
			this.filesProcessed = 0;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
				throws IOException {
			if (!path.getFileName().toString().endsWith(".java")) {
				return FileVisitResult.CONTINUE;
			}
			this.filesAccessed = this.filesAccessed + 1;
			Collection<NullCheck> checks = Collections.EMPTY_SET;
			try {
				checks = this.finder.find(path);
			} catch (TokenMgrError e) {
				System.err.println(String.format("ERROR Could not parse %s",
						path.toString()));
				e.printStackTrace();
				return FileVisitResult.CONTINUE;
			} catch (ParseException e) {
				System.err.println(String.format("ERROR Could not parse %s",
						path.toString()));
				e.printStackTrace();
				return FileVisitResult.CONTINUE;
			} catch (Exception e) {
				System.err.println(String.format(
						"ERROR Something went terribly wrong while parsing %s",
						path.toString()));
				e.printStackTrace();
				return FileVisitResult.CONTINUE;
			}
			for (NullCheck check : checks) {
				try {
					INullCheckClassification classification = this.classifier
							.classify(check);
					this.collector.collect(path, check, classification);
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnclassifiableNullCheckException e) {
					System.err.println(String.format("ERROR %s %s:%d:%d", e
							.toString(), path.toString(), check.getNode()
							.getBeginLine(), check.getNode().getBeginColumn()));
				}
			}
			this.filesProcessed = this.filesProcessed + 1;
			return FileVisitResult.CONTINUE;
		}

		public int getFilesAccessed() {
			return this.filesAccessed;
		}

		public int getFilesProcessed() {
			return this.filesProcessed;
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
		System.out.println(String.format(
				"FINISHED accessed %d, processed %d files",
				visitor.getFilesAccessed(), visitor.getFilesProcessed()));
	}
}
