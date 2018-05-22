package edu.ncsu.csc.itrust.controller.immunization;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.immunization.ImmunizationMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "immunization_controller")
@SessionScoped
public class ImmunizationController extends iTrustController {

    private static final String INVALID_IMMUNIZATION = "Invalid Immmunization";
    
    ImmunizationMySQL sql;
    
    public ImmunizationController() throws DBException {
        super();
        sql = new ImmunizationMySQL();
    }
    
    public ImmunizationController(DataSource ds) {
        super();
        sql = new ImmunizationMySQL(ds);
    }
    
    /**
     * This is for setting the MySQL instance for testing purposes. Never use
     * this except for testing.
     * @param newSQL The new SQL instance
     */
    public void setSQL(ImmunizationMySQL newSQL){
        sql = newSQL;
    }
    
    public void add(Immunization immunization) {
        try {
            if (sql.add(immunization)) {
                printFacesMessage(FacesMessage.SEVERITY_INFO, "Immunization successfully created",
                        "Immunization successfully created", null);
                Long ovid = getSessionUtils().getCurrentOfficeVisitId();
                logTransaction(TransactionType.IMMUNIZATION_ADD, ovid == null ? null : ovid.toString());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_IMMUNIZATION, INVALID_IMMUNIZATION, null);
        }
    }
    
    public void edit(Immunization immunization) {
        try {
            if (sql.update(immunization)) {
                printFacesMessage(FacesMessage.SEVERITY_INFO, "Immunization successfully updated",
                        "Immunization successfully updated", null);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_IMMUNIZATION, INVALID_IMMUNIZATION, null);
        }
    }
    
    public void remove(long immunizationID) {
        try {
            if (sql.remove(immunizationID)) {
                printFacesMessage(FacesMessage.SEVERITY_INFO, "Immunization successfully deleted",
                        "Immunization successfully deleted", null);
                Long ovid = getSessionUtils().getCurrentOfficeVisitId();
                logTransaction(TransactionType.IMMUNIZATION_REMOVE, ovid == null ? null : ovid.toString());
            } else {
                throw new Exception();
            }
        } catch (SQLException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_IMMUNIZATION, e.getMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_IMMUNIZATION, INVALID_IMMUNIZATION, null);
        }
    }
    
    public List<Immunization> getImmunizationsByOfficeVisit(String officeVisitID) throws DBException {
        List<Immunization> immunizations = Collections.emptyList();
        long ovID = -1;
        if ( officeVisitID != null ) {
            ovID = Long.parseLong(officeVisitID);
            try {
                immunizations = sql.getImmunizationsForOfficeVisit(ovID);
            } catch (Exception e) {
                printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Immunizations", "Unable to Retrieve Immunizations", null);
            }
        }
        return immunizations;
    }
    
    public String getCodeName(String codeString){
        String codeName = "";
        
        try {
            codeName = sql.getCodeName(codeString);
        }  catch (SQLException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Immunization", e.getMessage(), null);
        }
        
        return codeName;
    }
}
