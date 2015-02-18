package ch.unibe.scg.nullfinder;

public class TestNullClass {

	public static Object classVariable;
	public Object instanceVariable;

	public void checkClassVariable() {
		if (TestNullClass.classVariable == null) {
			// noop
		}
	}

	public void checkInstanceVariable() {
		if (this.instanceVariable == null) {
			// noop
		}
	}

	public void checkParameter(Object parameter) {
		if (parameter == null) {
			// noop
		}
	}

	public void checkReturnedValue() {
		Object returnedValue = this.getNull();
		if (returnedValue == null) {
			// noop
		}
	}

	public Object getNull() {
		return null;
	}

}
