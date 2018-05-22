package edu.ncsu.csc.itrust.controller.loinccode;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.ncsu.csc.itrust.controller.loinccode.LoincCodeController;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "loinc_code_form")
@ViewScoped
public class LoincCodeForm {
	private LoincCodeController controller;
	private LOINCCode loincCode;
	// Input Fields
	private String code;
	private String component;
	private String kindOfProperty;
	private String timeAspect;
	private String system;
	private String scaleType;
	private String methodType;

	private String search;
	private boolean displayCodes;

	public LoincCodeForm() {
		this(null);
	}

	public LoincCodeForm(LoincCodeController loincCodeController) {
		controller = (loincCodeController == null) ? new LoincCodeController() : loincCodeController;
		search = "";
		setDisplayCodes(false);
	}

	public void add() {
		setLoincCode(new LOINCCode(code, component, kindOfProperty, timeAspect, system, scaleType, methodType));
		controller.add(loincCode);
		controller.logTransaction(TransactionType.LOINC_CODE_ADD, loincCode.getCode());
		code = "";
		component = "";
		kindOfProperty = "";
		timeAspect = "";
		system = "";
		scaleType = "";
		methodType = "";
	}

	public void update() {
		setLoincCode(new LOINCCode(code, component, kindOfProperty, timeAspect, system, scaleType, methodType));
		controller.edit(loincCode);
		controller.logTransaction(TransactionType.LOINC_CODE_EDIT, loincCode.getCode());
		code = "";
		component = "";
		kindOfProperty = "";
		timeAspect = "";
		system = "";
		scaleType = "";
		methodType = "";
	}

	public void delete() {
		setLoincCode(new LOINCCode(code, component, kindOfProperty, timeAspect, system, scaleType, methodType));
		controller.remove(code);
		code = "";
		component = "";
		kindOfProperty = "";
		timeAspect = "";
		system = "";
		scaleType = "";
		methodType = "";
	}

	public void fillInput(String code, String component, String kindOfProperty, String timeAspect, String system,
			String scaleType, String methodType) {
		this.code = code;
		this.component = component;
		this.kindOfProperty = kindOfProperty;
		this.timeAspect = timeAspect;
		this.system = system;
		this.scaleType = scaleType;
		this.methodType = methodType;
	}

	public List<LOINCCode> getCodesWithFilter() {
		return controller.getCodesWithFilter(search);
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

		if (this.displayCodes) {
			logViewLoincCodes();
		}
	}

	/**
	 * Logs a view action for each LOINC code matching the current search query.
	 * Only logs if search query is non-empty.
	 */
	private void logViewLoincCodes() {
		if (search != null && !search.equals("")) {
			for (LOINCCode code : controller.getCodesWithFilter(search)) {
				controller.logTransaction(TransactionType.LOINC_CODE_VIEW, code.getCode());
			}
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LOINCCode getLoincCode() {
		return loincCode;
	}

	public void setLoincCode(LOINCCode loincCode) {
		this.loincCode = loincCode;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getTimeAspect() {
		return timeAspect;
	}

	public void setTimeAspect(String timeAspect) {
		this.timeAspect = timeAspect;
	}

	public String getKindOfProperty() {
		return kindOfProperty;
	}

	public void setKindOfProperty(String kindOfProperty) {
		this.kindOfProperty = kindOfProperty;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getScaleType() {
		return scaleType;
	}

	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

}
