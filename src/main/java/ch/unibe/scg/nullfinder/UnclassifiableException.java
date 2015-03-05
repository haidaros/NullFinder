package ch.unibe.scg.nullfinder;


public class UnclassifiableException extends Exception {

	private static final long serialVersionUID = 1L;

	protected NullCheck check;

	public UnclassifiableException(NullCheck check, Exception cause) {
		super(cause);
		this.check = check;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", super.toString(), this.check.toString());
	}

}
