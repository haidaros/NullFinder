package ch.unibe.scg.nullfinder.jpa.entity;

import java.util.ArrayList;
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

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;

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
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "feature")
	protected List<Reason> reasons;
	@OneToMany(mappedBy = "reasonFeature")
	protected List<FeatureReason> featureReasons;
	@Columns(columns = { @Column(name = "className", nullable = false),
			@Column(name = "level", nullable = false) })
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.ExtractorType")
	protected IExtractor extractor;

	public Feature(NullCheck nullCheck, IExtractor extractor) {
		this.nullCheck = nullCheck;
		this.reasons = new ArrayList<>();
		this.featureReasons = new ArrayList<>();
		this.extractor = extractor;
	}

	/**
	 * Required by Spring Data.
	 */
	protected Feature() {

	}

	public NullCheck getNullCheck() {
		return this.nullCheck;
	}

	public void setNullCheck(NullCheck nullCheck) {
		this.nullCheck = nullCheck;
	}

	public List<Reason> getReasons() {
		return this.reasons;
	}

	public void setReasons(List<Reason> reasons) {
		this.reasons = reasons;
	}

	public IExtractor getExtractor() {
		return this.extractor;
	}

	public void setExtractor(IExtractor extractor) {
		this.extractor = extractor;
	}

	public List<FeatureReason> getFeatureReasons() {
		return this.featureReasons;
	}

	public void setFeatureReasons(List<FeatureReason> featureReasons) {
		this.featureReasons = featureReasons;
	}

}
