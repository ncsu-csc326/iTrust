package edu.ncsu.csc.itrust.model.icdcode;

public class ICDCode {
	/**
	 * Code of the ICDCode for diagnosis, cannot be more than 8 characters long.
	 */
	private String code;
	
	/**
	 * Name of the code of diagnosis, cannot be more than 30 characters long.
	 */
	private String name;
	
	/**
	 * Represents whether the symptom is chronic. 
	 */
	private boolean isChronic;

	/**
	 * Constructor for creating ICDCode instance.
	 * 
	 * @param code
	 * 			ICD10CM code, cannot be more than 8 characters long
	 * @param name
	 * 			Name of the diagnosis, cannot be more than 30 characters long
	 * @param isChronic
	 * 			Whether if the symptom is chronic
	 */
	public ICDCode(String code, String name, boolean isChronic) {
		super();
		this.code = code;
		this.name = name;
		this.isChronic = isChronic;
	}

	public ICDCode() {
        // TODO Auto-generated constructor stub
    }

    /**
	 * @return ICD10CM code of the instance
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the ICD10CM code of the instance.
	 * @param code
	 * 		new ICD10CM code of the instance
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return diagnosis name of the instance
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the diagnosis name of the instance.
	 * @param code
	 * 		new diagnosis name of the instance
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isChronic flag of the instance
	 */
	public boolean isChronic() {
		return isChronic;
	}

	/**
	 * Sets the isChronic flag of the instance.
	 * @param code
	 * 		new isChronic flag of the instance
	 */
	public void setChronic(boolean isChronic) {
		this.isChronic = isChronic;
	}
	
	public String toString(){
	    String ret = code + " - " + name + " - "; 
	    if( isChronic)
	        return ret + "Chronic";
        return  ret + "Not Chronic";
    }

}
