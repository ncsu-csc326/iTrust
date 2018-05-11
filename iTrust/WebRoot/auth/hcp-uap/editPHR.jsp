<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>

<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FamilyMemberBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.risk.RiskChecker"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Personal Health Record";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Health Records" />
<%
PatientDAO patientDAO = new PatientDAO(prodDAO);
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
PersonnelBean personnelb = personnelDAO.getPersonnel(loggedInMID.longValue());
DateFormat df = DateFormat.getDateInstance();

String switchString = "";
if (request.getParameter("switch") != null) {
	switchString = request.getParameter("switch");
}

String relativeString = "";
if (request.getParameter("relative") != null) {
	relativeString = request.getParameter("relative");
}

String patientString = "";
if (request.getParameter("patient") != null) {
	patientString = request.getParameter("patient");
}

String pidString;
long pid = 0;

if (switchString.equals("true")) pidString = "";
else if (!relativeString.equals("")) {

	int relativeIndex = Integer.parseInt(relativeString);
	List<PatientBean> relatives = (List<PatientBean>) session.getAttribute("relatives");
	pid = relatives.get(relativeIndex).getMID();
	pidString = "" + pid;
	session.removeAttribute("relatives");
	session.setAttribute("pid", pidString);
}
else if (!patientString.equals("")) {

	int patientIndex = Integer.parseInt(patientString);
	List<PatientBean> patients = (List<PatientBean>) session.getAttribute("patients");
	pid = patients.get(patientIndex).getMID();
	pidString = "" + pid;
	session.removeAttribute("patients");
	session.setAttribute("pid", pidString);
}
else {
	if (session.getAttribute("pid") == null) {
		pid = 0;
		pidString = "";
	} else {
		pid = (long) Long.parseLong((String) session.getAttribute("pid"));
		pidString = ""+pid;
	}
}

if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("../getPatientID.jsp?forward=hcp-uap/editPHR.jsp");
	
   	return;
}
loggingAction.logEvent(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, loggedInMID.longValue(), pid, "");

//else {
//	session.removeAttribute("pid");
//}


EditPHRAction action = new EditPHRAction(prodDAO,loggedInMID.longValue(), pidString);
pid = action.getPid();
String confirm = "";
if(request.getParameter("addA") != null)
{
	try{
		confirm = action.updateAllergies(pid,request.getParameter("description"));
		loggingAction.logEvent(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, loggedInMID.longValue(), pid, "");
	} catch(Exception e)
	{
		confirm = e.getMessage();
	}
}

PatientBean patient = action.getPatient();
List<HealthRecord> records = action.getAllHealthRecords();
HealthRecord mostRecent = records.size() > 0 ? records.get(0) : null;
List<OfficeVisitBean> officeVisits = action.getAllOfficeVisits();
List<FamilyMemberBean> family = action.getFamily(); 
%>


<%@page import="edu.ncsu.csc.itrust.exception.NoHealthRecordsException"%><script type="text/javascript">
function showRisks(){
	document.getElementById("risks").style.display="inline";
	document.getElementById("riskButton").style.display="none";
}
</script>

<% if (!"".equals(confirm)) {%>
<span class="iTrustError"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span><br />
<% } %>

<br />
<div align=center>
	<div style="margin-right: 10px;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="2">Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
			</tr>
			<tr>
				<td  class="subHeaderVertical">Address:</td>
				<td > <%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress1())) %><br />
				     <%="".equals(patient.getStreetAddress2()) ? "" : patient.getStreetAddress2() + "<br />"%>
				     <%= StringEscapeUtils.escapeHtml("" + (patient.getStreetAddress3())) %><br />									  
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getPhone())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical" >Email:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getEmail())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Directions to Home:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getDirectionsToHome())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Religion:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getReligion())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Language:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getLanguage())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Spiritual Practices:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getSpiritualPractices())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Alternate Name:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getAlternateName())) %></td>
			</tr>
			<tr>
				<th colspan="2">Insurance Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical" >Provider Name:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcName())) %></td>
			</tr>
			<tr>
				<td  class="subHeaderVertical">Address:</td>
				<td > <%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress1())) %><br />
					<%="".equals(patient.getIcAddress2()) ? "" : patient.getIcAddress2() + "<br />"%>
					<%= StringEscapeUtils.escapeHtml("" + (patient.getIcAddress3())) %><br />							
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getIcPhone())) %></td>
			</tr>
		</table>
		<br />
		<a href="editPatient.jsp" style="text-decoration: none;">
			<input type=button value="Edit" onClick="location='editPatient.jsp';">
		</a>
	</div>
	<div style="margin-right: 10px;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="2">Basic Health Records</th>
			</tr>
			<% if (null == mostRecent) { %>
			<tr><td colspan=2>No basic health records are on file for this patient</td></tr>
			<% } else {%>
			<tr>
				<td class="subHeaderVertical">Height:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getHeight())) %>in.</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Weight:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getWeight())) %>lbs.</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Smoker?:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getSmokingStatus()) + " - " + (mostRecent.getSmokingStatusDesc())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Blood Pressure:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getBloodPressureN())) %>/<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getBloodPressureD())) %>mmHg</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Cholesterol:</td>
				<td >
				<table>
					<tr>
						<td style="text-align: right">HDL:</td>
						<td><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolHDL())) %> mg/dL</td>
					</tr>
					<tr>
						<td style="text-align: right">LDL:</td>
						<td><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolLDL())) %> mg/dL</td>
					</tr>
					<tr>
						<td style="text-align: right">Tri:</td>
						<td><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolTri())) %> mg/dL</td>
					</tr>
					<tr>
						<td style="text-align: right">Total:</td>
						<td>
							<span id="totalSpan" style="font-weight: bold;"><%= StringEscapeUtils.escapeHtml("" + (mostRecent.getTotalCholesterol())) %> mg/dL</span>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<% } //closing for "there is a most recent record for this patient" %>
		</table>
		<br />
		<a href="viewBasicHealth.jsp" style="text-decoration: none;">
			<input type="button" value="View/Edit History" onClick="location='viewBasicHealth.jsp';">
		</a>
	</div>
	<div >
		<table class="fTable" align="center">
			<tr>
				<th>Office Visits</th>
			</tr>
			<tr>
				<td align="center">
					<div style="overflow:auto; height:200px; width:200px;">
					<% for (OfficeVisitBean ov : officeVisits) { %>
						<a href="/iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitID())) %>"><%= StringEscapeUtils.escapeHtml("" + (df.format(ov.getVisitDate()))) %></a><br />
					<% } %>
					</div>
				</td>
			</tr>
		</table>
		<br />
		<a href="getPrescriptionReport.jsp" style="text-decoration: none;" >
			<input type=button value="Get Prescription Report" onClick="location='getPrescriptionReport.jsp';">
		</a>				
	</div>
</div>
<br />

<table class="fTable" align="center" >
	<tr>
		<th colspan="9">Family Medical History</th>
	</tr>
	<tr class="subHeader">
		<td> Name </td>
		<td> Relation </td>
		<td> High Blood Pressure </td>
		<td> High Cholesterol </td>
		<td> Diabetes </td>
		<td> Cancer </td>
		<td> Heart Disease </td>
		<td> Smoker </td>
		<td> Cause of Death </td>
	</tr>
<% if (0 == family.size()) {%>
	<tr>
		<td colspan="9" style="text-align: center;">No Relations on	record</td>
	</tr>
<%	} 
	else {
		List<PatientBean> patientRelatives = new ArrayList<PatientBean>();
		int index = 0;
		for(FamilyMemberBean member : family) {
			patientRelatives.add(patientDAO.getPatient(member.getMid())); 
%>
	<tr>					
		<td class = "valueCell" ><a href="editPHR.jsp?relative=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (member.getFullName())) %></a></td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (member.getRelation())) %></td>
		<td  align=center><%= StringEscapeUtils.escapeHtml("" + (action.doesFamilyMemberHaveHighBP(member) ? "x" : "")) %></td>
		<td  align=center><%= StringEscapeUtils.escapeHtml("" + (action.doesFamilyMemberHaveHighCholesterol(member) ? "x" : "")) %></td>
		<td  align=center><%= StringEscapeUtils.escapeHtml("" + (action.doesFamilyMemberHaveDiabetes(member) ? "x" : "")) %></td>
		<td  align=center><%= StringEscapeUtils.escapeHtml("" + (action.doesFamilyMemberHaveCancer(member) ? "x" : "")) %></td>
		<td  align=center><%= StringEscapeUtils.escapeHtml("" + (action.doesFamilyMemberHaveHeartDisease(member) ? "x" : "")) %></td>
		<td  align=center><%= StringEscapeUtils.escapeHtml("" + (action.isFamilyMemberSmoker(member) ? "x" : "")) %></td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (action.getFamilyMemberCOD(member))) %></td>
	</tr>
<%			index++;
		}
		session.setAttribute("relatives", patientRelatives);
	} %>
</table>
<br />
<div align=center>
	<div style="margin-right: 10px; display: inline-table;">
		<% List<AllergyBean> allergies = action.getAllergies(); %>
		<table class="fTable" align="center" >
			<tr>
				<th colspan="2">Allergies</th>
			</tr>
			<tr class="subHeader">
				<td>Allergy Description</td>
				<td>First Found</td>
			</tr>
	
			<% if (0 == allergies.size()) { %>
			<tr>
				<td  colspan="2" style="text-align: center;">No Allergies on record</td>
			</tr>
			<% } else {
				for (AllergyBean allergy : allergies) {%>
			<tr>
				<td  style="text-align: center;"><%= StringEscapeUtils.escapeHtml("" + (allergy.getDescription())) %></td>
				<td  style="text-align: center;"><%= StringEscapeUtils.escapeHtml("" + (df.format(allergy.getFirstFound()))) %></td>
			</tr>			
			<% } } %>
			<form name="AddAllergy" action="editPHR.jsp" method="post" id="addAllergyForm">
			<tr >
				<th colspan="2" style="text-align: center;">New</th>
			</tr>
			<tr>
				<td colspan="2">
					<input name="description" id="description" type="text" size="10" />
					<!-- <select name="description" id="description" style="font-size:10px;" >
                	<option value=""> -- Please Select a Medication -- </option>-->
                	<%//for(MedicationBean med : prodDAO.getNDCodesDAO().getAllNDCodes()){%>
                    	<!-- <option value="<%//=med.getNDCode()%>">-->
                        	<%//= StringEscapeUtils.escapeHtml("" + (med.getNDCode())) %> <!-- - --> <%//= StringEscapeUtils.escapeHtml("" + (med.getDescription())) %>
                    	<!-- </option>-->
               		 <%//}%>
            		<!-- </select>-->
					<input type="submit" name="addA" value="Add Allergy">
				</td>
			</tr>
			</form>
		</table>
	</div>
	<div style="margin-right: 10px; display: inline-table;">
		<table class="fTable" align=center>
			<tr>
				<th colspan=2>Chronic Disease Risk Factors</th>
			</tr>
			<tr>
				<td align="center">
					<div id="risks" style="display: none;">
						Patient is at risk for the following:<br />
						<% try{
								List<RiskChecker> diseases = action.getDiseasesAtRisk();
								for (RiskChecker disease : diseases) { %>
						  			<span ><%= StringEscapeUtils.escapeHtml("" + (disease.getName())) %></span><br />
							<% }
						   } catch (NoHealthRecordsException e) {
							   %><%=StringEscapeUtils.escapeHtml(e.getMessage())%><%
						   } %>
						<a style="font-size: 80%" href="chronicDiseaseRisks.jsp">More Information</a>
					</div>
				</td>
			</tr>
		</table>
		<br />
		<div id="riskButton">
			<input type=button value="Show Chronic Diseases Risk Factors" onclick="javascript:showRisks();">
		</div>
	</div>
	<div style="display: inline-table;">
		<table class="fTable" align=center>
			<tr>
				<th colspan="3">Immunizations</th>
			</tr>
			<tr class="subHeader">
	  			<td>CPT Code</td>
 				<td>Description</td>
  				<td>Date Received</td>
 			</tr>
<%	
		for (OfficeVisitBean ov: officeVisits) {
			
			for (ProcedureBean proc:  action.getProcedures(ov.getVisitID())) {
				if (null != proc.getAttribute() && proc.getAttribute().equals("immunization")) { 
%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode() )) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (proc.getDescription() )) %></td>
				<td ><a href="editOfficeVisit.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitID())) %>"><%= StringEscapeUtils.escapeHtml("" + (proc.getDate() )) %></a></td>	
			</tr>
<%
				}
			}
		}
%>
		</table>
	</div>
</div>

<br /><br /><br />
<%@include file="/footer.jsp" %>
