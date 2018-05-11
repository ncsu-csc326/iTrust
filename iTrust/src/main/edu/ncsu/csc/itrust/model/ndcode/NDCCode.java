package edu.ncsu.csc.itrust.model.ndcode;

public class NDCCode {
    private String code;
    private String description;
    
    public NDCCode() {
	}
    
	public NDCCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String toString(){
    	return code + " - " + description;
    }
}
