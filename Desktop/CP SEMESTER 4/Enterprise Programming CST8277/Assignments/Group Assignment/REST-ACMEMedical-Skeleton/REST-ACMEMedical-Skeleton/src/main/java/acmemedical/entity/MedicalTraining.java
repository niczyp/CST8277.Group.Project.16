/********************************************************************************************************
 * File:  MedicalTraining.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * 
 * Date Updated: 2025-04-06
 * @author Zachary Bernard
 * @author Prince Khakhriya
 * @author Melbinbenny Thomas
 * @author Nicholas Zypchen
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_training database table.
 */
//TODO MT01 - Add the missing annotations.
//TODO MT02 - Do we need a mapped super class?  If so, which one?
// Yes... we need a mapped superclass, and PojoBase already extends Serializable and provides primary key fields (id, created, updated)
@Entity
@Table(name = "medical_training")
@AttributeOverride(name = "id", column = @Column(name = "training_id"))
@NamedQuery(
	    name = "MedicalTraining.findById",
	    query = "SELECT mt FROM MedicalTraining mt WHERE mt.id = :param1"
	)
@NamedQuery(
		name = "MedicalTraining.findAll", 
		query = "SELECT mt FROM MedicalTraining mt"
	)

public class MedicalTraining extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_ID = "MedicalTraining.findById";
	public static final String ALL_TRAINING_QUERY_NAME = "MedicalTraining.findAll";

	// TODO MT03 - Add annotations for M:1.  What should be the cascade and fetch types?
	// optional = false because every medical training must be associated with a school.
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", referencedColumnName = "school_id", nullable = false)
	private MedicalSchool school;

	// TODO MT04 - Add annotations for 1:1.  What should be the cascade and fetch types?
	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "training_id", referencedColumnName = "training_id", nullable = false)
	private MedicalCertificate certificate;

	@Embedded
	private DurationAndStatus durationAndStatus;

	public MedicalTraining() {
		durationAndStatus = new DurationAndStatus();
	}

	public MedicalSchool getMedicalSchool() {
		return school;
	}

	public void setMedicalSchool(MedicalSchool school) {
		this.school = school;
	}

	public MedicalCertificate getCertificate() {
		return certificate;
	}
	
	public void setCertificate(MedicalCertificate certificate) {
		this.certificate = certificate;
	}
	
	public DurationAndStatus getDurationAndStatus() {
		return durationAndStatus;
	}

	public void setDurationAndStatus(DurationAndStatus durationAndStatus) {
		this.durationAndStatus = durationAndStatus;
	}
	
	//Inherited hashCode/equals NOT sufficient for this Entity class
	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		
		// include DurationAndStatus in identity
		return prime * result + Objects.hash(getId(), getDurationAndStatus());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof MedicalTraining otherMedicalTraining) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherMedicalTraining.getId()) &&
				Objects.equals(this.getDurationAndStatus(), otherMedicalTraining.getDurationAndStatus());
		}
		return false;
	}
}
