package ch.unibe.scg.nullfinder.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import ch.unibe.scg.nullfinder.ast.JavaParserNodeEqualityVisitor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
		"compilationUnitId", "className", "beginLine", "beginColumn",
		"endLine", "endColumn" }))
public class Node {

	/**
	 * Gets a cached version of a node for the specified AST parameters. Creates
	 * a new node if there is no matching node found. A created node is then
	 * registered in the cache and added to the compilation unit's nodes.
	 *
	 * @param javaParserNode
	 * @return
	 */
	static public Node getCachedNode(CompilationUnit compilationUnit,
			com.github.javaparser.ast.Node javaParserNode) {
		if (javaParserNode.getData() == null) {
			Node node = new Node(compilationUnit, javaParserNode);
			compilationUnit.getNodes().add(node);
			javaParserNode.setData(node);
		}
		return (Node) javaParserNode.getData();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@OneToOne(cascade = CascadeType.ALL, optional = true, mappedBy = "node")
	protected NullCheck nullCheck;
	@ManyToOne(optional = true)
	@JoinColumn(name = "compilationUnitId", nullable = true)
	protected CompilationUnit compilationUnit;
	@OneToMany(mappedBy = "node")
	protected List<NodeReason> nodeReasons;
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
		this.nodeReasons = new ArrayList<>();
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

	public void setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public NullCheck getNullCheck() {
		return this.nullCheck;
	}

	public void setNullCheck(NullCheck nullCheck) {
		this.nullCheck = nullCheck;
	}

	public List<NodeReason> getNodeReasons() {
		return this.nodeReasons;
	}

	public void setNodeReasons(List<NodeReason> nodeReasons) {
		this.nodeReasons = nodeReasons;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getBeginLine() {
		return this.beginLine;
	}

	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}

	public int getBeginColumn() {
		return this.beginColumn;
	}

	public void setBeginColumn(int beginColumn) {
		this.beginColumn = beginColumn;
	}

	public int getEndLine() {
		return this.endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public int getEndColumn() {
		return this.endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public void setJavaParserNode(com.github.javaparser.ast.Node javaParserNode) {
		this.javaParserNode = javaParserNode;
	}

}
