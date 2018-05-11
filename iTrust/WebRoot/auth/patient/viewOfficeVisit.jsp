<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.DecimalFormat"%>

<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Office Visit Details";
%>

<%@include file="/header.jsp"%>
<%

session.removeAttribute("personnelList");

	String ovID = request.getParameter("ovID");
	String repMID = request.getParameter("repMID");
	
	long logMID;
	if(repMID == null){
		logMID = 0; 
	} else {
		logMID = Long.parseLong(repMID);
	}
		
	loggingAction.logEvent(TransactionType.OFFICE_VISIT_VIEW, loggedInMID.longValue(), logMID, "Office Visit: " + ovID);
	
	ViewOfficeVisitAction ovaction = null;
	
	if(repMID != null){
		//This constructor checks that the representative is correct
		ovaction = new ViewOfficeVisitAction(prodDAO, loggedInMID.longValue(), repMID, ovID);
	} else{
		ovaction = new ViewOfficeVisitAction(prodDAO, loggedInMID.longValue(),ovID);
	}

	/* (if(request.getParameter() */
	PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());	
	DateFormat df = DateFormat.getDateInstance();
	OfficeVisitBean ovbean = ovaction.getOfficeVisit();	
	String hcpName = ovaction.getHCPName(ovbean.getHcpID());
	String type = ovbean.getAppointmentType();
	boolean isBilled = ovbean.isBilled();
	
	//Get the patient's age in months
	int patientAge = ovaction.getPatientAgeInMonths();
	
	//Get the health record associated with this office visit
	HealthRecord hr = ovaction.getHealthRecord();
	
	List<LabProcedureBean> procs = ovaction.getLabProcedures();
	ovaction.setViewed(procs);
%>

<br />
<table class="fTable" align="center">
	<tr><th colspan=2>Office Visit Details</th></tr>
	<tr>
		<td  class="subHeader">Date:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + (ovbean.getVisitDateStr())) %></td>
	</tr>
	<tr>
		<td  class="subHeader">HCP:</td>
<%
		List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
		int index = 0;
%>
		<td><a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (hcpName)) %></a></td>
<%
		PersonnelBean personnel = new PersonnelDAO(prodDAO).getPersonnel(ovbean.getHcpID());
		personnelList.add(personnel);
		index++;
		session.setAttribute("personnelList", personnelList);
%>
	</tr>
	<tr>
		<td class="subHeader">Appointment Type:</td>
		<td>
			<%
			if(type == null || "null".equals(type)){
			%>
			N/A
			<%
			} else {
			%>
			<%= type %>
			<%} %>
		</td>
	</tr>
	<tr>
		<td class="subHeader">Were you billed?</td>
		<td>
			<%
			if(isBilled){
			%>
			Yes
			<%
			} else {
			%>
			No
			<%} %>
		</td>
	</tr>
	<tr>
		<td  class="subHeader">Notes:</td>
		<td>
			<%= StringEscapeUtils.escapeHtml("" + ( ovbean.getNotes() )) %>
		</td>
	</tr>
</table>
<br /><br />
<table class="fTable" align="center">
<%//If the patient is less than 36 months (3 years) old
if (patientAge < 36) {
%>
	<!-- 
	Create a health history table with length, weight, bmi, head circumference, household smoking status, 
	last recorded date, and recorded by personnel fields
	 -->
	<tr>
    	<th colspan="7">Basic Health Metrics</th>
    </tr>
	<tr class = "subHeader">
		<td>Length</td>
		<td>Weight</td>
		<td>BMI</td>
		<td>Head Circumference</td>
		<td>Household Smoking Status</td>
		<td>Last Recorded</td>
		<td>By Personnel</td>
	</tr>
	
	<%
	//If no health record exists for this office visit
	if (hr == null) {
		//Prints a message indicating no message exists
		%>
		<tr>
    		<td colspan="7" style="text-align: center;">No Health Metrics on record</td>
    	</tr>
    	<%
    } else {
		//If there is a health record associated with this office visit,
		//get bmi as a string (weight in pounds * 703)/(height in inches * height in inches)
		DecimalFormat decf = new DecimalFormat("00.00");
		String bmiString = "Invalid length value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = decf.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
		}
		%>
		<!-- 
		Get the height, weight, bmi, head circumference, household smoking status, date recorded, and recorded by personnel
		for the health record.
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
		</tr>
	<%
	}
//If the patient is under 144 months (12 years) old
} else if (patientAge < 144) { 
%>
	<!-- 
	Create a health history table with height, weight, bmi, blood pressure, household smoking status, 
	last recorded date, and recorded by personnel fields.
	 -->
	<tr>
    	<th colspan="7">Basic Health Metrics</th>
    </tr>
	<tr class = "subHeader">
		<td>Height</td>
		<td>Weight</td>
		<td>BMI</td>
		<td>Blood Pressure</td>
		<td>Household Smoking Status</td>
		<td>Last Recorded</td>
		<td>By Personnel</td>
	</tr>
	
	<%
			
	//If no health record exists for this office visit
	if (hr == null) {
		//Print a message indicating no message exists
		%>
		<tr>
    		<td colspan="7" style="text-align: center;">No Health Metrics on record</td>
    	</tr>
    	<%
    } else {
		//If there is a health record associated with this office visit,
		//get bmi as a string (weight in pounds * 703)/(height in inches * height in inches)
		DecimalFormat decf = new DecimalFormat("00.00");
		String bmiString = "Invalid length value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = decf.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
		}
		%>		
		<!-- 
		Get the height, weight, bmi, blood pressure, household smoking status, date recorded, and recorded by personnel
		for the health record.
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
		</tr>
	<%
	}
//If the patient is 12 or older
} else { 
%>
	<!-- 
	Create a health history table with height, weight, bmi, blood pressure, patient smoking status, household smoking status, cholesterol,
	last recorded date, recorded by personnel, and action fields.
	 -->
    <tr>
        <th colspan="12">Basic Health Metrics</th>
    </tr>
	<tr class = "subHeader">
		<td>Height</td>
		<td>Weight</td>
		<td>BMI</td>
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
	//If no health record exists for this office visit
	if (hr == null) { 
		//Print a message indicating no message exists
		%>
		<tr>
    		<td colspan="12" style="text-align: center;">No Health Metrics on record</td>
    	</tr>
    	<%
    } else {
		//If there is a health record associated with this office visit,
		//get bmi as a string (weight in pounds * 703)/(height in inches * height in inches)
		DecimalFormat decf = new DecimalFormat("00.00");
		String bmiString = "Invalid height value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = decf.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
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
		</tr>
	<%
	}
} 
%>
</table>
<br /><br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="3">Diagnoses</th>
	</tr>
	<tr  class="subHeader">
		<th>ICD Code</th>
		<th>Description</th>
		<th>URL</th>
	</tr>
	<% if (ovaction.getDiagnoses().size() == 0) { %>
	<tr>
		<td colspan="2" >No Diagnoses for this visit</td>
	</tr>
	<% } else { 
		ICDCodesDAO icdcodesDAO = prodDAO.getICDCodesDAO();
		for(DiagnosisBean d : ovaction.getDiagnoses()) { String link = icdcodesDAO.getICDCode(d.getICDCode()).getURL();%>
		<tr>
			<td ><itrust:icd9cm code="<%= StringEscapeUtils.escapeHtml(d.getICDCode()) %>"/></td>
			<td  style="white-space: nowrap;"><%= StringEscapeUtils.escapeHtml("" + (d.getDescription() )) %></td>
			<td><a href=<%= link %>><%= link %></a></td>
		</tr>
	   <%} 
  	   }  %>
</table>
<br /><br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="5">Medications</th>
	</tr>
	<tr class="subHeader">
		<td>NDCode</td>
		<td>Description</td>
		<td>Date of Usage</td>
		<td>Dosage</td>
		<td>Instructions</td>
	</tr>
	<% if (ovaction.getPrescriptions().size() == 0) { %>
	<tr>
		<td colspan="5" class = "valueCell" align="center">No Medications on record</td>
	</tr>
	<% } else { 
		for(PrescriptionBean m : ovaction.getPrescriptions()) { %>
		<tr>
			<td class = "valueCell"><%= StringEscapeUtils.escapeHtml("" + (m.getMedication().getNDCodeFormatted())) %></td>
			<td class = "valueCell"><%= StringEscapeUtils.escapeHtml("" + (m.getMedication().getDescription() )) %></td>
			<td class = "valueCell"><%= StringEscapeUtils.escapeHtml("" + (m.getStartDateStr())) %> to <%= StringEscapeUtils.escapeHtml("" + (m.getEndDateStr())) %></td>
			<td class = "valueCell"><%= StringEscapeUtils.escapeHtml("" + (m.getDosage())) %>mg</td>
			<td class = "valueCell"><%= StringEscapeUtils.escapeHtml("" + (m.getInstructions())) %></td>
		</tr>
	<%  } 
	  } %>
</table>
<br /><br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="2">Procedures</th>
	</tr>
	<tr class="subHeader">
		<td>CPT Code</td>
		<td>Description</td>
	</tr>
	<% if (ovaction.getProcedures().size() == 0) { %>
	<tr>
		<td colspan="2" >No Procedures on record</td>
	</tr>
	<% } else { 
		for(ProcedureBean p : ovaction.getAllProcedures()) {%>
		<tr>
			<td ><%= StringEscapeUtils.escapeHtml("" + (p.getCPTCode() )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (p.getDescription() )) %></td>
		</tr>
	<%  } 
	   }  %>
</table>
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="8">Lab Procedures</th>
	</tr>
	<tr class="subHeader">
        <td>Patient</td>
        <td>Lab Procedure</td>
        <td>Status</td>
        <td>Commentary</td>
        <td colspan="2">Numerical<br/>Result</td>
        <td>Normality</td>
        <td>Updated Date</td>
	</tr>
<%
	if(procs.size() > 0 ) {
		for (LabProcedureBean bean : procs) {
			String status = bean.getStatus();
			String commentary = "";
			String numericalResult = "";
			String numericalResultUnit = "";
			String lowerBound = "";
			String upperBound = "";
			String normality = "";
			if (status.equals(LabProcedureBean.Completed)) {
	            commentary =       StringEscapeUtils.escapeHtml("" + (bean.getCommentary()));
	            numericalResult =  StringEscapeUtils.escapeHtml("" + (bean.getNumericalResult()));
	            numericalResultUnit = StringEscapeUtils.escapeHtml("" + (bean.getNumericalResultUnit()));
	            lowerBound =       StringEscapeUtils.escapeHtml("" + (bean.getLowerBound()));
	            upperBound =       StringEscapeUtils.escapeHtml("" + (bean.getUpperBound()));
	            
	            float res = java.lang.Float.parseFloat(numericalResult);
	            float lower = java.lang.Float.parseFloat(lowerBound);
	            float upper = java.lang.Float.parseFloat(upperBound);
	            
	            if ( res < lower ) {
	            	
	            	normality = "Abnormal";
	            	
	            } else if ( res > upper ) {
	            	
	            	normality = "Abnormal";
	            	
	            } else {
	            	
	            	normality = "Normal";
	            	
	            }
	            
			}
%>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (bean.getLoinc())) %></td>
		        <td><%= status %></td>
		        <td><%= commentary %></td>
		        <td><%= numericalResult %></td>
		        <td><%= numericalResultUnit %></td>
		        <td><%= normality %></td>
		        <td><%= StringEscapeUtils.escapeHtml("" + (bean.getTimestamp())) %></td>
			</tr>
<%
		}
	}
	else {
%>
		<tr>
			<td colspan=8 align=center>
				No Data
			</td>
		</tr>
<%
	}
%>
</table>
<br />
<%@include file="/footer.jsp"%>
