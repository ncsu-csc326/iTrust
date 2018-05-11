package edu.ncsu.csc.itrust.controller.ndcode;

import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;


@ManagedBean(name = "ndc_code_form")
@ViewScoped
public class NDCCodeForm {
	
    private NDCCodeController controller;
    private NDCCode ndcCode;

    private String code;
    private String description;

    private String search;
    private boolean displayCodes;

    public NDCCodeForm() {
        this(null);
    }

    public NDCCodeForm(NDCCodeController cptCodeController) {
        controller = (cptCodeController == null) ? new NDCCodeController() : cptCodeController;
        search = "";
        setDisplayCodes(false);
    }

    public void add() {
        setNDCCode(new NDCCode(code, description));
        controller.add(ndcCode);
        controller.logTransaction(TransactionType.DRUG_CODE_ADD, code);
        code = "";
        description = "";
    }

    public void update() {
        setNDCCode(new NDCCode(code, description));
        controller.edit(ndcCode);
		controller.logTransaction(TransactionType.DRUG_CODE_EDIT, code);
        code = "";
        description = "";
    }

    public void delete() {
        setNDCCode(new NDCCode(code, description));
        controller.remove(code);
        code = "";
        description = "";
    }

    public List<NDCCode> getCodesWithFilter() {
    	List<NDCCode> codes = Collections.emptyList();
    	if(!"".equals(search)) { // Only search if there's a search query
    		codes = controller.getCodesWithFilter(search);
    	}
    	return codes;
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

    public NDCCode getNDCCode() {
        return ndcCode;
    }

    public void setNDCCode(NDCCode ndcCode) {
        this.ndcCode = ndcCode;
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
 			logViewDrugCodes();
 		}
    }
    
    /**
	 * Logs a view action for each drug code matching the current search query.
	 * Only logs if search query is non-empty.
	 */
    private void logViewDrugCodes() {
    	if(!"".equals(search)) {
	 		for (NDCCode code : controller.getCodesWithFilter(search)) {
	 			controller.logTransaction(TransactionType.DRUG_CODE_VIEW, code.getCode());
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
