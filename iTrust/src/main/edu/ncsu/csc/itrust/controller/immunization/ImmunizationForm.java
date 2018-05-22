package edu.ncsu.csc.itrust.controller.immunization;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "immunization_form")
@ViewScoped
public class ImmunizationForm {
    private Immunization immunization;
    private ImmunizationController controller;
    private SessionUtils sessionUtils;
    private CPTCodeMySQL cptData;
    
    public ImmunizationForm() {
        this(null, null, SessionUtils.getInstance(), null);
    }
    
    public ImmunizationForm(ImmunizationController ic, CPTCodeMySQL cptData, SessionUtils sessionUtils, DataSource ds) {
        this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
        try {
            if (ds == null) {
                this.cptData = (cptData == null) ? new CPTCodeMySQL() : cptData;
                controller = (ic == null) ? new ImmunizationController() : ic;
            } else {
                this.cptData = (cptData == null) ? new CPTCodeMySQL(ds) : cptData;
                controller = (ic == null) ? new ImmunizationController(ds) : ic;
            }
            clearFields();
            
        } catch (Exception e) {
            this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Immunization Controller Error",
                    "Immunization Controller Error", null);
        }
    }
    
    public void add(){
        controller.add(immunization);
        clearFields();
    }
    
    public void edit(){
        controller.edit(immunization);
        clearFields();
    }
    
    public void remove(String id){
        controller.remove(Long.parseLong(id));
        clearFields();
    }
    
    public List<Immunization> getImmunizationsByOfficeVisit(String ovID){
        List<Immunization> immunizations = Collections.emptyList();
        try {
            immunizations = controller.getImmunizationsByOfficeVisit(ovID);
        } catch (DBException e) {
            sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Immunization Controller Error", "Immunization Controller Error",
                    null);
        }
        return immunizations;
    }
    
    public Immunization getImmunization(){
        return immunization;
    }
    
    public void setImmunization(Immunization i){
        immunization = i;
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
    
    public void fillInput(String immunizationID, CPTCode code){
        immunization.setCptCode(code);
        immunization.setId(Long.parseLong(immunizationID));
    }

    private void clearFields() {
        immunization = new Immunization(0, sessionUtils.getCurrentOfficeVisitId(), new CPTCode());
    }
}
