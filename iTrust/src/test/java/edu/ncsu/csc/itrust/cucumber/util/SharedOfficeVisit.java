package edu.ncsu.csc.itrust.cucumber.util;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class SharedOfficeVisit {
	private OfficeVisit ov;
	private OfficeVisitController ovc;
	private boolean wasAddSuccessful;
	private boolean hasError;
	public SharedOfficeVisit() throws Exception {
		hasError = false;
		wasAddSuccessful = false;
		ovc = new OfficeVisitController(((TestDAOFactory)TestDAOFactory.getTestInstance()).getDataSource());
	}
	public OfficeVisitController getOfficeVisitController() {
		return ovc;
	}
	public OfficeVisit getOfficeVisit() {
		return ov;
	}
	public void setOfficeVisit(OfficeVisit officeVisit) {
		this.ov = officeVisit;
	}
	public boolean wasAddSuccessful() {
		return wasAddSuccessful;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	public boolean hasError() {
		return hasError;
	}
	public void add() {
		wasAddSuccessful = ovc.addReturnResult(this.ov);
		setHasError(!wasAddSuccessful);
	}
}
