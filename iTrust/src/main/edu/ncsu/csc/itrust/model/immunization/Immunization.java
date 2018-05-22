package edu.ncsu.csc.itrust.model.immunization;

import edu.ncsu.csc.itrust.model.cptcode.CPTCode;

/**
 * Model of HCP's immunization of a patient during office visit.
 */
public class Immunization {

	/** Primary identifier of immunization. */
	private long id;
	
	/** ID references the office visit that the immunization belongs. */
	private long visitId;
	
	/** cpt code representing description of the immunization. */
	private CPTCode cptCode;

	public Immunization(){
	    cptCode = new CPTCode();
	}
	/**
	 * Constructor for creating an instance of immunization.
	 * 
	 * @param id
	 * 			id of immunization
	 * @param visitId
	 * 			office visit id of immunization
	 * @param cptCode
	 * 			cpt code for immunization
	 */
	public Immunization(long id, long visitId, CPTCode cptCode) {
		super();
		this.id = id;
		this.visitId = visitId;
		this.cptCode = cptCode;
	}

	/**
	 * @return id of the instance
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id of the instance.
	 * @param id
	 * 			new id of instance
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return cpt code of the instance
	 */
	public CPTCode getCptCode() {
		return cptCode;
	}

	/**
	 * Sets the cpt code of the instance.
	 * @param icdCode
	 * 			new cpt code of the instance
	 */
	public void setCptCode(CPTCode cptCode) {
		this.cptCode = cptCode;
	}
	
	/**
	 * @return cpt code string of the instance
	 */
	public String getCode() {
		return getCptCode().getCode();
	}
	
	/**
	 * @return cpt code description of the instance
	 */
	public String getName() {
		return getCptCode().getName();
	}
}