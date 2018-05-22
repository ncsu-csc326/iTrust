package edu.ncsu.csc.itrust.model.medicalProcedure;

import edu.ncsu.csc.itrust.model.cptcode.CPTCode;

public class MedicalProcedure {
    private long officeVisitId;
    private CPTCode cptCode;
    private long id;
    
    public MedicalProcedure(){
        cptCode = new CPTCode();
    }
    
    public long getOfficeVisitId() {
        return officeVisitId;
    }
    public void setOfficeVisitId(long officeVisitId) {
        this.officeVisitId = officeVisitId;
    }
    public CPTCode getCptCode() {
        return cptCode;
    }
    public void setCptCode(CPTCode code) {
        this.cptCode = code;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
