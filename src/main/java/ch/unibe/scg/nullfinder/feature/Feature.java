package ch.unibe.scg.nullfinder.feature;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;
import ch.unibe.scg.nullfinder.feature.reason.Reason;

@Entity
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = { "check",
// "extractor" }))
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "checkId", nullable = false)
	protected NullCheck check;
	@Transient
	// @Column(name = "extractor", nullable = false)
	// @Type(type = "ch.unibe.scg.nullfinder.jpa.type.ExtractorType")
	protected IExtractor extractor;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "feature")
	protected Set<Reason> reasons;

	public Feature(NullCheck check, IExtractor extractor) {
		this.check = check;
		this.extractor = extractor;
		this.reasons = new HashSet<>();
	}

	public NullCheck getNullCheck() {
		return this.check;
	}

	public IExtractor getExtractor() {
		return this.extractor;
	}

	public Set<Reason> getReasons() {
		return this.reasons;
	}

	/**
	 * Spring Data needs this.
	 */
	protected Feature() {

	}

}
