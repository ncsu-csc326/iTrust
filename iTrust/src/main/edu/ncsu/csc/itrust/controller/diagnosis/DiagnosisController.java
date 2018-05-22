package edu.ncsu.csc.itrust.controller.diagnosis;

import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisData;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name="diagnosis_controller")
@SessionScoped
public class DiagnosisController extends iTrustController {
	private DiagnosisData sql;
	private static final String INVALID_DIAGNOSIS = "Invalid diagnosis";
	
	/**
	 * Default constructor for DiagnosisController
	 * @throws DBException
	 */
	public DiagnosisController() throws DBException {
		super();
		sql = new DiagnosisMySQL();
	}
	
	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public DiagnosisController(DataSource ds) throws DBException {
		super();
		this.sql = new DiagnosisMySQL(ds);
	}
	
	/**
	 * Set the MySQL instance for testing purposes
	 * @param sql
	 */
	public void setSql(DiagnosisMySQL sql){
	    this.sql = sql;
	}
	
	public void add(Diagnosis diagnosis) {
		try {
			if (sql.add(diagnosis)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, "Diagnosis is successfully created",
						"Diagnosis is successfully created", null);
				Long ovid = getSessionUtils().getCurrentOfficeVisitId();
				logTransaction(TransactionType.DIAGNOSIS_ADD, ovid == null ? null : ovid.toString());
			} else {
				throw new Exception();
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, INVALID_DIAGNOSIS, null);
		}
	}

	public void edit(Diagnosis diagnosis) {
		try {
			if (sql.update(diagnosis)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, "Prescription is successfully updated",
						"Prescription is successfully updated", null);
			} else {
				throw new Exception();
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, INVALID_DIAGNOSIS, null);
		}
	}

	public void remove(long diagnosisID) {
        try {
        	if (sql.remove(diagnosisID)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, "Diagnosis is successfully deleted",
						"Diagnosis is successfully deleted", null);
				Long ovid = getSessionUtils().getCurrentOfficeVisitId();
				logTransaction(TransactionType.DIAGNOSIS_REMOVE, ovid == null ? null : ovid.toString());
        	} else {
        		throw new Exception();
        	}
        } catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, INVALID_DIAGNOSIS, null);
		}
	}
	
	public List<Diagnosis> getDiagnosesByOfficeVisit(long officeVisitID) {
		List<Diagnosis> result = Collections.emptyList();
		try {
			result = sql.getAllDiagnosisByOfficeVisit(officeVisitID);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_DIAGNOSIS, INVALID_DIAGNOSIS, null);
		}
		return result;
	}
}
