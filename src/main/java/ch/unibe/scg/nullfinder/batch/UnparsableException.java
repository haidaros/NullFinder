package ch.unibe.scg.nullfinder.batch;

public class UnparsableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnparsableException(Throwable throwable) {
		super(throwable);
	}

}
