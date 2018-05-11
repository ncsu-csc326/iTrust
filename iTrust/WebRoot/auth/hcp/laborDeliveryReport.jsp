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

<% pageTitle = "iTrust - Labor and Delivery Report";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/laborDeliveryReport.jsp");
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
	
	//---get all the obstetrics records---
	ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
	//we need a list of all past pregnancies to display
	List<ObstetricsRecordBean> pastPregnancies = viewObstetrics.getViewableObstetricsRecordsByMIDType(
			Long.parseLong(pidString), PregnancyStatus.Complete);
	//the most recent initial record
	List<ObstetricsRecordBean> initialRecords = viewObstetrics.getViewableObstetricsRecordsByMIDType(
			Long.parseLong(pidString), PregnancyStatus.Initial);
	ObstetricsRecordBean initialRecord = null;
	if (initialRecords == null || initialRecords.size() == 0) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/obstetricsHome.jsp");
		throw new ITrustException("The patient chosen is not an initialized obstetrics patient"); //this shows up
	} else {
		initialRecord = initialRecords.get(0);
	}
	//apparently, get ALL office visits
	List<ObstetricsRecordBean> officeVisits = viewObstetrics.getViewableObstetricsRecordsByMIDType(
			Long.parseLong(pidString), PregnancyStatus.Office);
	
	//prepare to get all flags
	FlagsDAO flagsDAO = new FlagsDAO(prodDAO);
	
	//prepare to get all preexisting conditions
	PreExistingConditionsDAO conditions = new PreExistingConditionsDAO(prodDAO);
	
	//get the patient bean for name and blood type
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
	
	//get the personnelbean for specialty checking (this probably should be done) <- I disagree. I think anyone should be able to view this page
	//ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	//PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
%>

<div align=center>
	<h2>Labor and Delivery Report</h2>
	<h3>Patient Information</h3>
	<!-- TODO EDD, blood type, name here -->
	<table class="fTable">
		<tr>
			<th colspan="2"><% out.write(patient.getFullName()); %></th>
		</tr>
		<tr>
			<td>EDD: </td>
			<td><% out.write(initialRecord.getEddString()); %></td>
		</tr>
		<tr>
			<td>Blood type: </td>
			<td><% out.write(patient.getBloodType().toString()); %></td>
		</tr>
	</table>
	
	<h3>Office Visits</h3>
	<%
		// Past pregnancies. We want to list this in the same style
		// that we do on the obstetrics initialization page.
		if (officeVisits != null && officeVisits.size() > 0) {
			// Use a stringbuilder so we're more memory efficient.
			StringBuilder sbPast = new StringBuilder();
			sbPast.append("<table class=\"fTable\">")
				.append("<tr>")
				.append("<th># Weeks Pregnant</th>")
				.append("<th>Weight</th>")
				.append("<th>Blood Pressure</th>")
				.append("<th>FHR</th>")
				.append("<th>FHU</th>")
				.append("</tr>");
			int count = 0;
			for (ObstetricsRecordBean officeVisit : officeVisits) {
				if (officeVisit != null) {
					sbPast.append("<tr>")
						.append("<td>").append(officeVisit.getWeeksPregnant()).append("</td>")
						.append("<td>").append(officeVisit.getWeight()).append(" lbs</td>")
						.append("<td>").append(officeVisit.getBloodPressureS()).append(" / ").append(officeVisit.getBloodPressureD()).append(" mm Hg</td>")
						.append("<td>").append(officeVisit.getFhr()).append(" bpm</td>")
						.append("<td>").append(officeVisit.getFhu()).append(" cm</td>")
					.append("</tr>");
					count++;
				}
			}
			sbPast.append("</table>");
			out.write(sbPast.toString());
		}
		else {
			out.write("<p>No office visit records exist for this patient.</p>");
		}
	%>
	
	<h3>Past Pregnancies</h3>
	
	<%
		// Past pregnancies. We want to list this in the same style
		// that we do on the obstetrics initialization page.
		if (pastPregnancies != null && pastPregnancies.size() > 0) {
			// Use a stringbuilder so we're more memory efficient.
			StringBuilder sbPast = new StringBuilder();
			sbPast.append("<table class=\"fTable\">")
				.append("<tr>")
				.append("<th>Year of Conception</th>")
				.append("<th># Weeks Pregnant</th>")
				.append("<th># Hours in Labor</th>")
				.append("<th>Delivery Type</th>")
				.append("</tr>");
			int count = 0;
			for (ObstetricsRecordBean pastPregnancy : pastPregnancies) {
				if (pastPregnancy != null) {
					sbPast.append("<tr>")
						.append("<td>").append(pastPregnancy.getYearConception()).append("</td>")
						.append("<td>").append(pastPregnancy.getWeeksPregnant()).append("</td>")
						.append("<td>").append(pastPregnancy.getHoursInLabor()).append("</td>")
						.append("<td>").append(pastPregnancy.getDeliveryType().toString()).append("</td>")
					.append("</tr>");
					count++;
				}
			}
			sbPast.append("</table>");
			out.write(sbPast.toString());
		}
		else {
			out.write("<p>No prior pregnancy records exist for this patient.</p>");
		}
	%>
	<br />
		<%
			ObstetricsRecordBean currentPregnancy = null;
			// 06NOV2014 (Avy)
			// If we already have the initial record from above, we can use it here
			currentPregnancy = initialRecord;

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
						List<String> patientConditions = conditions.getConditionsByMID(patient.getMID());
						if(patientConditions != null && patientConditions.size() > 0) {
							for (String str : conditions.getConditionsByMID(patient.getMID())) {
								out.write("<tr><td>" + str + "</td></tr>");
	            			}
						} else {
							out.write("<tr><td>None</td></tr>");
						}
						%> 
					</table>
				</td>
				</tr>
				</table>
				<%
			
	}	
		%>
</div>
<%@include file="/footer.jsp" %>
