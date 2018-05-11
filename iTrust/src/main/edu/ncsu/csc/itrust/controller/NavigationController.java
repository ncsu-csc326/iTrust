package edu.ncsu.csc.itrust.controller;

import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import edu.ncsu.csc.itrust.controller.user.patient.PatientController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "navigation_controller")
@RequestScoped
public class NavigationController {

	private PatientController patientController;

	public NavigationController() throws DBException {
		patientController = new PatientController();
	}

	/**
	 * Navigate to the getPatientID page if the current patientID stored in the
	 * session variable is null
	 * 
	 * @throws DBException
	 * @throws IOException
	 */
	public void redirectIfInvalidPatient() throws DBException, IOException {
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		long pid = 0;
		Map<String, Object> session = ctx.getSessionMap();
		Object pidObj = session.get("pid");
		if (pidObj instanceof Long) {
			pid = (long) pidObj;
		}
		if ((pidObj == null) || (!(patientController.doesPatientExistWithID(Long.toString(pid))))) {
			updatePatient();
		}
	}

	/**
	 * Navigate to getPatientID from current URL
	 * 
	 * @throws DBException
	 * @throws IOException
	 */
	public void updatePatient() throws DBException, IOException {
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		Object req = ctx.getRequest();
		String url = "";
		if (req instanceof HttpServletRequest) {
			HttpServletRequest req2 = (HttpServletRequest) req;
			url = req2.getRequestURI();
		}
		ctx.redirect("/iTrust/auth/getPatientID.jsp?forward=" + url);
	}

	public static void baseOfficeVisit() throws IOException {
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml");
		}
	}

	public static void editOfficeVisit() throws IOException {
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml");
		}
	}

	public static void officeVisitInfo(Long visitId) throws IOException {
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/hcp-uap/officeVisitInfo.xhtml?visitID=" + visitId);
		}
	}

	public static void officeVisitInfo() throws IOException {
		Long officeVisitId = SessionUtils.getInstance().getCurrentOfficeVisitId();
		officeVisitInfo(officeVisitId);
	}
	
	public void redirectToOfficeVisitInfoIfNeeded(boolean shouldRedirect) throws DBException, IOException {
		if (shouldRedirect) {
			baseOfficeVisit();
		}
	}
	
	public static void patientViewOfficeVisit() throws IOException {
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/patient/viewOfficeVisit.xhtml");
		}
	}
}