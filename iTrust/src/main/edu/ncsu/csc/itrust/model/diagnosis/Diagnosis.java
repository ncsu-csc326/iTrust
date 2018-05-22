package edu.ncsu.csc.itrust.model.diagnosis;

import edu.ncsu.csc.itrust.model.icdcode.ICDCode;

/**
 * Model of HCP's diagnosis of a patient during office visit.
 */
public class Diagnosis {

	/**
	 * Primary identifier of diagnosis.
	 */
	private long id;
	
	/**
	 * ID references the office visit that the diagnosis belongs.
	 */
	private long visitId;
	
	/**
	 * ICD10CM code representing description of the diagnosis.
	 */
	private ICDCode icdCode;

	/**
	 * Default constructor.
	 */
	public Diagnosis() {
		super();
		icdCode = new ICDCode();
	}
	
	/**
	 * Constructor for creating an instance of Diagnosis.
	 * 
	 * @param id
	 * 			id of diagnosis
	 * @param visitId
	 * 			office visit id of diagnosis
	 * @param icdCode
	 * 			ICD10CM code for diagnosis
	 */
	public Diagnosis(long id, long visitId, ICDCode icdCode) {
		super();
		this.id = id;
		this.visitId = visitId;
		this.icdCode = icdCode;
	}

	/**
	 * @return id of the instance
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return office visit id of the instance
	 */
	public long getVisitId() {
		return visitId;
	}

	/**
	 * Sets the office visit id of the instance.
	 * @param visitId
	 * 			new office visit id of the instance
	 */
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}

	/**
	 * @return ICD10CM code of the instance
	 */
	public ICDCode getIcdCode() {
		return icdCode;
	}

	/**
	 * Sets the ICD10CM code of the instance.
	 * @param icdCode
	 * 			new ICD10CM code of the instance
	 */
	public void setIcdCode(ICDCode icdCode) {
		this.icdCode = icdCode;
	}
	
	/**
	 * @return ICD10CM code string of the instance
	 */
	public String getCode() {
		return getIcdCode() == null ? "" : getIcdCode().getCode();
	}
	
	/**
	 * @return ICD10CM name description of the instance
	 */
	public String getName() {
		return getIcdCode() == null ? "" : getIcdCode().getName();
	}
}
