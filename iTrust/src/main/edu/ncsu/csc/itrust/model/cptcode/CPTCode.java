package edu.ncsu.csc.itrust.model.cptcode;

public class CPTCode {
	
	/** Code of the cptCode for immunization, cannot be more than 5 characters long. */
	private String code = "";
	
	/** Name of the code of diagnosis, cannot be more than 30 characters long. */
	private String name = "";
	
	/**
	 * Constructor for creating cptCode instance.
	 * 
	 * @param code
	 * 			cpt code, cannot be more than 5 characters long
	 * @param name
	 * 			Name of the immunizations, cannot be more than 30 characters long
	 */
	public CPTCode(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public CPTCode() {
        // TODO Auto-generated constructor stub
    }

    /**
	 * @return cpt code of the instance
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the cpt code of the instance.
	 * @param code
	 * 		new cpt code of the instance
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return immunization name of the instance
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the immunization name of the instance.
	 * @param code
	 * 		new immunization name of the instance
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
        return code + " - " + name;
    }

}
