package ch.unibe.scg.nullfinder.batch;

import java.nio.file.Path;

public class UnparsableException extends Exception {

	private static final long serialVersionUID = 1L;

	protected Path javaSourcePath;

	public UnparsableException(Path javaSourcePath, Throwable throwable) {
		super(throwable);
		this.javaSourcePath = javaSourcePath;
	}

	public Path getJavaSourcePath() {
		return this.javaSourcePath;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(),
				this.javaSourcePath.toString());
	}

}
