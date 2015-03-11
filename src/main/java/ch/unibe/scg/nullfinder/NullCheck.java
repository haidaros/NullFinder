package ch.unibe.scg.nullfinder;

import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.github.javaparser.ast.expr.NullLiteralExpr;

@Entity
public class NullCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.PathType")
	@Column
	protected Path path;
	@Transient
	protected NullLiteralExpr node;

	/**
	 * Used by Spring Data.
	 */
	@SuppressWarnings("unused")
	private NullCheck() {
	}

	public NullCheck(Path path, NullLiteralExpr node) {
		this.path = path;
		this.node = node;
	}

	public Path getPath() {
		return this.path;
	}

	public NullLiteralExpr getNode() {
		return node;
	}

	@Override
	public String toString() {
		return String.format("%s line %d column %d %s", this.path.toString(),
				this.node.getBeginLine(), this.node.getBeginColumn(), this.node
						.getParentNode().toString());
	}

}
