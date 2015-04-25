package ch.unibe.scg.nullfinder.jpa.type;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import ch.unibe.scg.nullfinder.jpa.type.descriptor.java.PathTypeDescriptor;

public class PathType extends AbstractSingleColumnStandardBasicType<Path>
		implements DiscriminatorType<Path> {

	private static final long serialVersionUID = 1L;

	public static final PathType INSTANCE = new PathType();

	public PathType() {
		super(VarcharTypeDescriptor.INSTANCE, PathTypeDescriptor.INSTANCE);
	}

	@Override
	public String toString(Path value) throws HibernateException {
		return value.toString();
	}

	@Override
	public Path fromStringValue(String string) throws HibernateException {
		return Paths.get(string);
	}

	@Override
	public String getName() {
		return "path";
	}

	@Override
	public Path stringToObject(String xml) throws Exception {
		return this.fromStringValue(xml);
	}

	@Override
	public String objectToSQLString(Path value, Dialect dialect)
			throws Exception {
		return "\'" + this.toString(value) + "\'";
	}

}
