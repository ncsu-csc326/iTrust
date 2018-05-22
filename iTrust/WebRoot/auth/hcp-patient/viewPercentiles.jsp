<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewHealthRecordsHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.UploadReferenceTablesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>


<%@include file="/global.jsp" %>








<%
// Retrieve type of data from URL
String dataType = (String)request.getParameter("dataType");
String record = (String)request.getParameter("healthRecord");
int recordIndex = Integer.parseInt(record);

if (!dataType.equals("Height") && !dataType.equals("Weight") && !dataType.equals("BMI") && !dataType.equals("HeadCirc")) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewBasicHealth.jsp");
	return;
}

pageTitle = "iTrust - View Percentiles";

//Require a Patient ID first
String role = (String)session.getAttribute("userRole");
String pidString = "";

if(role.equals("patient")){
	pidString = loggedInMID.toString();
}
else{
	pidString = (String)session.getAttribute("pid");
}

//Get list of health records
List<HealthRecord> healthRecords = (List<HealthRecord>) session.getAttribute("healthRecordsList");
//Get the health record associated with this page
HealthRecord healthRecord = healthRecords.get(recordIndex);
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="View Percentiles" />
<script type="text/javascript" src="js/d3.js"></script>

<%
ViewHealthRecordsHistoryAction historyAction = new ViewHealthRecordsHistoryAction(prodDAO,pidString,loggedInMID.longValue());
session.setAttribute("historyAction", historyAction);

String patientName = historyAction.getPatientName();
boolean tripper = false;
//Double value for holding the z score
double zScore = -999;
double percentile = 0.0;

if(dataType.equals("Weight")){
	zScore = historyAction.getWeightZScore(healthRecord);
	if(zScore > -999){
		percentile = historyAction.getPercentile(zScore);
		tripper = true;
	}
	else{
		%>
		<p align=center style="font-size:20px"><i>Percentile data unavailable for selected health metric</i></p>
		<%
	}	
}
else if(dataType.equals("Height")){
	zScore = historyAction.getHeightZScore(healthRecord);
	if(zScore > -999){
		percentile = historyAction.getPercentile(zScore);
		tripper = true;
	}
	else{
		%>
		<p align=center style="font-size:20px"><i>Percentile data unavailable for selected health metric</i></p>
		<%
	}	
}
else if(dataType.equals("HeadCirc")){
	zScore = historyAction.getHeadCircZScore(healthRecord);
	if(zScore > -999){
		percentile = historyAction.getPercentile(zScore);
		tripper = true;
	}
	else{
		%>
		<p align=center style="font-size:20px"><i>Percentile data unavailable for selected health metric</i></p>
		<%
	}	
}
else if(dataType.equals("BMI")){
	zScore = historyAction.getBMIZScore(healthRecord);
	if(zScore > -999){
		percentile = historyAction.getPercentile(zScore);
		tripper = true;
	}
	else{
		%>
		<p align=center style="font-size:20px"><i>Percentile data unavailable for selected health metric</i></p>
		<%
	}	
}

%>


<% if(tripper){ %>
<br />
<br />
<br />
<div align=center>
	<table id="PercentilesTable" name="PercentilesTable" align="center" class="fTable">
		<tr>
			<th colspan="20" style="text-align: center;"><%= patientName %>'s Health Metric Percentile</th>
			</tr>
			<tr class = "subHeader">
				<td colspan="2">Office Visit Date</td>
				<td colspan="2">Patient Age</td>
				<td colspan="2"><%=dataType%></td>
				<td colspan="2">Percentile</td>
			</tr>
			<tr>
				<td align=center colspan="2"> <%= healthRecord.getVisitDateStr() %></td>
			<% if (historyAction.calculateAgeInMonthsAtOfficeVisit(healthRecord.getOfficeVisitID()) < 12) { %>
				<td align=center colspan="2"> <%= historyAction.calculateAgeInMonthsAtOfficeVisit(healthRecord.getOfficeVisitID()) %> mos</td>
			<% } else { %>
				<td align=center colspan="2"> <%= historyAction.calculateAgeInMonthsAtOfficeVisit(healthRecord.getOfficeVisitID()) / 12 %> yrs</td>
			<% } %>
			<% if (dataType.equals("Weight")) { %>
				<td align=center colspan="2"> <%= healthRecord.getWeight() %> lbs</td>
			<% } else if (dataType.equals("Height")) { %>
				<td align=center colspan="2"> <%= healthRecord.getHeight() %> in</td>	
			<% } else if (dataType.equals("HeadCirc")) { %>
				<td align=center colspan="2"> <%= healthRecord.getHeadCircumference() %> in</td>
			<% } else if (dataType.equals("BMI")) { %>
				<td align=center colspan="2"> <%= healthRecord.getBodyMassIndex() %></td>
			<% } else { %>
				<td align=center colspan="2"> N/A </td>
			<% } %>
				<td align=center colspan="2"> <%= percentile %>% </td>
			</tr>
	</table>
</div>

<%} %>

<br />


<p align=center><i>Select the percentile in the chart legend below to display/hide that percentile curve.</i></p>

<br />


<script src="/iTrust/js/jquery-1.7.2.js" type="text/javascript"></script>
<script src="/iTrust/js/highcharts.js" type="text/javascript"></script>
<script src="/iTrust/js/highcharts-more.js" type="text/javascript"></script>


<%
if(dataType.equals("Weight")){
	if(historyAction.calculateAgeInMonthsAtOfficeVisit(healthRecord.getOfficeVisitID()) <= 36){
		if(historyAction.getPatientGender().equals("Male")){
%>
	<script src="/iTrust/js/percentiles/wtageinf_males.js" type="text/javascript"></script>
	<script type="text/javascript">
		chartSetup();
	</script>
<%
		}
		else if(historyAction.getPatientGender().equals("Female")){
%>
	<script src="/iTrust/js/percentiles/wtageinf_females.js" type="text/javascript"></script>
	<script type="text/javascript">
		chartSetup();
	</script>	
<%		
		}
	}
	else{
		if(historyAction.getPatientGender().equals("Male")){
			%>
				<script src="/iTrust/js/percentiles/wtage_males.js" type="text/javascript"></script>
				<script type="text/javascript">
					chartSetup();
				</script>
			<%
		}
		else if(historyAction.getPatientGender().equals("Female")){
			%>
				<script src="/iTrust/js/percentiles/wtage_females.js" type="text/javascript"></script>
				<script type="text/javascript">
					chartSetup();
				</script>	
			<%		
		}
	}
}
else if(dataType.equals("Height")){
	if(historyAction.calculateAgeInMonthsAtOfficeVisit(healthRecord.getOfficeVisitID()) <= 36){
		if(historyAction.getPatientGender().equals("Male")){
%>	
			<script src="/iTrust/js/percentiles/lenageinf_males.js" type="text/javascript"></script>
			<script type="text/javascript">
				chartSetup();
			</script>	
<%
		}
		else if(historyAction.getPatientGender().equals("Female")){
%>
			<script src="/iTrust/js/percentiles/lenageinf_females.js" type="text/javascript"></script>
			<script type="text/javascript">
				chartSetup();
			</script>	
<%		
		}
	}
	else{
		if(historyAction.getPatientGender().equals("Male")){
			%>
			<script src="/iTrust/js/percentiles/statage_males.js" type="text/javascript"></script>
			<script type="text/javascript">
				chartSetup();
			</script>		
	
			<%
		}
		else if(historyAction.getPatientGender().equals("Female")){
			%>
			<script src="/iTrust/js/percentiles/statage_females.js" type="text/javascript"></script>
			<script type="text/javascript">
				chartSetup();
			</script>		
	
		<%		
		}
	}
}

else if(dataType.equals("HeadCirc")){
	if(historyAction.getPatientGender().equals("Male")){
		%>
		<script src="/iTrust/js/percentiles/hcageinf_males.js" type="text/javascript"></script>
		<script type="text/javascript">
			chartSetup();
		</script>			
		<%
	}
	else if(historyAction.getPatientGender().equals("Female")){
		%>
		<script src="/iTrust/js/percentiles/hcageinf_females.js" type="text/javascript"></script>
		<script type="text/javascript">
			chartSetup();
		</script>			
		
		<%
	}
	
}
else if(dataType.equals("BMI")){
	if(historyAction.getPatientGender().equals("Male")){
		%>
		<script src="/iTrust/js/percentiles/bmi_males.js" type="text/javascript"></script>
		<script type="text/javascript">
			chartSetup();
		</script>			
		<%
	}
	else if(historyAction.getPatientGender().equals("Female")){
		%>
		<script src="/iTrust/js/percentiles/bmi_females.js" type="text/javascript"></script>
		<script type="text/javascript">
			chartSetup();
		</script>		
		
		<%
	}
}
%>

<div id="container" style="height: 600px"></div>





<br />
<br />
<br />
<br />
<itrust:patientNav thisTitle="View Percentiles" />

<%@include file="/footer.jsp" %>