package ch.unibe.scg.nullfinder.jpa.type;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.jpa.type.descriptor.java.ExtractorTypeDescriptor;

public class ExtractorType extends
		AbstractSingleColumnStandardBasicType<IExtractor> implements
		DiscriminatorType<IExtractor> {

	private static final long serialVersionUID = 1L;

	public static final ExtractorType INSTANCE = new ExtractorType();

	public ExtractorType() {
		super(VarcharTypeDescriptor.INSTANCE, ExtractorTypeDescriptor.INSTANCE);
	}

	@Override
	public String toString(IExtractor value) throws HibernateException {
		return value.getClass().getName();
	}

	@Override
	public IExtractor fromStringValue(String string) throws HibernateException {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends IExtractor> cls = (Class<? extends IExtractor>) Class
					.forName(string);
			IExtractor extractor = cls.newInstance();
			return extractor;
		} catch (ClassNotFoundException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException exception) {
			throw new HibernateException(exception);
		}
	}

	@Override
	public String getName() {
		return "extractor";
	}

	@Override
	public IExtractor stringToObject(String xml) throws Exception {
		return this.fromStringValue(xml);
	}

	@Override
	public String objectToSQLString(IExtractor value, Dialect dialect)
			throws Exception {
		return "\'" + this.toString(value) + "\'";
	}

}
