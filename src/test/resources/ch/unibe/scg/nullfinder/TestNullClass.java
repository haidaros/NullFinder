package ch.unibe.scg.nullfinder;

public class TestNullClass {

	public Object field;

	public void checkFieldAccess() {
		if (this.field == null) {
			// noop
		}
	}

	public void checkFieldAccess() {
		String[] array = new String[0]; 
		if (array[0] == null) {
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
