<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FlagsBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - View Obstetrics Record";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/viewObstetricsRecord.jsp");
		return;
	}
	
	ObstetricsRecordBean bean = null;
	PatientBean patient = null;
	boolean twinsChecked = false;
	boolean placentaChecked = false;
	
	//get the ObstetricsRecordBean given by the URL param
	String oidString = request.getParameter("oid");
	if (oidString != null && !oidString.equals("")) {
		long oid = Long.parseLong(request.getParameter("oid"));
		ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
		bean = viewObstetrics.getViewableObstetricsRecords(oid);
		
		//then grab the associated PatientBean
		ViewPatientAction viewPatient = new ViewPatientAction(prodDAO, loggedInMID, pidString);
		patient = viewPatient.getPatient(pidString);
		
		//finally, let's grab the manual flags to check checkboxes as necessary (twins and low-lying placenta)
		twinsChecked = viewObstetrics.getFlagForRecord(bean, FlagValue.Twins).isFlagged();
		placentaChecked = viewObstetrics.getFlagForRecord(bean, FlagValue.LowLyingPlacenta).isFlagged();
	}
	else {
		throw new ITrustException("Invalid Obstetrics ID passed to the View page");
	}
	
	//now check this bean's status AND the HCP's specialty to see if should redirect to the edit page
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (bean.getPregnancyStatus().equals(PregnancyStatus.Office)
			&& currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
		response.sendRedirect("/iTrust/auth/hcp/editObstetricsRecord.jsp?oid=" + oidString);
	}
%>
<div align=center>
	<form id="status">
	<table class="fTable" align="center">
		<tr><th colspan="3">View Obstetrics Record</th></tr>
		<!-- These fields are universal to all pregnancy statuses -->
		<tr>
			<td width="250px">Patient Name: </td>
			<td width="250px"><% out.write(patient.getFullName()); %></td>
			<td width="50px"></td>
		</tr>
		<tr>
			<td>Visit type:</td>
			<td><% out.write(bean.getPregnancyStatus().toString()); %></td>
			<td></td>
		</tr>
		<tr>
			<td>Weeks pregnant:</td>
			<td><% out.write(bean.getWeeksPregnant()); %></td>
			<td></td>
		</tr>
		<%
		//show these if the the status is NOT complete
		if (!bean.getPregnancyStatus().equals(PregnancyStatus.Complete)) {
			out.write("<tr>");
				out.write("<td>Date of visit:</td>");
				out.write("<td>" + bean.getDateVisitString() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
			out.write("<tr>");
				out.write("<td>Last menstrual period:</td>");
				out.write("<td>" + bean.getLmpString() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
			out.write("<tr>");
				out.write("<td>Estimated delivery date:</td>");
				out.write("<td>" + bean.getEddString() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
			
			//and an office visit has other fields
			if (bean.getPregnancyStatus().equals(PregnancyStatus.Office)) {
				out.write("<tr>");
					out.write("<td>Weight:</td>");
					out.write("<td>" + bean.getWeight() + " lbs</td>");
					out.write("<td></td>");
				out.write("</tr>");
				out.write("<tr>");
					out.write("<td>Blood Pressure:</td>");
					out.write("<td>" + bean.getBloodPressureS() + " / " + bean.getBloodPressureD() + "</td>");
					out.write("<td></td>");
				out.write("</tr>");
				out.write("<tr>");
					out.write("<td>Fetal Heart Rate:</td>");
					out.write("<td>" + bean.getFhr() + " bpm</td>");
					out.write("<td></td>");
				out.write("</tr>");
				out.write("<tr>");
					out.write("<td>Fundal Height of the Uterus:</td>");
					out.write("<td>" + bean.getFhu() + " cm</td>");
					out.write("<td></td>");
				out.write("</tr>");
				out.write("<tr>");
					out.write("<td>Low-lying Placenta: </td>");
					out.write("<td><input name=\"placenta\" type=\"checkbox\" " + ((placentaChecked)?"checked=\"checked\" value=\"on\" ":"") + "disabled/></td>");
					out.write("<td></td>");
				out.write("</tr>");
			}
		}
		//only show these fields if the pregnancyStatus is complete
		else {
			out.write("<tr>");
				out.write("<td>Year of conception:</td>");
				out.write("<td>" + bean.getYearConception() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
			out.write("<tr>");
				out.write("<td>Hours in labor:</td>");
				out.write("<td>" + bean.getHoursInLabor() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
			out.write("<tr>");
				out.write("<td>Delivery type:</td>");
				out.write("<td>" + bean.getDeliveryType().toString() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
		}
		//finally, write these below the others regardless of pregnancyStatus
		out.write("<tr>");
			out.write("<td>Twins: </td>");
			out.write("<td><input name=\"twins\" type=\"checkbox\" " + ((twinsChecked)?"checked=\"checked\" value=\"on\" ":"") + "disabled/></td>");
			out.write("<td></td>");
		out.write("</tr>");
		
		out.write("</table><br />"); //end the main table
		%>
	</form>
</div>
<p><br/><a href="/iTrust/auth/hcp/obstetricsHome.jsp">Back to Home</a></p>
<%@include file="/footer.jsp" %>