package ch.unibe.scg.nullfinder;


public class UnclassifiableException extends Exception {

	private static final long serialVersionUID = 1L;

	protected NullCheck nullCheck;

	public UnclassifiableException(NullCheck nullCheck, Exception cause) {
		super(cause);
		this.nullCheck = nullCheck;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(), this.nullCheck.toString());
	}

}
