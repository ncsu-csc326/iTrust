package edu.ncsu.csc.itrust.controller.diagnosis;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "diagnosis_form")
@ViewScoped
public class DiagnosisForm {
	private Diagnosis diagnosis;
	private DiagnosisController controller;
	private SessionUtils sessionUtils;
	private ICDCodeMySQL icdData;
	
	public DiagnosisForm() {
		this(null, null, null, null);
	}
	
	public DiagnosisForm(DiagnosisController dc, ICDCodeMySQL icdData, SessionUtils sessionUtils, DataSource ds) {
		this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		try {
		    if (ds == null) {
    			this.controller = (dc == null) ? new DiagnosisController() : dc;
    			this.icdData = (icdData == null) ? new ICDCodeMySQL() : icdData;
		    } else {
		        this.icdData = (icdData == null) ? new ICDCodeMySQL(ds) : icdData;
                controller = (dc == null) ? new DiagnosisController(ds) : dc; 
		    }
		} catch (DBException e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Diagnosis Controller Error",
				"Diagnosis Procedure Controller Error", null);
		}
		clearFields();
	}
	
	/**
	 * @return list of diagnosis from the current office visit
	 */
	public List<Diagnosis> getDiagnosesByOfficeVisit() {
		return controller.getDiagnosesByOfficeVisit(sessionUtils.getCurrentOfficeVisitId());
	}
	
	public Diagnosis getDiagnosis(){
	    return diagnosis;
	}
	
	public void setDiagnosis(Diagnosis dia){
	    diagnosis = dia;
    }
	
	public void add() {
		controller.add(diagnosis);
	}
	
	public void edit() {
		controller.edit(diagnosis);
	}
	
	public void remove(String diagnosisId) {
		controller.remove(Long.parseLong(diagnosisId));
	}
	
	public List<ICDCode> getICDCodes(){
        try {
            return icdData.getAll();
        } catch (SQLException e) {
            sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "ICD Code retrival error", "ICD Code retrival error",
                    null);
        }
        return Collections.emptyList();
    }
	
	public void clearFields() {
		diagnosis = new Diagnosis();
		diagnosis.setVisitId(sessionUtils.getCurrentOfficeVisitId());
	}
}
