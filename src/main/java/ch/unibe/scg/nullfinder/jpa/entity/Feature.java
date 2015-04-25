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

import org.hibernate.annotations.Type;

import ch.unibe.scg.nullfinder.feature.extractor.IExtractor;

@Entity
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
	@Column(name = "extractor", nullable = false)
	@Type(type = "ch.unibe.scg.nullfinder.jpa.type.ExtractorType")
	protected IExtractor extractor;
	@Column(name = "manifestation", nullable = false)
	protected String manifestation;

	public Feature(NullCheck nullCheck, IExtractor extractor,
			String manifestation) {
		this.nullCheck = nullCheck;
		this.reasons = new ArrayList<>();
		this.featureReasons = new ArrayList<>();
		this.extractor = extractor;
		this.manifestation = manifestation;
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

	public String getManifestation() {
		return this.manifestation;
	}

	public void setManifestation(String manifestation) {
		this.manifestation = manifestation;
	}

}
