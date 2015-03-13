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

import com.github.javaparser.ast.Node;

public class NodeType implements CompositeUserType {

	protected static final String[] PROPERTY_NAMES = new String[] {
			"className", "beginLine", "beginColumn", "endLine", "endColumn" };
	protected static final Type[] PROPERTY_TYPES = new Type[] {
			StringType.INSTANCE, IntegerType.INSTANCE, IntegerType.INSTANCE,
			IntegerType.INSTANCE, IntegerType.INSTANCE };

	@Override
	public String[] getPropertyNames() {
		return NodeType.PROPERTY_NAMES;
	}

	@Override
	public Type[] getPropertyTypes() {
		return NodeType.PROPERTY_TYPES;
	}

	@Override
	public Object getPropertyValue(Object component, int property)
			throws HibernateException {
		if (property < 0 || property >= this.getPropertyNames().length) {
			throw new HibernateException("'property' must be >= 0 and < "
					+ this.getPropertyNames().length);
		}
		Node node = (Node) component;
		if (property == 0) {
			return node.getClass().getName();
		}
		if (property == 1) {
			return node.getBeginLine();
		}
		if (property == 2) {
			return node.getBeginColumn();
		}
		if (property == 3) {
			return node.getEndLine();
		}
		if (property == 4) {
			return node.getEndColumn();
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
					"Can not set the class of an existing node");
		}
		Node node = (Node) component;
		if (!Integer.class.isInstance(value)) {
			throw new HibernateException("Value for window must be an integer");
		}
		int integer = (int) value;
		if (property == 1) {
			node.setBeginLine(integer);
		}
		if (property == 2) {
			node.setBeginColumn(integer);
		}
		if (property == 3) {
			node.setEndLine(integer);
		}
		if (property == 4) {
			node.setEndColumn(integer);
		}
	}

	@Override
	public Class<?> returnedClass() {
		return Node.class;
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
		Integer beginLine = (Integer) rs.getObject(names[1]);
		Integer beginColumn = (Integer) rs.getObject(names[2]);
		Integer endLine = (Integer) rs.getObject(names[3]);
		Integer endColumn = (Integer) rs.getObject(names[4]);
		return this.newNode(this.getNodeClass(className), beginLine,
				beginColumn, endLine, endColumn);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		Node node = (Node) value;
		st.setString(index, node.getClass().getName());
		st.setInt(index + 1, node.getBeginLine());
		st.setInt(index + 2, node.getBeginColumn());
		st.setInt(index + 3, node.getEndLine());
		st.setInt(index + 4, node.getEndColumn());
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		Node node = (Node) value;
		return this.newNode(node.getClass(), node.getBeginLine(),
				node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
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
		Node originalNode = (Node) original;
		Node targetNode = (Node) target;
		targetNode.setBeginLine(originalNode.getBeginLine());
		targetNode.setBeginColumn(originalNode.getBeginColumn());
		targetNode.setEndLine(originalNode.getEndLine());
		targetNode.setEndColumn(originalNode.getEndColumn());
		return targetNode;
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends Node> getNodeClass(String className)
			throws HibernateException {
		try {
			return (Class<? extends Node>) Class.forName(className);
		} catch (ClassNotFoundException exception) {
			throw new HibernateException(exception);
		}
	}

	protected Node newNode(Class<? extends Node> cls, int beginLine,
			int beginColumn, int endLine, int endColumn)
			throws HibernateException {
		try {
			Node node = cls.newInstance();
			node.setBeginLine(beginLine);
			node.setBeginColumn(beginColumn);
			node.setEndLine(endLine);
			node.setEndColumn(endColumn);
			return node;
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException exception) {
			throw new HibernateException(exception);
		}
	}

}
