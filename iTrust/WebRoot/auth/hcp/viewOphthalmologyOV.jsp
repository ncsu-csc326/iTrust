<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologyOVAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyDiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOPDiagnosesAction"%>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - View Ophthalmology Record";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/viewOphthalmologyOV.jsp");
		return;
	}
	
	OphthalmologyOVRecordBean bean = null;
	PatientBean patient = null;
	
	//get the OphthalmologyOVRecordBean given by the URL param
	String oidString = request.getParameter("oid");
	if (oidString != null && !oidString.equals("")) {
		long oid = Long.parseLong(request.getParameter("oid"));
		ViewOphthalmologyOVAction viewAction = new ViewOphthalmologyOVAction(prodDAO, loggedInMID);
		bean = viewAction.getOphthalmologyOVForHCP(oid);
		
		//then grab the associated PatientBean
		ViewPatientAction viewPatient = new ViewPatientAction(prodDAO, loggedInMID, pidString);
		patient = viewPatient.getPatient(pidString);
		
	}
	else {
		throw new ITrustException("Invalid Ophthalmology ID passed to the View page");
	}
	
	//now check this bean's status AND the HCP's specialty to see if should redirect to the edit page
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist") || currentPersonnel.getSpecialty().equalsIgnoreCase("optometrist")) {
		response.sendRedirect("/iTrust/auth/hcp/editOphthalmologyOV.jsp?oid=" + oidString);
	}
%>
<div align=center>
	<form id="status">
	<table class="fTable" align="center">
		<tr><th colspan="3">View Ophthalmology Record</th></tr>
		<tr>
			<td width="250px">Patient Name: </td>
			<td width="250px"><% out.write(patient.getFullName()); %></td>
			<td width="50px"></td>
		</tr>
		<%
		//show these if the the status is NOT complete
			out.write("<tr>");
				out.write("<td>Date of visit:</td>");
				out.write("<td>" + bean.getVisitDateString() + "</td>");
				out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Visual Acuity Numerator OD:</td>");
			out.write("<td>" + bean.getVaNumOD() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Visual Acuity Denumerator OD:</td>");
			out.write("<td>" + bean.getVaDenOD() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Visual Acuity Numerator OS:</td>");
			out.write("<td>" + bean.getVaNumOS() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Visual Acuity Denumerator OS:</td>");
			out.write("<td>" + bean.getVaDenOS() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Sphere(OD):</td>");
			out.write("<td>" + bean.getSphereOD() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Sphere(OS):</td>");
			out.write("<td>" + bean.getSphereOS() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			String cylODout = ""; //we only print the cylinder if we have one.
			if(bean.getCylinderOD() != null){ //this is the default value, and means that there is no cylinder value
				cylODout = Double.toString(bean.getCylinderOD());
			}
			out.write("<tr>");
			out.write("<td>Cyldinder(OD):</td>");
			out.write("<td>" + cylODout + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			String cylOSout = ""; //we only print the cylinder if we have one.
			if(bean.getCylinderOS() != null){ //this is the default value, and means that there is no cylinder value
				cylOSout = Double.toString(bean.getCylinderOS());
			}
			out.write("<tr>");
			out.write("<td>Cyldinder(OS):</td>");
			out.write("<td>" + cylOSout + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
				
			String axisODout = ""; //only print the axis value if there is one
			if(bean.getAxisOD() != null){ //default, meaning we don't have one
				axisODout = Integer.toString(bean.getAxisOD());
			}
			out.write("<tr>");
			out.write("<td>Axis(OD):</td>");
			out.write("<td>" + axisODout + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			String axisOSout = ""; //only print the axis value if there is one
			if(bean.getAxisOS() != null){ //0 is default, meaning we don't have one
				axisOSout = Integer.toString(bean.getAxisOS());
			}
			out.write("<tr>");
			out.write("<td>Axis(OS):</td>");
			out.write("<td>" + axisOSout + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Add(OD):</td>");
			out.write("<td>" + bean.getAddOD() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
			
			out.write("<tr>");
			out.write("<td>Add(OS):</td>");
			out.write("<td>" + bean.getAddOS() + "</td>");
			out.write("<td></td>");
			out.write("</tr>");
				
		out.write("</table><br />"); //end the main table
		%>
	</form>
	
	<table class="fTable" align="center" >
	<tr>
		<th colspan="3">Diagnoses</th>
	</tr>
	<tr  class="subHeader">
		<th>ICD Code</th>
		<th>Description</th>
		<th>URL</th>
	</tr>
	<%
	EditOPDiagnosesAction diagAction = new EditOPDiagnosesAction(prodDAO,oidString);
	if (diagAction.getDiagnoses().size() == 0) { %>
	<tr>
		<td colspan="3" >No Diagnoses for this visit</td>
	</tr>
	<% } else { 
		for(OphthalmologyDiagnosisBean d : diagAction.getDiagnoses()) { String link = d.getURL();%>
		<tr>
			<td ><%= StringEscapeUtils.escapeHtml(d.getICDCode()) %></td>
			<td  style="white-space: nowrap;"><%= StringEscapeUtils.escapeHtml("" + (d.getDescription() )) %></td>
			<td><a href=<%= d.getURL() %>><%= link %></a></td>
		</tr>
	   <%} 
  	   }  %>
</table>
<br /><br />
</div>
<p><br/><a href="/iTrust/auth/hcp/ophthalmologyHome.jsp">Back to Home</a></p>
<%@include file="/footer.jsp" %>