package edu.ncsu.csc.itrust.controller.cptcode;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;

@ManagedBean(name = "cptcode_controller")
@SessionScoped
public class CPTCodeController extends iTrustController {

	private static final String INVALID_CODE = "Invalid CPT Code";
	private static final String UNKNOWN_ERROR = "Unknown error";
	private static final String NONEXISTENT_CODE = "Code does not exist";
    private static final String DUPLICATE_CODE = "Cannot add duplicate code";
	private CPTCodeMySQL sql;
	//private SessionUtils sessionUtils;

	public CPTCodeController() {
		try {
			sql = new CPTCodeMySQL();
		} catch (DBException e) {
			sql = null;
		}
	}

	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public CPTCodeController(DataSource ds) {
		sql = new CPTCodeMySQL(ds);
	}

	/**
	 * Setter injection for lab procedure data. ONLY use for unit testing
	 * purposes.
	 */
	public void setMySQL(CPTCodeMySQL data) {
		this.sql = data;
	}

	/**
	 * Adds a cptCode.
	 * 
	 * @param code 
	 *            The code to add
	 */
	public void add(CPTCode code) {
		try {
            if (!sql.add(code)){
                printFacesMessage(FacesMessage.SEVERITY_ERROR, DUPLICATE_CODE, DUPLICATE_CODE, null);
            }
        } catch (FormValidationException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_CODE, e.getErrorList().toString(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	}

	/**
	 * Updates a cptCode. Prints FacesContext info message when
	 * successfully updated, error message when the update fails.
	 * 
	 * @param code 
	 *            The code to add
	 */
	public void edit(CPTCode code) {
	    try {
            if (!sql.update(code)){
                printFacesMessage(FacesMessage.SEVERITY_ERROR, NONEXISTENT_CODE, NONEXISTENT_CODE, null);
            }
        } catch (FormValidationException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_CODE, e.getErrorList().toString(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	}


	public void remove(String cptCodeID) {
	    try {
            if (!sql.delete(new CPTCode(cptCodeID, null))){
                printFacesMessage(FacesMessage.SEVERITY_ERROR, NONEXISTENT_CODE, NONEXISTENT_CODE, null);
            }
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	}

	public CPTCode getCodeByID(String cptCodeID) {
	    try {
            return sql.getByCode(cptCodeID);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	    return null;
	}
	
	public List<CPTCode> getCodesWithFilter(String filterString){
		try {
            return sql.getCodesWithFilter(filterString);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
		return new ArrayList<>();
	}
}
