package edu.ncsu.csc.itrust.controller.ndcode;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;

@ManagedBean(name = "ndccode_controller")
@SessionScoped
public class NDCCodeController extends iTrustController {

	private static final String INVALID_CODE = "Invalid NDC Code";
    private static final String UNKNOWN_ERROR = "Unknown error";
    private static final String NONEXISTENT_CODE = "Code does not exist";
    private static final String DUPLICATE_CODE = "Cannot add duplicate code";
	private NDCCodeMySQL sql;

	public NDCCodeController() {
		try {
			sql = new NDCCodeMySQL();
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
	public NDCCodeController(DataSource ds) {
		sql = new NDCCodeMySQL(ds);
	}

	/**
	 * Setter injection for lab procedure data. ONLY use for unit testing
	 * purposes.
	 */
	public void setSQLData(NDCCodeMySQL data) {
		this.sql = data;
	}

	/**
	 * Adds an ndcCode.
	 * 
	 * @param code 
	 *            The code to add
	 */
	public void add(NDCCode code) {
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
	 * Updates an NDCCode. Prints FacesContext info message when
	 * successfully updated, error message when the update fails.
	 * 
	 * @param code 
	 *            The code to add
	 */
	public void edit(NDCCode code) {
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


	public void remove(String ndcCodeID) {
	    try {
            if (!sql.delete(new NDCCode(ndcCodeID, ""))){
                printFacesMessage(FacesMessage.SEVERITY_ERROR, NONEXISTENT_CODE, NONEXISTENT_CODE, null);
            }
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	}

	public NDCCode getCodeByID(String ndcCodeID) {
	    try {
            return sql.getByCode(ndcCodeID);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
        return null;
	}
	
	public List<NDCCode> getCodesWithFilter(String filterString){
	    try {
            return sql.getCodesWithFilter(filterString);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
        return new ArrayList<>();
	}
}
