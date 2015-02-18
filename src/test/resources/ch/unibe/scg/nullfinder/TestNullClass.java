package ch.unibe.scg.nullfinder;

public class TestNullClass {

	public Object field;

	public void checkFieldAccess() {
		if (this.field == null) {
			// noop
		}
	}

	public void checkArrayAccess() {
		String[] array = new String[0];
		if (array[0] == null) {
			// noop
		}
	}

	public void checkEnclosed() {
		Object value;
		if ((value = this) != null) {
			// noop
		}
	}

	public void checkCast() {
		Object value = this;
		if ((String) value != null) {
			// noop
		}
	}

	public void checkName() {
		Object name = this.getNull();
		if (name == null) {
			// noop
		}
	}

	public void checkMethodCallValue() {
		if (this.getNull() == null) {
			// noop
		}
	}

	public Object getNull() {
		return null;
	}

}
