<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="edu.ncsu.csc.itrust.action.EditHealthHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>

<% { %>

<%
//String for displaying update message
String updateMessage = "";
//String for displaying error message
String fieldErrorMsg = "";
		
//Boolean for whether a record has been added
boolean recordAdded = false;

//Get the patient's age in months
Calendar officeVisitDate = Calendar.getInstance();
officeVisitDate.setTime(ovaction.getOfficeVisit().getVisitDate());
int patientAge = ovaction.healthRecord().getPatientAgeInMonths(officeVisitDate);

//If user chooses to remove a health record
if ("removeHealthRecordForm".equals(submittedFormName)) {
	//Get patient's Health History
	EditHealthHistoryAction healthHistory = ovaction.healthRecord();
	
	//Get the health record's office visit id
	String remID = request.getParameter("removeHealthRecordID");
	HealthRecord record = new HealthRecord();
    record.setOfficeVisitID(Long.parseLong(remID));
    //Remove the health record associated with the office visit id
    healthHistory.removeHealthRecord(record);
    
    updateMessage = "Health information successfully updated.";
    recordAdded = false;
}
//If user chooses to submit an health record form
if ("healthRecordForm".equals(submittedFormName)) {
    // Handle submitting the prescriptions form.
    //Get patient's Health History
	EditHealthHistoryAction healthHistory = ovaction.healthRecord();
    //Set the patient's age
    healthHistory.setPatientAge(patientAge);
    
    try {
		//Add a new health record with the information from the health record form
		healthHistory.addHealthRecord(Long.parseLong(pidString), new BeanBuilder<HealthRecordForm>().build(request.getParameterMap(), new HealthRecordForm()), ovaction.getOvID());
        updateMessage = "Health information successfully updated.";
        recordAdded = true;
    } catch (FormValidationException e) {
    	//If the form cannot be validated, update error message
        fieldErrorMsg = e.printHTMLasString();
    }
}

//Print update message if health information has been updated successfully
if (!"".equals(updateMessage)) {
    %>  <div align="center"><span class="iTrustMessage" align="center"><%= updateMessage %></span></div>  <%
}
//Print error message if form could not be validated
if (!"".equals(fieldErrorMsg)) { 
	%> <div style="background-color:yellow;color:black" align="center"><%= fieldErrorMsg %></div> <%
} 
%>

<!-- javascript for handling health record removal -->
<script type="text/javascript">
    function removeHealthRecordID(value) {
        document.getElementById("removeHealthRecordID").value = value;
        document.forms["removeHealthRecordForm"].submit();
    }
</script>

<form></form>

<div align="center">

<form action="editOfficeVisit.jsp#basic-health" method="post" id="removeHealthRecordForm">
	<input type="hidden" name="formName" value="removeHealthRecordForm" />
	<input type="hidden" id="removeHealthRecordID" name="removeHealthRecordID" value="" />
	<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOfficeVisit.jsp#basic-health" method="post" id="healthRecordForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="healthRecordForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
	
<table class="fTable" align="center" id="healthRecordsTable">
<%//If the patient is less than 36 months (3 years) old
if (patientAge < 36) {
%>
	<!-- 
	Create a health history table with length, weight, bmi, head circumference, household smoking status, 
	last recorded date, recorded by personnel, and action fields.
	 -->
	<tr>
    	<th colspan="8"><a href="#" class="topLink">[Top]</a>Basic Health Metrics</th>
    </tr>
	<tr class = "subHeader">
		<td>Length<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
		<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
		<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
		<td>Head Circumference</td>
		<td>Household Smoking Status</td>
		<td>Last Recorded</td>
		<td>By Personnel</td>
		<td style="width: 60px;">Action</td>
	</tr>
	
	<%
	//Get health record associated with the current office visit
	HealthRecord hr = ovaction.healthRecord().getHealthRecordByOfficeVisit(ovaction.getOvID());
			
	//If no health record exists for this office visit
	if (hr == null) { 
		//Initialize a new health record
		hr = new HealthRecord();
		//And print a message indicating no message exists
		%>
		<tr>
    		<td colspan="8" style="text-align: center;">No Health Metrics on record</td>
    	</tr>
    	<%
    } else {
		//If there is a health record associated with this office visit,
		//get bmi as a string (weight in pounds * 703)/(height in inches * height in inches)
		DecimalFormat df = new DecimalFormat("00.00");
		String bmiString = "Invalid length value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = df.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
		}
		%>
		<!-- 
		Get the height, weight, bmi, head circumference, household smoking status, date recorded, recorded by personnel,
		and remove link for the health record.
		-->
		<tr>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bmiString)) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHeadCircumference())) %>in</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHouseholdSmokingStatusDesc())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
			<%
			//Get the hcp who editted the health record and print his/her name
			PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
			%>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
			<td align=center>
                <a href="javascript:removeHealthRecordID('<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>');">Remove</a>
            </td>
		</tr>
	<%	
		//Log that the hcp has viewed the patient health records
		ovaction.logOfficeVisitEvent(TransactionType.VIEW_BASIC_HEALTH_METRICS);
		//Change the recordAdded boolean to indicate that a health record already exists for this office visit
		recordAdded = true;
	}
	%>
	<!-- Display an add new record form with length, weight, head circumference, and household smoking status fields -->
    <tr>
        <th colspan="8" style="text-align: center;">Add New</th>
    </tr>
	<tr>
		<td align="center" colspan="2">Length:  
			<input name="height"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> in.
		</td>
		<td align="center" colspan="2">Weight: 
			<input name="weight"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> lbs.
		</td>
		<td align="center" colspan="4">Head Circumference:  
			<input name="headCircumference"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getHeadCircumference())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> in.
		</td>
	</tr>
	<tr>
		<td align="center" colspan="8">Household Smoking Status: 
			<select name="householdSmokingStatus" id="householdSmokingStatus" style="font-size:10px;" <%= disableSubformsString %>>
           		<option value=""> -- Please Select a Household Smoking Status -- </option>
                <option value="1">1 - non-smoking household</option>
               	<option value="2">2 - outdoor smokers</option>
               	<option value="3">3 - indoor smokers</option>
        	</select>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="8">
		<%
		if (recordAdded) { 
		%>
			<input type="submit" id="addHR" value="Update Record" <%= disableSubformsString %>>
		<%
		} else {
		%>
			<input type="submit" id="addHR" value="Add Record" <%= disableSubformsString %>>
		<%
		}
		%>
		</td>
	</tr>
<%//If the patient is under 144 months (12 years) old
} else if (patientAge < 144) { 
%>
	<!-- 
	Create a health history table with height, weight, bmi, blood pressure, household smoking status, 
	last recorded date, recorded by personnel, and action fields.
	 -->
	<tr>
    	<th colspan="8"><a href="#" class="topLink">[Top]</a>Basic Health Metrics</th>
    </tr>
	<tr class = "subHeader">
		<td>Height<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
		<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
		<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
		<td>Blood Pressure</td>
		<td>Household Smoking Status</td>
		<td>Last Recorded</td>
		<td>By Personnel</td>
		<td style="width: 60px;">Action</td>
	</tr>
	
	<%
	//Get health record associated with the current office visit
	HealthRecord hr = ovaction.healthRecord().getHealthRecordByOfficeVisit(ovaction.getOvID());
			
	//If no health record exists for this office visit
	if (hr == null) { 
		//Initialize a new health record
		hr = new HealthRecord();
		//And print a message indicating no message exists
		%>
		<tr>
    		<td colspan="8" style="text-align: center;">No Health Metrics on record</td>
    	</tr>
    	<%
    } else {
		//If there is a health record associated with this office visit,
		//get bmi as a string (weight in pounds * 703)/(height in inches * height in inches)
		DecimalFormat df = new DecimalFormat("00.00");
		String bmiString = "Invalid length value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = df.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
		}
		%>		
		<!-- 
		Get the height, weight, bmi, blood pressure, household smoking status, date recorded, recorded by personnel,
		and remove link for the health record.
		-->
		<tr>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bmiString)) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressure())) %> mmHg</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHouseholdSmokingStatusDesc())) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
			<%
			//Get the hcp who editted the health record and print his/her name
			PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
			%>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
			<td align=center>
                <a href="javascript:removeHealthRecordID('<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>');">Remove</a>
            </td>
		</tr>
	<%
		//Log that the hcp has viewed the patient health records
		ovaction.logOfficeVisitEvent(TransactionType.VIEW_BASIC_HEALTH_METRICS);
		//Change the recordAdded boolean to indicate that a health record already exists for this office visit
		recordAdded = true;
	}
	%>
	<!-- Display an add new record form with height, weight, blood pressure, and household smoking status fields -->
    <tr>
        <th colspan="8" style="text-align: center;">Add New</th>
    </tr>
	<tr>
		<td align="center" colspan="2">Height:  
			<input name="height"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> in.
		</td>
		<td align="center" colspan="2">Weight: 
			<input name="weight"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> lbs.
		</td>
		<td align="center" colspan="4">Blood Pressure (mmHg):  
			<input name="bloodPressureN" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressureN())) %>" style="width: 40px" maxlength="3" type="text" <%= disableSubformsString %>> 
			/ <input name="bloodPressureD" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressureD())) %>" style="width: 40px" maxlength="3" type="text" <%= disableSubformsString %>>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="8">Household Smoking Status: 
			<select name="householdSmokingStatus" id="householdSmokingStatus" style="font-size:10px;" <%= disableSubformsString %>>
           		<option value=""> -- Please Select a Household Smoking Status -- </option>
                <option value="1">1 - non-smoking household</option>
               	<option value="2">2 - outdoor smokers</option>
               	<option value="3">3 - indoor smokers</option>
        	</select>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="8">
		<%
		if (recordAdded) { 
		%>
			<input type="submit" id="addHR" value="Update Record" <%= disableSubformsString %>>
		<%
		} else {
		%>
			<input type="submit" id="addHR" value="Add Record" <%= disableSubformsString %>>
		<%
		}
		%>
		</td>
	</tr>
<%//If the patient is 12 or older
} else { 
%>
	<!-- 
	Create a health history table with height, weight, bmi, blood pressure, patient smoking status, household smoking status, cholesterol,
	last recorded date, recorded by personnel, and action fields.
	 -->
    <tr>
        <th colspan="13"><a href="#" class="topLink">[Top]</a>Basic Health Metrics</th>
    </tr>
	<tr class = "subHeader">
		<td>Height<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
		<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
		<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
		<td>Blood Pressure</td>
		<td>Smokes?</td>
		<td>Household Smoking Status</td>
		<td>HDL</td>
		<td>LDL</td>
		<td>Triglycerides</td>
		<td>Total Cholesterol</td>
		<td>Last Recorded</td>
		<td>By Personnel</td>
		<td style="width: 60px;">Action</td>
	</tr>
	
	<%
	//Get health record associated with the current office visit
	HealthRecord hr = ovaction.healthRecord().getHealthRecordByOfficeVisit(ovaction.getOvID());
			
	//If no health record exists for this office visit
	if (hr == null) { 
		//Initialize a new health record
		hr = new HealthRecord();
		//And print a message indicating no message exists
		%>
		<tr>
    		<td colspan="13" style="text-align: center;">No Health Metrics on record</td>
    	</tr>
    	<%
    } else {
		//If there is a health record associated with this office visit,
		//get bmi as a string (weight in pounds * 703)/(height in inches * height in inches)
		DecimalFormat df = new DecimalFormat("00.00");
		String bmiString = "Invalid height value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = df.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
		} 
	%>	
		<!-- 
		Get the height, weight, bmi, blood pressure, patient smoking status, household smoking status, cholesterol,
		date recorded, recorded by personnel, and remove link for the health record.
		-->	
		<tr>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bmiString)) %></td>
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
			<td align=center>
                <a href="javascript:removeHealthRecordID('<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>');">Remove</a>
            </td>
		</tr>
	<%
		//Log that the hcp has viewed the patient health records
		ovaction.logOfficeVisitEvent(TransactionType.VIEW_BASIC_HEALTH_METRICS);
		//Change the recordAdded boolean to indicate that a health record already exists for this office visit
		recordAdded = true;
	}
	%>
	<!-- 
	Display an add new record form with height, weight, blood pressure, cholesterol, 
	patient smoking status, and household smoking status fields 
	-->
    <tr>
        <th colspan="13" style="text-align: center;">Add New</th>
    </tr>
	<tr>
		<td align="center" colspan="2">Height:  
			<input name="height"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> in.
		</td>
		<td align="center" colspan="2">Weight: 
			<input name="weight"
			value="<%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>" style="width: 50px" type="text"
			maxlength="5" 
			<%= disableSubformsString %>> lbs.
		</td>
		<td align="center" colspan="4">Blood Pressure (mmHg):  
			<input name="bloodPressureN" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressureN())) %>" style="width: 40px" maxlength="3" type="text" <%= disableSubformsString %>> 
			/ <input name="bloodPressureD" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressureD())) %>" style="width: 40px" maxlength="3" type="text" <%= disableSubformsString %>>
		</td>
		<td align="center" colspan="2">Cholesterol (mg/dL):</td>
		<td align="center" colspan="3">
		<table>
			<tr>
				<td style="text-align: right">HDL:</td>
				<td><input name="cholesterolHDL" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolHDL())) %>" 
				style="width: 38px" maxlength="3" type="text" <%= disableSubformsString %>></td>
			</tr>
			<tr>
				<td style="text-align: right">LDL:</td>
				<td>
					<input name="cholesterolLDL" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolLDL())) %>" style="width: 38px" maxlength="3" type="text" <%= disableSubformsString %>>
				</td>
			</tr>
			<tr>
				<td style="text-align: right">Tri:</td>
				<td>
					<input name="cholesterolTri" value="<%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolTri())) %>" style="width: 38px" maxlength="3" type="text" <%= disableSubformsString %>>
			    </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="6">Smoker?: 
			<select name="isSmoker" id="isSmoker" style="font-size:10px;" <%= disableSubformsString %>>
           		<option value=""> -- Please Select a Smoking Status -- </option>
                <option value="1">1 - Current every day smoker</option>
               	<option value="2">2 - Current some day smoker</option>
               	<option value="3">3 - Former smoker</option>
               	<option value="4">4 - Never smoker</option>
               	<option value="5">5 - Smoker, current status unknown</option>
               	<option value="9">9 - Unknown if ever smoked</option>
        	</select>
		</td>
		<td align="center" colspan="7">Household Smoking Status: 
			<select name="householdSmokingStatus" id="householdSmokingStatus" style="font-size:10px;" <%= disableSubformsString %>>
           		<option value=""> -- Please Select a Household Smoking Status -- </option>
                <option value="1">1 - non-smoking household</option>
               	<option value="2">2 - outdoor smokers</option>
               	<option value="3">3 - indoor smokers</option>
        	</select>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="13">
		<%
		if (recordAdded) { 
		%>
			<input type="submit" id="addHR" value="Update Record" <%= disableSubformsString %>>
		<%
		} else {
		%>
			<input type="submit" id="addHR" value="Add Record" <%= disableSubformsString %>>
		<%
		}
		%>
		</td>
	</tr>
<% 
} 
%>
</table>
</form>
</div>

<% } %>
