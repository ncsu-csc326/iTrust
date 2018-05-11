<%@page import="java.util.LinkedList"%>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologyOVAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<% pageTitle = "iTrust - Ophthalmology";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		String view = "";
		if (request.getParameter("view") != null)
			view = "?view";
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/ophthalmologyHome.jsp" + view);
		return;
	}
	
	ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
	PatientBean chosenPatient = patientAction.getPatient(pidString);

	
	//also handle when other JSPs redirect here after successfully doing whatever it is they do
	if (request.getParameter("addOV") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Ophthalmology Office Visit successfully added</p>");
	}
	else if (request.getParameter("editOV") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Ophthalmology Office Visit successfully edited</p>");
	}
	
	ViewOphthalmologyOVAction viewAction = new ViewOphthalmologyOVAction(prodDAO, loggedInMID);
	List<OphthalmologyOVRecordBean> beans = viewAction.getOphthalmologyOVByMID(Long.parseLong(pidString));
	PatientDAO patients = new PatientDAO(prodDAO);
	PatientBean patient = null;
	
	try{
		patient = patients.getPatient(Long.parseLong(pidString));
	} catch (NumberFormatException e) {
		throw new ITrustException("Illegal patient id string");
	}
	if (patient == null) {
		throw new ITrustException("Selected patient does not exist");
	}
%>

<div align=center>
	<%
		//if specialty is oph and this is acting as home, not view, show the initialize/add office visit buttons
		ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
		PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
		if (currentPersonnel != null 
				&& (currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist") || currentPersonnel.getSpecialty().equalsIgnoreCase("optometrist"))
				&& request.getParameter("view") == null) {
			out.write("<form action=\"/iTrust/auth/hcp/addOphthalmologyOV.jsp\" method=\"post\" id=\"addOVButtonForm\">");
				out.write("<input style=\"font-size: 150%; font-weight: bold;\" type=submit value=\"Add Ophthalmology Office Visit\">");
			out.write("</form>");
		}
	%>
	<h3>View Prior Records</h3>
	<p>
	<%
		if (beans != null && beans.size() > 0) {
			for (OphthalmologyOVRecordBean bean : beans) {
				if (bean != null) {
						out.write("<a href=\"/iTrust/auth/hcp/viewOphthalmologyOV.jsp?oid=" + bean.getOid() + "\">"
								+ bean.getVisitDateString() + "</a><br />");
				}
			}
		}
		else {
			out.write("No prior records");
		}
	%>
	</p>
	<br />
</div>
<%@include file="/footer.jsp" %>