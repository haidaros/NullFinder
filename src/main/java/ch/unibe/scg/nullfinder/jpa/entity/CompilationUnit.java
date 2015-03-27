package ch.unibe.scg.nullfinder.jpa.entity;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

@Entity
public class CompilationUnit extends Node {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "compilationUnit")
	protected List<Node> nodes;
	@Column(name = "path", unique = true, nullable = false)
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.PathType")
	protected Path path;
	@Column(name = "source", nullable = false)
	@Lob
	protected String source;

	public CompilationUnit(
			com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit,
			Path path, String source) {
		super(null, javaParserCompilationUnit);
		this.nodes = new ArrayList<>();
		this.path = path;
		this.source = source;
	}

	public CompilationUnit(
			com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit,
			Path path) {
		this(javaParserCompilationUnit, path, javaParserCompilationUnit
				.toString());
	}

	/**
	 * Required by Spring Data.
	 */
	protected CompilationUnit() {
		super();
	}

	@Override
	public com.github.javaparser.ast.Node getJavaParserNode() {
		if (this.javaParserNode == null) {
			try {
				this.javaParserNode = JavaParser
						.parse(new ByteArrayInputStream(this.source.getBytes()));
			} catch (ParseException exception) {
				throw new RuntimeException(exception);
			}
		}
		return this.javaParserNode;
	}

	@Override
	public com.github.javaparser.ast.CompilationUnit getJavaParserCompilationUnit() {
		return (com.github.javaparser.ast.CompilationUnit) this
				.getJavaParserNode();
	}

	@Override
	public CompilationUnit getCompilationUnit() {
		return this;
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Path getPath() {
		return this.path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
