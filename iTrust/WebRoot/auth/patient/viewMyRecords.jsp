<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FamilyMemberBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewHealthRecordsHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>


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
	
	List<HealthRecord> records = action.getAllHealthRecords();
	List<OfficeVisitBean> officeVisits = action.getAllOfficeVisits();
	List<FamilyMemberBean> family = action.getFamilyHistory();
	List<AllergyBean> allergies = action.getAllergies();
	List<PatientBean> represented = action.getRepresented();
	
	loggingAction.logEvent(TransactionType.MEDICAL_RECORD_VIEW, originalLoggedInMID, patient.getMID(), "");
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
<%
	for (OfficeVisitBean ov : officeVisits) {
%>
			<tr>
				<td >
					<a href="viewOfficeVisit.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitID())) %><%=isRepresenting ? "&repMID=" + loggedInMID.longValue() : "" %>"><%= StringEscapeUtils.escapeHtml("" + (df.format(ov.getVisitDate()))) %></a></td>
<%
		if (action.isSurveyCompleted(ov.getVisitID())) {
%>
				<td>&nbsp;</td>
<%
		} else {
%>
				<td >
					<a href="survey.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitID())) %>&ovDate=<%= StringEscapeUtils.escapeHtml("" + (df.format(ov.getVisitDate()))) %>">
						Complete Visit Survey
					</a>
				</td>
<%
		}
	}
%>
			</tr>
			<tr>
				<td colspan=2 align=center>
					<a href="viewPrescriptionRecords.jsp?<%= StringEscapeUtils.escapeHtml("" + (isRepresenting ? "&rep=" + loggedInMID.longValue() : "" )) %>">
						Get Prescriptions
					</a>
				</td>
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
		<td  align=center><%=action.doesFamilyMemberHaveHighBP(member) ? "x"
							: ""%></td>
		<td  align=center><%=action
									.doesFamilyMemberHaveHighCholesterol(member) ? "x"
							: ""%></td>
		<td  align=center><%=action
									.doesFamilyMemberHaveDiabetes(member) ? "x"
							: ""%></td>
		<td  align=center><%=action.doesFamilyMemberHaveCancer(member) ? "x"
							: ""%></td>
		<td  align=center><%=action
									.doesFamilyMemberHaveHeartDisease(member) ? "x"
							: ""%></td>
		<td  align=center><%=action.isFamilyMemberSmoker(member) ? "x"
					: ""%></td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (action.getFamilyMemberCOD(member))) %></td>
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

//Create ViewHealthRecordsHistoryAction object to interact with patient's historical health metric history
ViewHealthRecordsHistoryAction historyAction = new ViewHealthRecordsHistoryAction(prodDAO,pidString,loggedInMID.longValue());

//Get the patient's mid number
//long pid = historyAction.getPatientID();
//Get the patient's name
String patName = historyAction.getPatientName();

//Get all of the patient's health records
List<HealthRecord> historyRecords = historyAction.getAllPatientHealthRecords();
//Save the list of health records in the session
session.setAttribute("healthRecordsList", historyRecords);

//Tripper boolean values used in table creation
boolean babyTripper = false;
boolean youthTripper = false;
boolean adultTripper = false;

List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
int index = 0;


%>

<br />
	<table id="HealthRecordsTable" align="center" class="fTable">

	<%
	
	if(records.isEmpty()){
		%>
			<p style="font-size:20px"><i>No health records available</i></p>
		<%
	}
	
	for(HealthRecord hr : historyRecords){
		int patAgeOV = historyAction.calculateAgeInMonthsAtOfficeVisit(hr.getOfficeVisitID());
		
		if(patAgeOV < 36){
			
			if(babyTripper == false && !historyRecords.isEmpty()){
				%>
				<tr>
				<th colspan="8" style="text-align: center;"><%= patName %>'s Basic Baby Health History</th>
				</tr>
				<tr class = "subHeader">
					<td>Office Visit Date</td>
					<td>Length<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
					<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
					<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
					<td>Head Circumference</td>
					<td>Household Smoking Status</td>
					<td>Last Recorded</td>
					<td>By Personnel</td>
				</tr>
				<%
				babyTripper = true;
			}
			if(babyTripper == true && !historyRecords.isEmpty()){
				%>
				<!-- 
				Get the height, weight, bmi, head circumference, household smoking status, date recorded, 
				and recorded by personnel for each health record
				-->
				<tr>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getVisitDateStr())) %></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Height&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewHeightPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</a></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Weight&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewWeightPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</a></td>
					<% if(hr.getBodyMassIndex() != -1) { %>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=BMI&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewBmiPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getBodyMassIndex())) %></a></td>
					<% } else { %>
					<td align=center>N/A</td>
					<% } %>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=HeadCirc&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewHeadCircPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getHeadCircumference())) %>in</a></td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHouseholdSmokingStatusDesc())) %></td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
					<%
					//Get the hcp who editted the health record and print his/her name
					PersonnelBean personnel = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
						if (personnel != null) {
							personnelList.add(personnel);
						}
						%>
						<td >
				 		<% if (personnel != null) { %>
				  			<a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">
				     			<%= StringEscapeUtils.escapeHtml("" + (personnel.getFullName())) %>
				  			</a>
				  		<% 
				  		} 
				  		%>
				</tr>
				<%
			}
		//If the patient is less than 144 months (12 years) old
		} else if (patAgeOV < 144) {
			
			if(youthTripper == false && !historyRecords.isEmpty()){
				%>
				<!-- 
				Create a health history table with height, weight, bmi, blood pressure, household smoking status, 
				last recorded date, and recorded by personnel fields.
				 -->
				<tr>
					<th colspan="8" style="text-align: center;"><%= patName %>'s Basic Youth Health History</th>
				</tr>
				<tr class = "subHeader">
					<td>Office Visit Date</td>
					<td>Height<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
					<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
					<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
					<td>Blood Pressure</td>
					<td>Household Smoking Status</td>
					<td>Last Recorded</td>
					<td>By Personnel</td>
				</tr>
				<%
				youthTripper = true;
			}
			if(youthTripper == true && !historyRecords.isEmpty()){
				%>
				<!-- 
				Get the height, weight, bmi, blood pressure, household smoking status, date recorded, 
				and recorded by personnel for each health record
				-->
				<tr>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getVisitDateStr())) %></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Height&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewHeightPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</a></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Weight&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewWeightPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</a></td>
					<% if(hr.getBodyMassIndex() != -1) { %>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=BMI&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewBmiPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getBodyMassIndex())) %></a></td>
					<% } else { %>
					<td align=center>N/A</td>
					<% } %>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressure())) %> mmHg</td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHouseholdSmokingStatusDesc())) %></td>					
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
					<%
					//Get the hcp who editted the health record and print his/her name
					PersonnelBean personnel = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
						if (personnel != null) {
							personnelList.add(personnel);
						}
						%>
						<td >
				 		<% if (personnel != null) { %>
				  			<a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">
				     			<%= StringEscapeUtils.escapeHtml("" + (personnel.getFullName())) %>
				  			</a>
				  		<% 
				  		} 
				  		%>
				</tr>
				<%
			}
		//If patient is 12 years or older
		} else {	
			
			if(adultTripper == false && !historyRecords.isEmpty()){
				%>
				<!-- 
				Create a health history table with height, weight, bmi, blood pressure, patient smoking status, household smoking status, 
				cholesterol, last recorded date, and recorded by personnel fields.
				 -->
				<tr>
					<th colspan="14" style="text-align: center;"><%= patName %>'s Basic Adult Health History</th>
				</tr>
				<tr class = "subHeader">
					<td>Office Visit Date</td>
					<td>Height<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
					<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
					<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
					<td>Weight Status</td>
					<td>Blood Pressure</td>
					<td>Smokes?</td>
					<td>Household Smoking Status</td>
					<td>HDL</td>
					<td>LDL</td>
					<td>Triglycerides</td>
					<td>Total Cholesterol</td>
					<td>Last Recorded</td>
					<td>By Personnel</td>
				</tr>
				<%
				adultTripper = true;
			}
			if(adultTripper == true && !historyRecords.isEmpty()){
				%>
				<!-- 
				Get the height, weight, bmi, blood pressure, patient smoking status, household smoking status, cholesterol, date recorded, 
				and recorded by personnel for each health record
				-->
				<tr>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getVisitDateStr())) %></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Height&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewHeightPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</a></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Weight&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewWeightPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</a></td>
					<% if(hr.getBodyMassIndex() != -1) { %>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=BMI&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewBmiPercentile"><%= StringEscapeUtils.escapeHtml("" + (hr.getBodyMassIndex())) %></a></td>
					<% } else { %>
					<td align=center>N/A</td>
					<% } %>
					<% if(patAgeOV >= 240 && hr.getBodyMassIndex() < 18.5) { //Underweight by BMI %>
						<td align=center>Underweight</td>
					<% } else if(patAgeOV >= 240 && hr.getBodyMassIndex() < 25.0) { //Normal by BMI %>
						<td align=center>Normal</td>
					<% } else if(patAgeOV >= 240 && hr.getBodyMassIndex() < 30.0) { //Overweight by BMI %>
						<td align=center>Overweight</td>
					<% } else if(patAgeOV >= 240) { //Obese by BMI %>
						<td align=center>Obese</td>
					<% } else { //Patient not over 20 - BMI categories cannot accurately be determined %>
						<td align=center>N/A</td>
					<% } %>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressure())) %> mmHg</td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.isSmoker() ? "Y" : "N")) %></td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHouseholdSmokingStatusDesc())) %></td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolHDL())) %> mg/dL</td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolLDL())) %> mg/dL</td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolTri())) %> mg/dL</td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getTotalCholesterol())) %> mg/dL</td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
					<%
					//Get the hcp who editted the health record and print his/her name
					PersonnelBean personnel = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
						if (personnel != null) {
							personnelList.add(personnel);
						}
						%>
						<td >
				 		<% if (personnel != null) { %>
				  			<a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">
				     			<%= StringEscapeUtils.escapeHtml("" + (personnel.getFullName())) %>
				  			</a>
				  		<% 
				  		} 
				  		%>
				</tr>
				<%
			}
			
		}
		
		index++;
		session.setAttribute("personnelList", personnelList);

	}
	%>
	</table>
	<br />

<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="4">Immunizations</th>
	</tr>
	<tr class="subHeader">
  		<td>CPT Code</th>
	 	<td>Description</th>
   		<td>Date Received</th>
   		<td>Adverse Event</th>
  	</tr>
<%
boolean hasNoData = true;
for (OfficeVisitBean ov: officeVisits) {
	List<ProcedureBean> ovProcs = action.getProcedures(ov.getVisitID());
	for (ProcedureBean proc: ovProcs) {
		if (null != proc.getAttribute() && proc.getAttribute().equals("immunization")) {
			hasNoData=false;
%>
	<tr>
		<td><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (proc.getDescription() )) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (proc.getDate() )) %></td>
		<td>
		<%
			Date date = new Date();
			date.setYear(date.getYear()-1);
			if(proc.getDate().after(date)){
		%>
		<a href="reportAdverseEvent.jsp?presID=<%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %>&HCPMID=<%= StringEscapeUtils.escapeHtml("" + (ov.getHcpID() )) %>&code=<%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %>">Report</a>	
	
<%
		}%></td></tr><% }
	}
}
if(hasNoData) {
%>
	<tr>
		<td colspan=4 align=center>
			No Data
		</td>
	</tr>
<%
}
%>
</table>
<br />

<%@include file="/footer.jsp"%>
