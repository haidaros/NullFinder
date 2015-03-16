package ch.unibe.scg.nullfinder.ast;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

@Entity
public class CompilationUnit extends Node {

	@Column(name = "path", nullable = false, unique = true)
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.PathType")
	protected Path path;
	@Column(name = "source", nullable = false)
	@Lob
	protected String source;

	public CompilationUnit(
			com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit,
			Path path, String source) {
		super(null, javaParserCompilationUnit);
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

}
