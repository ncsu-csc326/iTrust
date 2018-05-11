<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewHealthRecordsHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Basic Health Record";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Basic Health History" />
<%
// Require a Patient ID first
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewBasicHealth.jsp");
   	return;
}

//Create ViewHealthRecordsHistoryAction object to interact with patient's historical health metric history
ViewHealthRecordsHistoryAction historyAction = new ViewHealthRecordsHistoryAction(prodDAO,pidString,loggedInMID.longValue());


//Get the patient's name
String patientName = historyAction.getPatientName();

//Get all of the patient's health records
List<HealthRecord> records = historyAction.getAllPatientHealthRecords();
//Save the list of health records in the session
session.setAttribute("healthRecordsList", records);

//Tripper boolean values used in table creation
boolean babyTripper = false;
boolean youthTripper = false;
boolean adultTripper = false;

//Index used for viewing percentiles information
int index = 0;
%>

<br />
<div align=center>
	<table id="HealthRecordsTable" align="center" class="fTable">

	<%
	
	if(records.isEmpty()){
		%>
			<p style="font-size:20px"><i>No health records available</i></p>
		<%
	}
	
	for(HealthRecord hr : records){
		int patAgeOV = historyAction.calculateAgeInMonthsAtOfficeVisit(hr.getOfficeVisitID());
		
		if(patAgeOV < 36){
			
			if(babyTripper == false && !records.isEmpty()){
				%>
				<tr>
				<th colspan="8" style="text-align: center;"><%= patientName %>'s Basic Infant Health History</th>
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
			if(babyTripper == true && !records.isEmpty()){
				%>
				<!-- 
				Get the height, weight, bmi, head circumference, household smoking status, date recorded, 
				and recorded by personnel for each health record
				-->
				<tr>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getVisitDateStr())) %></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Height&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewHeightPercentile<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</a></td>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=Weight&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewWeightPercentile<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</a></td>
					<% if(hr.getBodyMassIndex() != -1) { %>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=BMI&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewBmiPercentile<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (hr.getBodyMassIndex())) %></a></td>
					<% } else { %>
					<td align=center>N/A</td>
					<% } %>
					<td align=center><a href="/iTrust/auth/hcp-patient/viewPercentiles.jsp?dataType=HeadCirc&healthRecord=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" id="viewHeadCircPercentile<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (hr.getHeadCircumference())) %>in</a></td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHouseholdSmokingStatusDesc())) %></td>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
					<%
					//Get the hcp who editted the health record and print his/her name
					PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
					%>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
				</tr>
				<%
			}

		//If the patient is less than 144 months (12 years) old
		} else if (patAgeOV < 144) {
			
			if(youthTripper == false && !records.isEmpty()){
				%>
				<!-- 
				Create a health history table with height, weight, bmi, blood pressure, household smoking status, 
				last recorded date, and recorded by personnel fields.
				 -->
				<tr>
					<th colspan="8" style="text-align: center;"><%= patientName %>'s Basic Youth Health History</th>
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
			if(youthTripper == true && !records.isEmpty()){
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
					PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
					%>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
				</tr>
				<%
			}
		
		//If patient is 12 years or older
		} else {	
			
			if(adultTripper == false && !records.isEmpty()){
				%>
				<!-- 
				Create a health history table with height, weight, bmi, blood pressure, patient smoking status, household smoking status, 
				cholesterol, last recorded date, and recorded by personnel fields.
				 -->
				<tr>
					<th colspan="14" style="text-align: center;"><%= patientName %>'s Basic Adult Health History</th>
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
			if(adultTripper == true && !records.isEmpty()){
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
					PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
					%>
					<td align=center><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
				</tr>
				<%
			}
			
		}
		
		//Increment index for percentiles
		index++;
	}
	%>
	</table>
	<br />
</div>
<br />

<br />
<br />
<br />

<%@include file="/footer.jsp" %>
