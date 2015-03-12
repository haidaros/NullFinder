package ch.unibe.scg.nullfinder.jpa.type.descriptor.java;

import java.nio.file.Path;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import ch.unibe.scg.nullfinder.jpa.type.PathType;

public class PathTypeDescriptor extends AbstractTypeDescriptor<Path> {

	private static final long serialVersionUID = 1L;

	public static final PathTypeDescriptor INSTANCE = new PathTypeDescriptor();

	public PathTypeDescriptor() {
		super(Path.class);
	}

	@Override
	public String toString(Path value) {
		return PathType.INSTANCE.toString(value);
	}

	@Override
	public Path fromString(String string) {
		return PathType.INSTANCE.fromString(string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X unwrap(Path value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (Path.class.isAssignableFrom(type)) {
			return (X) value;
		}
		if (String.class.isAssignableFrom(type)) {
			return (X) this.toString(value);
		}
		throw unknownUnwrap(type);
	}

	@Override
	public <X> Path wrap(X value, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (Path.class.isInstance(value)) {
			return (Path) value;
		}
		if (String.class.isInstance(value)) {
			return this.fromString((String) value);
		}
		throw unknownWrap(value.getClass());
	}

}
