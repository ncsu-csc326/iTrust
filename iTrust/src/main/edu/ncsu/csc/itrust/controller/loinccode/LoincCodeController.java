package edu.ncsu.csc.itrust.controller.loinccode;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;

@ManagedBean(name = "loinccode_controller")
@SessionScoped
public class LoincCodeController extends iTrustController {

	private static final String INVALID_CODE = "Invalid LOINC Code";
    private static final String UNKNOWN_ERROR = "Unknown error";
    private static final String NONEXISTENT_CODE = "Code does not exist";
    private static final String DUPLICATE_CODE = "Cannot add duplicate code";
	private LOINCCodeMySQL sql;
	//private SessionUtils sessionUtils;

	public LoincCodeController() {
		try {
			sql = new LOINCCodeMySQL();
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
	public LoincCodeController(DataSource ds) {
		sql = new LOINCCodeMySQL(ds);
	}

	/**
	 * Setter injection for lab procedure data. ONLY use for unit testing
	 * purposes.
	 */
	public void setSQLData(LOINCCodeMySQL data) {
		this.sql = data;
	}

	public void add(LOINCCode code) {
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

	public void edit(LOINCCode code) {
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


	public void remove(String loincCodeID) {
	    try {
            if (!sql.delete(new LOINCCode(loincCodeID, "", ""))){
                printFacesMessage(FacesMessage.SEVERITY_ERROR, NONEXISTENT_CODE, NONEXISTENT_CODE, null);
            }
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
	}

	public LOINCCode getCodeByID(String loincCodeID) {
	    try {
            return sql.getByCode(loincCodeID);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
        return null;
	}
	
	public List<LOINCCode> getCodesWithFilter(String filterString){		
	    try {
            return sql.getCodesWithFilter(filterString);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, UNKNOWN_ERROR, UNKNOWN_ERROR, null);
        }
        return new ArrayList<>();
	}
}
