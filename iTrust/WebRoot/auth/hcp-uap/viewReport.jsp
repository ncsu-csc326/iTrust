<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ReportRequestBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewReportAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.FamilyMemberBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MedicationBean"%>
<%@page import="java.text.NumberFormat" %>
<%@page import="java.text.DecimalFormat" %>
<%@page import="edu.ncsu.csc.itrust.Localization" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Comprehensive Patient Report";
%>

<%@include file="/header.jsp" %>

<%
//long loggedInMID = request.getUserPrincipal()==null ? 0L : Long.valueOf(request.getUserPrincipal().getName());

PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
ViewMyReportRequestsAction rAction = new ViewMyReportRequestsAction(prodDAO, loggedInMID.longValue());
int patientIndex = Integer.parseInt(request.getParameter("patient"));
List<PatientBean> patients = (List<PatientBean>) session.getAttribute("patients");
//session.removeAttribute("patients");
String pidString = "" + patients.get(patientIndex).getMID();
String rrString = request.getParameter("requestID");
long patientMID;
int requestID;
ReportRequestBean reportRequest;
try {
	if (pidString == null || pidString.equals("")) throw new Exception("Error: patientMID is null");
	if (rrString == null || rrString.equals("")) throw new Exception("Error: requestID is null");
	patientMID = Long.parseLong(pidString);
	requestID = Integer.parseInt(rrString);
	reportRequest = rAction.getReportRequest(requestID);
	rAction.setViewed(requestID);
	reportRequest = rAction.getReportRequest(requestID);

ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, patientMID);
ViewReportAction viewAction = new ViewReportAction(prodDAO, loggedInMID.longValue(), patientMID);
PatientBean patient = action.getPatient();
List<AllergyBean> allergies = action.getAllergies();
List<PatientBean> represented = action.getRepresented();
List<PatientBean> representing = action.getRepresenting();
List<FamilyMemberBean> family = action.getFamily(); 
List<PersonnelBean> hcps = viewAction.getDeclaredHCPs(patientMID);

%><h3>Comprehensive Patient Report for <%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></h3>

<div align=center>
<table>
	<tr><td valign=top>
	<table class="fTable">
		<tr>
			<th colspan="2">Patient Information</th>
		</tr>
		<tr>
			<td class="subHeaderVertical">Name:</td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Address:</td>
			<td >
			<%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress1())) %><br />
			<%= "".equals(patient.getStreetAddress2()) ? "" : patient.getStreetAddress2() + "<br />"%>
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
	</table>
	</td>
	<td width="15px">&nbsp;</td>
	<td>
	<table class="fTable">
		<tr>
			<th colspan="2">Insurance Information</th>
		</tr>
		<tr>
			<td class="subHeaderVertical">Name (ID):</td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcName())) %> (<%= StringEscapeUtils.escapeHtml("" + (patient.getIcID())) %>)</td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Address:</td>
			<td >
			<%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress1())) %><br />
			<%="".equals(patient.getIcAddress2()) ? "" : patient.getIcAddress2() + "<br />"%>
			<%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress3())) %><br />							
			</td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Phone:</td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcPhone())) %></td>
		</tr>
	</table>
	</td></tr>
</table>
<br />
<br />
<br />
<table class="fTable">
		<tr>
			<th colspan="2">Designated HCPs</th>
		</tr>
			<tr class="subHeader">
				<td>HCP Name</td>
			</tr>
	
			<%if(hcps.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No designated HCPs on record</td>
			</tr>
			<%} else {
				for(PersonnelBean hcp : hcps){%>
			<tr>
				<td style="text-align: center;"><%= StringEscapeUtils.escapeHtml("" + (hcp.getFullName())) %></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="2">Allergies</th>
		</tr>
			<tr class="subHeader">
				<td>Allergy Description</td>
				<td>First Found</td>
			</tr>
	
			<%if(allergies.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No Allergies on record</td>
			</tr>
			<%} else {
				for(AllergyBean allergy : allergies){%>
			<tr>
				<td style="text-align: center;"><%= StringEscapeUtils.escapeHtml("" + (allergy.getDescription())) %></td>
				<td style="text-align: center;"><%= StringEscapeUtils.escapeHtml("" + (allergy.getFirstFoundStr())) %></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<br />
<br />
<br />
<table class="fTable">
		<tr>
			<th colspan="2">Known Relatives</th>
		</tr>
			<tr class="subHeader">
				<th>Name</th>
				<th>Relation</th>
			</tr>
			<%
			  if(family.size()==0){%>
				<tr>
					<td colspan="3" style="text-align: center;">No Relations on	record</td>
				</tr>
			  <%} else {
				  for(FamilyMemberBean member : family) {%>
			  	<tr>
					<td><%= StringEscapeUtils.escapeHtml("" + (member.getFullName())) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + (member.getRelation())) %></td>
				</tr>
			  <%  }
				}%>
</table>
<br />
<table class="fTable">
		<tr>
			<th>Patients <%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName())) %> is representing</th>
		</tr>
			<tr class="subHeader">
				<th>Patient</th>
			</tr>
			<%if(represented.size() ==0){ %>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName())) %> is not representing any patients</td>
			</tr>
			<%} else { 
				for(PatientBean p : represented){%>
				<tr>
					<td align=center>
						<%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %>
					</td>
				</tr>
			 <% }
			  }%>
</table>
<br />
<table class="fTable">
		<tr>
			<th >Patients Representing <%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName())) %></th>
		</tr>
			<tr class="subHeader">
				<th>Patient</th>
			</tr>
			<%if(representing.size() ==0){ %>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml("" + (patient.getFirstName())) %> is not represented by any patients</td>
			</tr>
			<%} else { 
				for(PatientBean p : representing){%>
				<tr>
					<td align=center>
						<%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %>
					</td>
				</tr>
			 <% }
			  }%>
</table>
<br />
<br />

<%} catch (Exception ex) {
	%><%=ex.getClass()+", " %><%=ex.getCause()+", " %><%=StringEscapeUtils.escapeHtml(ex.getMessage()) %><%
}
%>

<%@include file="/footer.jsp" %>
