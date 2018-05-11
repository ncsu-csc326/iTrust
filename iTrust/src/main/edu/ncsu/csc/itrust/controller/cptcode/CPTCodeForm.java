package edu.ncsu.csc.itrust.controller.cptcode;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "cpt_code_form")
@ViewScoped
public class CPTCodeForm {

	private CPTCodeController controller;
    private CPTCode cptCode;
    private String code;
    private String description;

    private String search;
    private boolean displayCodes;

    public CPTCodeForm() {
        this(null);
    }

    public CPTCodeForm(CPTCodeController cptCodeController) {
        controller = (cptCodeController == null) ? new CPTCodeController() : cptCodeController;
        search = "";
        setDisplayCodes(false);
    }

    public void add() {
        setCptCode(new CPTCode(code, description));
        controller.add(cptCode);
		controller.logTransaction(TransactionType.MEDICAL_PROCEDURE_CODE_ADD, code);
		controller.logTransaction(TransactionType.IMMUNIZATION_CODE_ADD, code);
        code = "";
        description = "";
    }

    public void update() {
        setCptCode(new CPTCode(code, description));
        controller.edit(cptCode);
		controller.logTransaction(TransactionType.MEDICAL_PROCEDURE_CODE_EDIT, code);
		controller.logTransaction(TransactionType.IMMUNIZATION_CODE_EDIT, code);
        code = "";
        description = "";
    }

    public void delete() {
        setCptCode(new CPTCode(code, description));
        controller.remove(code);
        code = "";
        description = "";
    }

    public List<CPTCode> getCodesWithFilter() {
        return controller.getCodesWithFilter(search);
    }

    public void fillInput(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public CPTCode getCptCode() {
        return cptCode;
    }

    public void setCptCode(CPTCode cptCode) {
        this.cptCode = cptCode;
    }

    public boolean getDisplayCodes() {
        return displayCodes;
    }

    /**
	 * Sets whether or not search results matching the given search string
	 * should be rendered. If displayCodes is true, this logs the view action
	 * for all codes matching the search filter.
	 * 
	 * @param displayCodes
	 */
    public void setDisplayCodes(boolean displayCodes) {
        this.displayCodes = displayCodes;
        
        // Log if displaying search results
        if(this.displayCodes) {
        	logViewCPTCodes();
        }
    }
    
    /**
	 * Logs a view action for each CPT code matching the current search query.
	 * Only logs if search query is non-empty.
	 */
    private void logViewCPTCodes() {
    	if(!"".equals(search)) {
    		for(CPTCode code : controller.getCodesWithFilter(search)) {
    			controller.logTransaction(TransactionType.MEDICAL_PROCEDURE_CODE_VIEW, code.getCode());
    			controller.logTransaction(TransactionType.IMMUNIZATION_CODE_VIEW, code.getCode());
    		}
    	}
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
