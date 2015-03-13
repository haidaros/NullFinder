package ch.unibe.scg.nullfinder;

import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import com.github.javaparser.ast.expr.NullLiteralExpr;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "path",
		"beginLine", "beginColumn" }))
public class NullCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column(name = "path", nullable = false)
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.PathType")
	protected Path path;
	@Columns(columns = { @Column(name = "className", nullable = false),
			@Column(name = "beginLine", nullable = false),
			@Column(name = "beginColumn", nullable = false),
			@Column(name = "endLine", nullable = false),
			@Column(name = "endColumn", nullable = false) })
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.NodeType")
	protected NullLiteralExpr node;

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

	/**
	 * Spring Data needs this.
	 */
	protected NullCheck() {

	}

}
