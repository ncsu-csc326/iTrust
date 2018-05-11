<%@page import="edu.ncsu.csc.itrust.dao.mysql.ObstetricsRecordDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.FlagsDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PreExistingConditionsDAO"%>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%> 
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FlagsBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Obstetrics Record Pre-Existing Conditions";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/obstetricsPreExistingConditions.jsp");
		return;
	}
	
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
	
	ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
	
	boolean ReadOnly = false;
	
	//now double-check this bean's status AND the HCP's specialty to see if should redirect back to view
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
		ReadOnly = true;
	}
	
	PreExistingConditionsDAO conditions = new PreExistingConditionsDAO(prodDAO);
	
	//don't run unless the form was actually submitted
	if ("true".equals(request.getParameter("formIsFilled"))) {
		
		FlagsDAO fDao = new FlagsDAO(prodDAO);
		FlagsBean p = new FlagsBean();
		p.setFlagValue(FlagValue.PreExistingConditions);
		p.setMid(patient.getMID());
		
		List<ObstetricsRecordBean> initialBeans;
		ObstetricsRecordDAO obstetricsDAO = new ObstetricsRecordDAO(prodDAO);
		initialBeans = obstetricsDAO.getObstetricsRecordsByMIDPregStatus(patient.getMID(), PregnancyStatus.Initial.toString());
		if (initialBeans == null || initialBeans.isEmpty()) {
			//Can't set flag for non-existant preg id
		}
		else {
			p.setPregId(initialBeans.get(0).getPregId());
		}

		fDao.getFlag(p);
		p.setFlagged(true);
		fDao.setFlag(p);
		
		//add the record
		String condition = request.getParameter("condition");
		String resp = conditions.putConditionByMID(patient.getMID(), condition);
		out.write("<p class=\"iTrustError\">" + resp + "</p>");
	}
%>
<div align=center>
	<table class="fTable" align="center">
		<tr><th colspan="2">Pregnancy relevant pre-existing conditions for <%=patient.getFullName()%></th></tr>
<% if (!ReadOnly) { %>
		<tr>
			<form action="/iTrust/auth/hcp/obstetricsPreExistingConditions.jsp" method="POST" id="existingConditions">
			<input type="hidden" name="formIsFilled" value="true" />
			<td><input type="text" id="condition" name="condition" /></td>
			<td><input type="submit" id="submit" value="Submit" /></td>
			</form>
		</tr>
<% }
	for (String str : conditions.getConditionsByMID(patient.getMID())) {
%>
	<tr>
		<td colspan="2"><%=str%></td>
	</tr>
<% } %>

	</table>
	<br />
</div>
<p><br/><a href="/iTrust/auth/hcp/obstetricsHome.jsp">Back to Home</a></p>

<%@include file="/footer.jsp" %>