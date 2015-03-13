package ch.unibe.scg.nullfinder.jpa.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;

public class ExtractorType implements CompositeUserType {

	protected static final String[] PROPERTY_NAMES = new String[] {
			"className", "level" };
	protected static final Type[] PROPERTY_TYPES = new Type[] {
			StringType.INSTANCE, IntegerType.INSTANCE };

	@Override
	public String[] getPropertyNames() {
		return ExtractorType.PROPERTY_NAMES;
	}

	@Override
	public Type[] getPropertyTypes() {
		return ExtractorType.PROPERTY_TYPES;
	}

	@Override
	public Object getPropertyValue(Object component, int property)
			throws HibernateException {
		if (property < 0 || property >= this.getPropertyNames().length) {
			throw new HibernateException("'property' must be >= 0 and < "
					+ this.getPropertyNames().length);
		}
		IExtractor extractor = (IExtractor) component;
		if (property == 0) {
			return extractor.getClass().getName();
		}
		if (property == 1) {
			return extractor.getLevel();
		}
		throw new HibernateException("How the hell did you get here?!");
	}

	@Override
	public void setPropertyValue(Object component, int property, Object value)
			throws HibernateException {
		if (property < 0 || property >= this.getPropertyNames().length) {
			throw new HibernateException("'property' must be >= 0 and < "
					+ this.getPropertyNames().length);
		}
		if (property == 0) {
			throw new HibernateException(
					"Can not set the class of an existing extractor");
		}
		IExtractor extractor = (IExtractor) component;
		if (!Integer.class.isInstance(value)) {
			throw new HibernateException("Value for 'level' must be an integer");
		}
		int integer = (int) value;
		if (property == 1) {
			extractor.setLevel(integer);
		}
	}

	@Override
	public Class<?> returnedClass() {
		return IExtractor.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == null || y == null) {
			return false;
		}
		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String className = rs.getString(names[0]);
		Integer level = (Integer) rs.getObject(names[1]);
		return this.newExtractor(this.getExtractorClass(className), level);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		IExtractor extractor = (IExtractor) value;
		st.setString(index, extractor.getClass().getName());
		st.setInt(index + 1, extractor.getLevel());
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		IExtractor extractor = (IExtractor) value;
		return this.newExtractor(extractor.getClass(), extractor.getLevel());
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value, SessionImplementor session)
			throws HibernateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object assemble(Serializable cached, SessionImplementor session,
			Object owner) throws HibernateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object replace(Object original, Object target,
			SessionImplementor session, Object owner) throws HibernateException {
		IExtractor originalExtractor = (IExtractor) original;
		IExtractor targetExtractor = (IExtractor) target;
		targetExtractor.setLevel(originalExtractor.getLevel());
		return targetExtractor;
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends IExtractor> getExtractorClass(String className)
			throws HibernateException {
		try {
			return (Class<? extends IExtractor>) Class.forName(className);
		} catch (ClassNotFoundException exception) {
			throw new HibernateException(exception);
		}
	}

	protected IExtractor newExtractor(Class<? extends IExtractor> cls, int level)
			throws HibernateException {
		try {
			IExtractor extractor = cls.newInstance();
			extractor.setLevel(level);
			return extractor;
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException exception) {
			throw new HibernateException(exception);
		}
	}

}
