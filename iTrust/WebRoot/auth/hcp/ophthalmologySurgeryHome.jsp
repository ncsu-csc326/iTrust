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
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologySurgeryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologySurgeryRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<% pageTitle = "iTrust - Surgical Ophthalmology";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		String view = "";
		if (request.getParameter("view") != null)
			view = "?view";
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/ophthalmologySurgeryHome.jsp" + view);
		return;
	}
	
	ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
	PatientBean chosenPatient = patientAction.getPatient(pidString);

	
	//also handle when other JSPs redirect here after successfully doing whatever it is they do
	if (request.getParameter("addSurgery") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Surgical Ophthalmology Office Visit successfully added</p>");
	}
	else if (request.getParameter("editSurgery") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Surgical Ophthalmology Office Visit successfully edited</p>");
	}
	
	ViewOphthalmologySurgeryAction viewAction = new ViewOphthalmologySurgeryAction(prodDAO, loggedInMID);
	List<OphthalmologySurgeryRecordBean> beans = viewAction.getOphthalmologySurgeryByMID(Long.parseLong(pidString));
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
				&& (currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist"))
				&& request.getParameter("view") == null) {
			out.write("<form action=\"/iTrust/auth/hcp/addOphthalmologySurgery.jsp\" method=\"post\" id=\"addSurgeryButtonForm\">");
				out.write("<input style=\"font-size: 150%; font-weight: bold;\" type=submit value=\"Add Surgical Ophthalmology Office Visit\">");
			out.write("</form>");
		}
	%>
	<h3>View Prior Records</h3>
	<p>
	<%
		if (beans != null && beans.size() > 0) {
			for (OphthalmologySurgeryRecordBean bean : beans) {
				if (bean != null) {
						out.write("<a href=\"/iTrust/auth/hcp/viewOphthalmologySurgery.jsp?oid=" + bean.getOid() + "\">"
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