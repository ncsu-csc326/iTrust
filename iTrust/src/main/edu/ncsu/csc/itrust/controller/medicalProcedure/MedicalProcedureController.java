package edu.ncsu.csc.itrust.controller.medicalProcedure;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedure;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedureMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "medical_procedure_controller")
@SessionScoped
public class MedicalProcedureController extends iTrustController {
    private static final String INVALID_MEDICAL_PROCEDURE = "Invalid Medical Procedure";
    MedicalProcedureMySQL sql;
    
    public MedicalProcedureController() throws DBException{
        super();
        sql = new MedicalProcedureMySQL();
    }
    
    public MedicalProcedureController(DataSource ds){
        super();
        sql = new MedicalProcedureMySQL(ds);
    }
    
    public void add(MedicalProcedure mp){
        try {
            if (sql.add(mp)) {
                printFacesMessage(FacesMessage.SEVERITY_INFO, "Medical Procedure successfully created",
                        "Medical Procedure successfully created", null);
                Long ovid = getSessionUtils().getCurrentOfficeVisitId();
                logTransaction(TransactionType.PROCEDURE_ADD, ovid == null ? null : ovid.toString());
            } else {
                throw new Exception();
            }
        } catch (SQLException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_MEDICAL_PROCEDURE, e.getMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_MEDICAL_PROCEDURE, INVALID_MEDICAL_PROCEDURE, null);
        }
    }
    
    public void edit(MedicalProcedure mp){
        try {
            if (sql.update(mp)) {
                printFacesMessage(FacesMessage.SEVERITY_INFO, "Medical Procedure successfully updated",
                        "Medical Procedure successfully updated", null);
                Long ovid = getSessionUtils().getCurrentOfficeVisitId();
                logTransaction(TransactionType.PROCEDURE_EDIT, ovid == null ? null : ovid.toString());
            } else {
                throw new Exception();
            }
        } catch (SQLException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_MEDICAL_PROCEDURE, e.getMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_MEDICAL_PROCEDURE, INVALID_MEDICAL_PROCEDURE, null);
        }
    }
    
    public void remove(long mpID) {
        try {
            if (sql.remove(mpID)) {
                printFacesMessage(FacesMessage.SEVERITY_INFO, "Medical Procedure successfully deleted",
                        "Medical Procedure successfully deleted", null);
                Long ovid = getSessionUtils().getCurrentOfficeVisitId();
                logTransaction(TransactionType.PROCEDURE_REMOVE, ovid == null ? null : ovid.toString());
            } else {
                throw new Exception();
            }
        } catch (SQLException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_MEDICAL_PROCEDURE, e.getMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_MEDICAL_PROCEDURE, INVALID_MEDICAL_PROCEDURE, null);
        }
    }
    
    public List<MedicalProcedure> getMedicalProceduresByOfficeVisit(String officeVisitID){
        List<MedicalProcedure> medicalProcedures = Collections.emptyList();
        long ovID = -1;
        if ( officeVisitID != null ) {
            ovID = Long.parseLong(officeVisitID);
            try {
                medicalProcedures = sql.getMedicalProceduresForOfficeVisit(ovID);
            } catch (Exception e) {
                printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Medical Procedures", "Unable to Retrieve Medical Procedures", null);
            }
        }
        return medicalProcedures;
    }
    
    public String getCodeName(String codeString){
        String codeName = "";
        
        try {
            codeName = sql.getCodeName(codeString);
        }  catch (SQLException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Medical Procedure", e.getMessage(), null);
        }
        
        return codeName;
    }

    public void setSQL(MedicalProcedureMySQL newsql) {
        sql = newsql;
    }
}
