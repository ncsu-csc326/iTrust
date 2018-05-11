package edu.ncsu.csc.itrust.controller.medicalProcedure;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedure;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "medical_procedure_form")
@ViewScoped
public class MedicalProcedureForm {
    private MedicalProcedure medicalProcedure;
    private MedicalProcedureController controller;
    private SessionUtils sessionUtils;
    private CPTCodeMySQL cptData;
    
    public MedicalProcedureForm(){
        this(null, null, SessionUtils.getInstance(), null);
    }
    
    public MedicalProcedureForm(MedicalProcedureController mpc, CPTCodeMySQL cptData, SessionUtils sessionUtils, DataSource ds){
        this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
        try {
            if (ds == null) {
                this.cptData = (cptData == null) ? new CPTCodeMySQL() : cptData;
                controller = (mpc == null) ? new MedicalProcedureController() : mpc;
            } else {
                this.cptData = (cptData == null) ? new CPTCodeMySQL(ds) : cptData;
                controller = (mpc == null) ? new MedicalProcedureController(ds) : mpc;
            }
            clearFields();
            
        } catch (Exception e) {
            this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Medical Procedure Controller Error",
                    "Medical Procedure Controller Error", null);
        }
    }
    
    public void add(){
        controller.add(medicalProcedure);
        clearFields();
    }
    
    public void edit(){
        controller.edit(medicalProcedure);
        clearFields();
    }
    
    public void remove(String id){
        controller.remove(Long.parseLong(id));
        clearFields();
    }
    
    public List<MedicalProcedure> getMedicalProceduresByOfficeVisit(String ovID){
        List<MedicalProcedure> medicalProcedures = Collections.emptyList();
        medicalProcedures = controller.getMedicalProceduresByOfficeVisit(ovID);
        return medicalProcedures;
    }
    
    public MedicalProcedure getMedicalProcedure(){
        return medicalProcedure;
    }
    
    public void setMedicalProcedure(MedicalProcedure mp){
        medicalProcedure = mp;
    }
    
    public String getCodeName(String codeString){
        return controller.getCodeName(codeString);
    }
    
    public List<CPTCode> getCPTCodes(){
        try {
            return cptData.getAll();
        } catch (SQLException e) {
            sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "CPT Code retrival error", "CPT Code retrival error",
                    null);
        }
        return Collections.emptyList();
    }
    
    public void fillInput(String medicalProcedureID, CPTCode code){
        medicalProcedure.setCptCode(code);
        medicalProcedure.setId(Long.parseLong(medicalProcedureID));
    }

    private void clearFields() {
        medicalProcedure = new MedicalProcedure();
        medicalProcedure.setOfficeVisitId(sessionUtils.getCurrentOfficeVisitId());
    }
}
