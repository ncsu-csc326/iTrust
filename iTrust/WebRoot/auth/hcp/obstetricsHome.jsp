<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="java.util.LinkedList"%>
<%@page import="edu.ncsu.csc.itrust.beans.FlagsBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.FlagsDAO"%>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%> 
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PreExistingConditionsDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<% pageTitle = "iTrust - Obstetrics";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		String view = "";
		if (request.getParameter("view") != null)
			view = "?view";
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/obstetricsHome.jsp" + view);
		return;
	}
	
	//if we do have a patient ID, check their gender
	ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
	PatientBean chosenPatient = patientAction.getPatient(pidString);

	//this test works and so does the redirect, but it has to be an exeption that's thrown for it to show up
	if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/obstetricsHome.jsp");
		throw new ITrustException("The patient is not eligible for obstetrics care."); //this shows up
	}
	
	//also handle when other JSPs redirect here after successfully doing whatever it is they do
	if (request.getParameter("initial") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Obstetrics Record successfully added</p>");
	}
	else if (request.getParameter("addOV") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Obstetrics Office Visit successfully added</p>");
	}
	else if (request.getParameter("editOV") != null) {
		out.write("<p style=\"border: 1px solid #090; color: #090; font-size: 20px; padding: 2px;\">" + 
			"Obstetrics Office Visit successfully edited</p>");
	}
	
	ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID); //, Long.parseLong(pidString), 0);
	List<ObstetricsRecordBean> beans = viewObstetrics.getViewableObstetricsRecordsByMID(Long.parseLong(pidString));
	FlagsDAO flagsDAO = new FlagsDAO(prodDAO);
	PreExistingConditionsDAO conditions = new PreExistingConditionsDAO(prodDAO);
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
		//if specialty is ob/gyn and this is acting as home, not view, show the initialize/add office visit buttons
		ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
		PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
		if (currentPersonnel != null 
				&& currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")
				&& request.getParameter("view") == null) {
			out.write("<form action=\"/iTrust/auth/hcp/addObstetricsInitialRecord.jsp\" method=\"post\" id=\"addInitialButtonForm\">");
				out.write("<input style=\"font-size: 150%; font-weight: bold;\" type=submit value=\"Initialize Obstetrics Record\">");
			out.write("</form><br />");
			out.write("<form action=\"/iTrust/auth/hcp/addObstetricsOfficeVisit.jsp\" method=\"post\" id=\"addOVButtonForm\">");
				out.write("<input style=\"font-size: 150%; font-weight: bold;\" type=submit value=\"Add Obstetrics Office Visit\">");
			out.write("</form>");
		}
	%>
	<h3>View Prior Records</h3>
	<p>
	<%
		if (beans != null && beans.size() > 0) {
			for (ObstetricsRecordBean bean : beans) {
				if (bean != null) {
					if (bean.getPregnancyStatus().equals(PregnancyStatus.Complete)) {
						out.write("<a href=\"/iTrust/auth/hcp/viewObstetricsRecord.jsp?oid=" + bean.getOid() + "\">"
								+ bean.getYearConception() + "-Completed</a><br />");
					}
					else {
						out.write("<a href=\"/iTrust/auth/hcp/viewObstetricsRecord.jsp?oid=" + bean.getOid() + "\">"
								+ bean.getDateVisitString() + "-" + bean.getPregnancyStatus() + "</a><br />");
					}
				}
			}
		}
		else {
			out.write("No prior records");
		}
	%>
	</p>
	<br />
		<%
			ObstetricsRecordBean currentPregnancy = null;
			for (ObstetricsRecordBean b : beans) {
				if (b.getPregnancyStatus() == PregnancyStatus.Initial) { 
					currentPregnancy = b;
					break;
				}
			}

			if(currentPregnancy != null) {
				%>
				<table>
				<tr>
				<td>
				<table class="fTable" align="center">
					<tr><th>Pregnancy Warning Flags for Patient</th></tr><%
				boolean hadFlags = false;
				for (FlagValue f: FlagValue.values()) {
					FlagsBean flag = new FlagsBean();
					flag.setMid(currentPregnancy.getMid());
					flag.setPregId(currentPregnancy.getPregId());
					flag.setFlagValue(f);
					flag = flagsDAO.getFlag(flag);
					if (flag != null && flag.isFlagged()) {
						hadFlags = true;
						%>
						<tr>
							<td><%=flag.getFlagValue().toString()%></td>
						</tr>
						<%
					}
				}
				//if there were NO flagged values in the DB, output None
				if (!hadFlags) {
					out.write("<tr><td>None</td></tr>");
				}
				%>
				</table>
				</td>
				<%
					%>
					<td>
					<table class="fTable" align="center">
						<tr><th>Allergies</th></tr>
						<%
							//Grab a list of allergies to be used if submitted OR to display if not
							EditPHRAction allergyAction = new EditPHRAction(prodDAO, loggedInMID, pidString);
							List<AllergyBean> allergyBeans = allergyAction.getAllergies();
							if (allergyBeans != null && allergyBeans.size() > 0) {
								for (AllergyBean allergy : allergyBeans) {
								out.write("<tr><td>" + allergy.getDescription() + "</td></tr>");
								}
							} else {
								out.write("<tr><td>None</td></tr>");
							}
							%>
					</table>
					</td>
				<td>
				<table class="fTable" align="center">
				<tr><th>Pre-existing Conditions</th></tr>
				<%
				boolean hadExisting = false;
				for (String str : conditions.getConditionsByMID(patient.getMID())) {
					hadExisting = true;
					%>
						<tr>
							<td><%=str%></td>
						</tr>
				<%
				}
				
				if (!hadExisting) {
					out.write("<tr><td>None</td></tr>");
				}
				%>
				</td>
				</tr>
				</table>
				<%
			}
			
		%>
</div>
<%@include file="/footer.jsp" %>