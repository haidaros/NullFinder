package ch.unibe.scg.nullfinder.batch;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;

public class UnvisitableException extends Exception {

	private static final long serialVersionUID = 1L;

	protected CompilationUnit compilationUnit;

	public UnvisitableException(CompilationUnit compilationUnit,
			Throwable throwable) {
		super(throwable);
		this.compilationUnit = compilationUnit;
	}

	public CompilationUnit getCompilationUnit() {
		return this.compilationUnit;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(), this.compilationUnit
				.getPath().toString());
	}

}
