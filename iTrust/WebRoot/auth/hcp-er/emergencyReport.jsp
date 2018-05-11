<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EmergencyReportAction"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ReportRequestDAO" %>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.Email" %>
<%@page import="edu.ncsu.csc.itrust.EmailUtil" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Date" %>
<%@page import="java.lang.NumberFormatException" %>
<%@page import="java.lang.Integer" %>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean" %>
<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - ER Report";

String pidString = null;
boolean print = (null != request.getParameter("print") && request.getParameter("print").equals("true"));

if (!print) {
	/* Require a Patient ID first */
	pidString = (String)session.getAttribute("pid");
	if (null == pidString || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-er/emergencyReport.jsp");
		return;
	}
	else {
		session.setAttribute("printPid", pidString);
		
	}
}
else {
	pidString = (String)session.getAttribute("printPid");
	if (null == pidString || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-er/emergencyReport.jsp");
		return;
	}
	session.removeAttribute("printPid");
}

/* If the patient id doesn't check out, then kick 'em out to the exception handler */
EmergencyReportAction action = new EmergencyReportAction(prodDAO, loggedInMID.longValue(), pidString);
	
/* Now take care of updating information */
%>

<% if (print) { %>
<%@include file="/print_header.jsp" %>
<%}
else {%>
<%@include file="/header.jsp" %>
<%}%>

<% 

if (!print) {
	
	// Generate a report to the hcp's of this patient.
	//  TODO: possibly change this to use messages and a different display format on
	//		hcp's home screen.
	ReportRequestDAO makeReport = new ReportRequestDAO(prodDAO);
	PatientDAO patients = new PatientDAO(prodDAO);
	PatientBean patient = patients.getPatient(action.getPid());
	PersonnelDAO hcps = new PersonnelDAO(prodDAO);
	List<PersonnelBean> peeps = hcps.getAllPersonnel();
	List<String> emails = new ArrayList<String>();
	
	for (PersonnelBean p : peeps) {
		if (patients.checkDeclaredHCP(patient.getMID(), p.getMID())) {
			makeReport.addReportRequest(p.getMID(), patient.getMID(), new Date());
			emails.add(p.getEmail());
		}
	}
	
	Email myEmail = new Email();
	myEmail.setBody("Patient " + patient.getFullName() + "'s medical record was viewed by an ER");
	myEmail.setSubject("Patient records viewed");
	myEmail.setToList(emails);
	myEmail.setFrom("noreply@itrust.com");
	
	EmailUtil emailer = new EmailUtil(prodDAO);
	emailer.sendEmail(myEmail);
	loggingAction.logEvent(TransactionType.EMERGENCY_REPORT_VIEW, loggedInMID, Long.parseLong(pidString), "");
	
%>
<form action="emergencyReport.jsp" method="post">
	<input type="hidden" name="print" id="print" value="true" />
	<input type="submit" value="Print" />
</form>
<%} %>

<ul>
<li>Name: <%= StringEscapeUtils.escapeHtml("" + (action.getPatientName())) %></li>
<li>Age: <%= StringEscapeUtils.escapeHtml("" + (action.getPatientAge())) %></li>
<li>Gender: <%= StringEscapeUtils.escapeHtml("" + (action.getPatientGender())) %> </li>
<li>Emergency Contact: <%= StringEscapeUtils.escapeHtml("" + (action.getPatientEmergencyContact() )) %></li>
<li>Allergies:
<%
if (0 == action.getAllergies().size()) {
%><strong>No allergies on record</strong><%	
}
else {
	%><ul><%
	for ( AllergyBean bean: action.getAllergies()) {
		String beanDesc = "";
		MedicationBean NDCbean = prodDAO.getNDCodesDAO().getNDCode(bean.getDescription());
		if (NDCbean == null) beanDesc = bean.getDescription();
		else beanDesc = NDCbean.getDescription();
		out.print("<li>" + StringEscapeUtils.escapeHtml("" + (beanDesc)) + " " + StringEscapeUtils.escapeHtml("" + (bean.getFirstFoundStr())) + "</li>");
	} 
	%></ul><%
}
%>
</li>
<li>Blood Type: <%= StringEscapeUtils.escapeHtml("" + (action.getBloodType())) %> </li>

<li>Diagnoses: 
<%
if (0 == action.getWarningDiagnoses().size()) {
%><strong>No critical diagnoses on record</strong><%	
}
else {
	%><ul><%
	for(DiagnosisBean bean : action.getWarningDiagnoses()) {
		out.print("<li>" + StringEscapeUtils.escapeHtml("" + (bean.getICDCode())) + " " + StringEscapeUtils.escapeHtml("" + (bean.getDescription())) + "</li>");
	} 
	%></ul><%
}
%>
</li>

<li>Prescriptions: 
<%
if (0 == action.getCurrentPrescriptions().size()) {
%><strong>No current prescriptions on record</strong><%	
}
else {
	%><ul><%
	for(PrescriptionBean bean : action.getCurrentPrescriptions()) {
		out.print("<li>" + StringEscapeUtils.escapeHtml("" + (bean.getMedication().getNDCode())) + " " + StringEscapeUtils.escapeHtml("" + (bean.getMedication().getDescription())) + "</li>");
	} 
	%></ul><%
}
%>
</li>

<li>Immunizations:
<% if (0 == action.getImmunizations().size()) { %>
<strong>no immunizations on record</strong>
<%	
}
else {
%>
<ul>
<%
	for (ProcedureBean bean : action.getImmunizations()) {
		if (null != bean.getAttribute() && bean.getAttribute().equals("immunization"))
			out.print("<li>" + StringEscapeUtils.escapeHtml("" + (bean.getDescription())) + " (" + StringEscapeUtils.escapeHtml("" + (bean.getCPTCode())) +")" + "</li>");
	} 
%>
</ul>
<%
}
%>
</li>

<%@include file="/footer.jsp" %>
