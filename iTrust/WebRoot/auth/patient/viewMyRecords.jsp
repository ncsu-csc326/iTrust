<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.FamilyMemberBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>


<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Records";
%>

<%@include file="/header.jsp"%>

<%
session.removeAttribute("personnelList");
Long originalLoggedInMID = loggedInMID;

	String representee = request.getParameter("rep");
	boolean isRepresenting = false;
	if (representee != null && !"".equals(representee)) {
		int representeeIndex = Integer.parseInt(representee);
		List<PatientBean> representees = (List<PatientBean>) session.getAttribute("representees");
		if(representees != null) {
			loggedInMID = new Long("" + representees.get(representeeIndex).getMID());
//			session.removeAttribute("representees");
			isRepresenting = true;
//		loggedInMID = new Long(action.representPatient(representee));
%>
<span >You are currently viewing your representee's records</span><br />
<%
		}
	}
	
	PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());
	DateFormat df = DateFormat.getDateInstance();
	ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, loggedInMID.longValue());

	patient = action.getPatient();
	
	//Get the patient's age in months
	int patientAge = action.getPatientAgeInMonths(Calendar.getInstance());
	
	List<FamilyMemberBean> family = action.getFamilyHistory();
	List<AllergyBean> allergies = action.getAllergies();
	List<PatientBean> represented = action.getRepresented();
	
	action.logViewMedicalRecords(originalLoggedInMID, patient.getMID());
%> 

<%
if (request.getParameter("message") != null) {
%>
	<div class="iTrustMessage" style="font-size: 24px;" align=center>
		<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("message") )) %>
	</div>
<%
}
%>
<br />
<table align=center>
	<tr> <td>
	<div style="float:left; margin-right:5px;">
		<table class="fTable" border=1 align="center">
			<tr>
				<th colspan="2" >Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
			</tr>
			<tr>
				<td  class="subHeaderVertical">Address:</td>
				<td >
					<%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress1())) %><br />
					<%="".equals(patient.getStreetAddress2()) ? ""
						: patient.getStreetAddress2() + "<br />"%>
					<%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress3())) %><br />
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getPhone())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Email:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getEmail())) %></td>
			</tr>
			<tr>
				<th colspan="2">
					Insurance Information
				</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">
					Provider Name:
				</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcName())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Address:</td>
				<td >
					<%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress1())) %><br />
					<%="".equals(patient.getIcAddress2()) ? "" : patient
						.getIcAddress2()
						+ "<br />"%>
					<%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress3())) %><br />
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcPhone())) %></td>
			</tr>
		</table>
	</div>
	<div style="float: left; margin-left: 5px;">
		<table class="fTable" border=1 align="center">
			<tr>
				<th>Office Visits</th>
				<th>Survey</th>
			</tr>
		</table>
	</div>
	</td> </tr>
</table>

<br />
<br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="9">
			Family Medical History
		</th>
	</tr>
	<tr class="subHeader">
		<td>Name</td>
		<td>Relation</td>
		<td>High Blood Pressure</td>
		<td>High Cholesterol</td>
		<td>Diabetes</td>
		<td>Cancer</td>
		<td>Heart Disease</td>
		<td>Smoker</td>
		<td>Cause of Death</td>
	</tr>
	<%
		if (family.size() == 0) {
	%>
	<tr>
		<td colspan="9" style="text-align: center;">
			No Relations on record
		</td>
	</tr>
	<%
		} else {
			for (FamilyMemberBean member : family) {
	%>
	<tr>
		<td ><%= StringEscapeUtils.escapeHtml("" + (member.getFullName())) %></td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (member.getRelation())) %></td>
	</tr>
	<%
			}
		}
	%>
</table>
<br />
<br />
<table align=center>
	<tr> <td>
	<div style="float:left; margin-right:5px;">
		<table class="fTable" align="center" >
			<tr>
				<th colspan="2">Allergies</th>
			</tr>
			<tr class="subHeader">
				<td>Allergy Description</td>
				<td>First Found</td>
			</tr>

<%
	if (allergies.size() == 0) {
%>
			<tr>
				<td colspan="2" >No Allergies on record</td>
			</tr>
<%
	} else {
		for (AllergyBean allergy : allergies) {
%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (allergy.getDescription())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (df.format(allergy.getFirstFound()))) %></td>
			</tr>
<%
		}
	}
%>
		</table>
	</div>
	
	<div style="float:left; margin-left:5px;">
		<table class="fTable">
			<tr>
				<th> Patients <%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName())) %> Represents </th>
			</tr>
			<tr class="subHeader">
				<td>Patient</td>
			</tr>
<%
	if (represented.size() == 0) {
%>
			<tr>
				<td >
					<%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName())) %> is not representing any patients
				</td>
			</tr>
<%
	} else {
		int index = 0;
		for (PatientBean p : represented) {
%>
			<tr>
				<td >
<%
	if(isRepresenting) {
%>
		<%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %>
<%
	} else {
%>
		<a href="viewMyRecords.jsp?rep=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></a>
<%
	}
%>
					
				</td>
			</tr>
<%
		index++;
		}
		if(!isRepresenting) {
			session.setAttribute("representees", represented);
		}
	}
%>
		</table>
	</div>
	</td></tr>
</table>
<br />
<br />

<%

//Require a Patient ID first
String pidString = loggedInMID.toString();


List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
int index = 0;


%>

<br />
	<table id="HealthRecordsTable" align="center" class="fTable">
		<p style="font-size:20px"><i>No health records available</i></p>
	</table>
<br />
<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="4">Immunizations</th>
	</tr>
</table>
<br />

<%@include file="/footer.jsp"%>
