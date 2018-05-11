<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.AddPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Patient";
%>

<%@include file="/header.jsp" %>

<%
boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
if (formIsFilled) {
	//This page is not actually a "page", it just adds a user and forwards.
	PatientBean p = new BeanBuilder<PatientBean>().build(request.getParameterMap(), new PatientBean());
	try{
		boolean isDependent = false;
		long representativeId = -1L;
		if(request.getParameter("isDependent") != null && request.getParameter("isDependent").equals("on")){
			isDependent = true;
		}
		
		if(request.getParameter("repId") != "" && isDependent){
			representativeId = Long.valueOf(request.getParameter("repId"));
		}else if(isDependent && request.getParameter("repId") == ""){
			throw new FormValidationException("Representative MID must be filled if the patient is marked as a dependent.");
		}
		long newMID = 1L; 
		if(isDependent){
			newMID = new AddPatientAction(prodDAO, loggedInMID.longValue()).addDependentPatient(p, representativeId);
		}else{
			newMID = new AddPatientAction(prodDAO, loggedInMID.longValue()).addPatient(p);
		}
		session.setAttribute("pid", Long.toString(newMID));
		String fullname;
		String password;
		password = p.getPassword();
		fullname = p.getFullName();
		if (isDependent) {
			loggingAction.logEvent(TransactionType.HCP_CREATED_DEPENDENT_PATIENT, loggedInMID.longValue(), newMID, "");
		} else {
			loggingAction.logEvent(TransactionType.PATIENT_CREATE, loggedInMID.longValue(), newMID, "");			
		}
%>
	<div align=center>
		<span class="iTrustMessage">New patient <%= StringEscapeUtils.escapeHtml("" + (fullname)) %> successfully added!</span>
		<br />
		<table class="fTable">
			<tr>
				<th colspan=2>New Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">MID:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + (newMID)) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Temporary Password:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + (password)) %></td>
			</tr>
		</table>
		<br />Please get this information to <b><%= StringEscapeUtils.escapeHtml("" + (fullname)) %></b>! 
		<p>
			<a href = "/iTrust/auth/hcp-uap/editPatient.jsp">Continue to patient information.</a>
		</p>
	</div>
<%
	} catch(FormValidationException e){
%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
<%
	}
}
%>

<div align=center>
<form action="addPatient.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<div style="width: 50%; text-align:left;">Please enter in the name of the new
patient, with a valid email address. If the user does not have an email
address, use the hospital's email address, [insert pre-defined email],
to recover the password.</div>
<br />
<br />
<table class="fTable">
	<tr>
		<th colspan=2>Patient Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">First name:</td>
		<td><input type="text" name="firstName"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Last Name:</td>
		<td><input type="text" name="lastName">
	</tr>
	<tr>
		<td class="subHeaderVertical">Email:</td>
		<td><input type="text" name="email"></td>
	</tr>
</table>
<br />
<input type="checkbox" id="isDependent" name="isDependent"><i>Make this patient a dependent</i><br>
<i>Representative ID</i><input type="text" id="repId" name="repId"><br>
<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add patient">
</form>
<br />
</div>
<%@include file="/footer.jsp" %>
