package ch.unibe.scg.nullfinder.feature;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.feature.reason.Reason;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nullCheckId",
		"className", "level" }))
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "nullCheckId", nullable = false)
	protected NullCheck nullCheck;
	@Columns(columns = { @Column(name = "className", nullable = false),
			@Column(name = "level", nullable = false) })
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.ExtractorType")
	protected IExtractor extractor;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "feature")
	protected List<Reason> reasons;

	public Feature(NullCheck nullCheck, IExtractor extractor) {
		this.nullCheck = nullCheck;
		this.extractor = extractor;
		this.reasons = new LinkedList<>();
	}

	/**
	 * Required by Spring Data.
	 */
	protected Feature() {

	}

	public NullCheck getNullCheck() {
		return this.nullCheck;
	}

	public IExtractor getExtractor() {
		return this.extractor;
	}

	public List<Reason> getReasons() {
		return this.reasons;
	}

}
