package ch.unibe.scg.nullfinder.jpa.type.descriptor.java;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.jpa.type.ExtractorType;

public class ExtractorTypeDescriptor extends AbstractTypeDescriptor<IExtractor> {

	private static final long serialVersionUID = 1L;

	public static final ExtractorTypeDescriptor INSTANCE = new ExtractorTypeDescriptor();

	public ExtractorTypeDescriptor() {
		super(IExtractor.class);
	}

	@Override
	public String toString(IExtractor value) {
		return ExtractorType.INSTANCE.toString(value);
	}

	@Override
	public IExtractor fromString(String string) {
		return ExtractorType.INSTANCE.fromStringValue(string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X unwrap(IExtractor value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (IExtractor.class.isAssignableFrom(type)) {
			return (X) value;
		}
		if (String.class.isAssignableFrom(type)) {
			return (X) this.toString(value);
		}
		throw this.unknownUnwrap(type);
	}

	@Override
	public <X> IExtractor wrap(X value, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (IExtractor.class.isInstance(value)) {
			return (IExtractor) value;
		}
		if (String.class.isInstance(value)) {
			return this.fromString((String) value);
		}
		throw this.unknownWrap(value.getClass());
	}

}
