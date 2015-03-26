package ch.unibe.scg.nullfinder.ast;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
		"compilationUnitId", "className", "beginLine", "beginColumn",
		"endLine", "endColumn" }))
public class Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(optional = true)
	@JoinColumn(name = "compilationUnitId", nullable = true)
	protected CompilationUnit compilationUnit;
	@Column(name = "className", nullable = false)
	protected String className;
	@Column(name = "beginLine", nullable = false)
	protected int beginLine;
	@Column(name = "beginColumn", nullable = false)
	protected int beginColumn;
	@Column(name = "endLine", nullable = false)
	protected int endLine;
	@Column(name = "endColumn", nullable = false)
	protected int endColumn;
	@Transient
	protected com.github.javaparser.ast.Node javaParserNode;

	public Node(CompilationUnit compilationUnit, String className,
			int beginLine, int beginColumn, int endLine, int endColumn) {
		this.compilationUnit = compilationUnit;
		this.className = className;
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}

	public Node(CompilationUnit compilationUnit,
			com.github.javaparser.ast.Node javaParserNode) {
		this(compilationUnit, javaParserNode.getClass().getName(),
				javaParserNode.getBeginLine(), javaParserNode.getBeginColumn(),
				javaParserNode.getEndLine(), javaParserNode.getEndColumn());
		this.javaParserNode = javaParserNode;
	}

	/**
	 * Required by Spring Data.
	 */
	protected Node() {

	}

	/**
	 * Checks if the specified JavaParser node matches this by comparing the
	 * compilation unit, the class and window.
	 * 
	 * @param javaParserNode
	 * @return true if it matches, false otherwise
	 */
	public boolean equalsJavaParserNode(
			com.github.javaparser.ast.Node javaParserNode) {
		com.github.javaparser.ast.Node javaParserCompilationUnit = javaParserNode;
		while (javaParserCompilationUnit.getParentNode() != null) {
			javaParserCompilationUnit = javaParserCompilationUnit
					.getParentNode();
		}
		return this.getJavaParserCompilationUnit().equals(
				javaParserCompilationUnit)
				&& this.getClassName().equals(
						javaParserNode.getClass().getName())
				&& this.getBeginLine() == javaParserNode.getBeginLine()
				&& this.getBeginColumn() == javaParserNode.getBeginColumn()
				&& this.getEndLine() == javaParserNode.getEndLine()
				&& this.getEndColumn() == javaParserNode.getEndColumn();
	}

	public com.github.javaparser.ast.Node getJavaParserNode() {
		if (this.javaParserNode == null) {
			com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit = this
					.getJavaParserCompilationUnit();
			JavaParserNodeEqualityVisitor visitor = new JavaParserNodeEqualityVisitor();
			javaParserCompilationUnit.accept(visitor, this);
			this.javaParserNode = visitor.getJavaParserNode();
		}
		return this.javaParserNode;
	}

	public com.github.javaparser.ast.CompilationUnit getJavaParserCompilationUnit() {
		return this.compilationUnit.getJavaParserCompilationUnit();
	}

	public CompilationUnit getCompilationUnit() {
		return this.compilationUnit;
	}

	public String getClassName() {
		return this.className;
	}

	public int getBeginLine() {
		return this.beginLine;
	}

	public int getBeginColumn() {
		return this.beginColumn;
	}

	public int getEndLine() {
		return this.endLine;
	}

	public int getEndColumn() {
		return this.endColumn;
	}

}
