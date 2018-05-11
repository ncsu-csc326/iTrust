package edu.ncsu.csc.itrust.controller.icdcode;

import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "icd_code_form")
@ViewScoped
public class ICDCodeForm {

    private ICDCodeController controller;
    private ICDCode icdCode;
    private String code;
    private String description;
    private boolean isChronic;

    private String search;
    private boolean displayCodes;

    public ICDCodeForm() {
        this(null);
    }

    public ICDCodeForm(ICDCodeController icdCodeController) {
        controller = (icdCodeController == null) ? new ICDCodeController() : icdCodeController;
        search = "";
        setDisplayCodes(false);
    }

    public void add() {
        setIcdCode(new ICDCode(code, description, isChronic));
        controller.add(icdCode);
        controller.logTransaction(TransactionType.DIAGNOSIS_CODE_ADD, code);
        code = "";
        description = "";
        isChronic = false;
    }

    public void update() {
        setIcdCode(new ICDCode(code, description, isChronic));
        controller.edit(icdCode);
        controller.logTransaction(TransactionType.DIAGNOSIS_CODE_EDIT, code);
        code = "";
        description = "";
        isChronic = false;
    }

    public void delete() {
        setIcdCode(new ICDCode(code, description, isChronic));
        controller.remove(code);
        code = "";
        description = "";
        isChronic = false;
    }

    public void fillInput(String code, String description, boolean isChronic) {
        this.code = code;
        this.description = description;
        this.isChronic = isChronic;
    }

    public List<ICDCode> getCodesWithFilter() {
    	List<ICDCode> codes = Collections.emptyList();
    	if (!"".equals(search)) { // Only search if there's a search query
			codes = controller.getCodesWithFilter(search);
		}
    	return codes;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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
 		if (this.displayCodes) {
 			logViewDiagnosisCodes();
 		}
    }
    
    /**
	 * Logs a view action for each diagnosis code matching the current search query.
	 * Only logs if search query is non-empty.
	 */
    private void logViewDiagnosisCodes() {
 		if (!"".equals(search)) {
	 		for (ICDCode code : controller.getCodesWithFilter(search)) {
	 			controller.logTransaction(TransactionType.DIAGNOSIS_CODE_VIEW, code.getCode());
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

    public ICDCode getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(ICDCode icdCode) {
        this.icdCode = icdCode;
    }

    public boolean getIsChronic() {
        return isChronic;
    }

    public void setIsChronic(boolean isChronic) {
        this.isChronic = isChronic;
    }
}
