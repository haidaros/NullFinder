package ch.unibe.scg.nullfinder.jpa.entity;

import java.nio.file.Path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

@Entity
public class BadCompilationUnit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column(name = "path", unique = true, nullable = false)
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.PathType")
	protected Path path;
	@Column(name = "source")
	@Lob
	protected String source;
	@Column(name = "exceptionString")
	@Lob
	protected String exceptionString;

	public BadCompilationUnit(Path path, String source, String exceptionString) {
		this.path = path;
		this.source = source;
		this.exceptionString = exceptionString;
	}

	/**
	 * Required by Spring Data.
	 */
	protected BadCompilationUnit() {
		super();
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

	public String getExceptionString() {
		return this.exceptionString;
	}

	public void setExceptionString(String exceptionString) {
		this.exceptionString = exceptionString;
	}

}
