package edu.ncsu.csc.itrust.controller.icdcode;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;

@ManagedBean(name = "icdcode_controller")
@SessionScoped
public class ICDCodeController extends iTrustController {

	private static final String INVALID_CODE = "Invalid ICD Code";
    private static final String UNKNOWN_ERROR = "Unknown error";
    private static final String NONEXISTENT_CODE = "Code does not exist";
    private static final String DUPLICATE_CODE = "Cannot add duplicate code";
	private ICDCodeMySQL sql;
	//private SessionUtils sessionUtils;

	public ICDCodeController() {
		try {
			sql = new ICDCodeMySQL();
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
	public ICDCodeController(DataSource ds) {
		sql = new ICDCodeMySQL(ds);
	}

	/**
	 * Setter injection for lab procedure data. ONLY use for unit testing
	 * purposes.
	 */
	public void setSQLData(ICDCodeMySQL data) {
		this.sql = data;
	}
	
	public void add(ICDCode code) {
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

	public void edit(ICDCode code) {
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


	public void remove(String icdCodeID) {
	    try {
            if (!sql.delete(new ICDCode(icdCodeID, null, false))){
                printFacesMessage(FacesMessage.SEVERITY_ERROR, NONEXISTENT_CODE, NONEXISTENT_CODE, null);
            }
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	}

	public ICDCode getCodeByID(String icdCodeID) {
	    try {
            return sql.getByCode(icdCodeID);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
        return null;
	}
	
	public List<ICDCode> getCodesWithFilter(String filterString){
	    try {
            return sql.getCodesWithFilter(filterString);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
        return new ArrayList<>();
	}
}
