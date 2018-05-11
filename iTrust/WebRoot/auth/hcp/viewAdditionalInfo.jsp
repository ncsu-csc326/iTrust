<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Additional Info";
%>

<%@include file="/header.jsp" %>

<%
int patientIndex = 0;
if(request.getParameter("patient") != null){
	String patient = request.getParameter("patient");
	session.setAttribute("patient", patient);
	patientIndex = Integer.parseInt(patient);
}
else{
	String patient = (String) session.getAttribute("patient");
	patientIndex = Integer.parseInt(patient);
}
List<RemoteMonitoringDataBean> patientList = (List<RemoteMonitoringDataBean>) session.getAttribute("patientList");
RemoteMonitoringDataBean rmdb = patientList.get(patientIndex);
PatientDAO patDAO = prodDAO.getPatientDAO();
long patientMID = rmdb.getPatientMID();
PatientBean p = patDAO.getPatient(patientMID);

loggingAction.logEvent(TransactionType.TELEMEDICINE_DATA_VIEW, loggedInMID.longValue(), patientMID, "");

ViewMyRemoteMonitoringListAction listAction = new ViewMyRemoteMonitoringListAction(prodDAO,loggedInMID.longValue());
String startDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
String endDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
if("date".equals(request.getParameter("sortBy"))) {
	startDate = request.getParameter("startDate");
	endDate = request.getParameter("endDate");
%>
	<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="2">Patient Details</th>
		</tr>
		<tr >
			<td class="subHeaderVertical">Name:</td>
			<td><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
		</tr>
		<tr >
			<td class="subHeaderVertical">Phone:</td>
			<td><%= StringEscapeUtils.escapeHtml("" + (p.getPhone())) %></td>
		</tr>
	</table>
	<br />
<%
	List<PatientBean> patientRepresentatives = patDAO.getRepresenting(patientMID);
%>
	<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="4">Patient's Representatives</th>
		</tr>
<%
	if(patientRepresentatives.isEmpty()){
%>
		<tr>
			<td>This patient has no representatives.</td>
		</tr>
<%
	}
	for(PatientBean pat : patientRepresentatives){
%>
		<tr>
			<td class="subHeaderVertical">Name:</td>
			<td><%= StringEscapeUtils.escapeHtml("" + (pat.getFullName())) %></td>
			<td class="subHeaderVertical">Phone:</td>
			<td><%= StringEscapeUtils.escapeHtml("" + (pat.getPhone())) %></td>
		</tr>
<%
	}
%>
	</table>
	<br />
	<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="7">Patient BP/Glucose Statistics</th>
		</tr>
		<tr class="subHeader">
			<th width="20%">Date</th>
			<th>Systolic Blood Pressure</th>
			<th>Diastolic Blood Pressure</th>
			<th>Glucose Level</th>
			<th>Reporter</th>
		</tr>
<%
	List<RemoteMonitoringDataBean> patientPhysiologicStats = listAction.getPatientDataByDate(patientMID, startDate, endDate);
	if(patientPhysiologicStats.isEmpty()){
%>
		<tr bgcolor="#ff6666">
			<td><b>No Information Provided</b></td>
			<td><b></b></td>
			<td><b></b></td>
			<td><b></b></td>
			<td><b></b></td>
		</tr>
<%
	}
	String highlight = "";
	String reporterName = "";
	for(RemoteMonitoringDataBean stats : patientPhysiologicStats){
		highlight = "";
		int sysBP = stats.getSystolicBloodPressure();
		int diasBP = stats.getDiastolicBloodPressure();
		int gluLvl = stats.getGlucoseLevel();
		
		if (gluLvl != -1 || diasBP != -1 || sysBP != -1) {
			// Highlighting only partially implemented here; commented out.
			//if(((sysBP != -1) && (sysBP < 90 || sysBP > 140)) || ((diasBP != -1) && (diasBP < 60 || diasBP > 90)) || ((gluLvl != -1) && (gluLvl < 70 || gluLvl > 150))) {
			//	highlight = "#ffff00";
			//}
			
			reporterName = authDAO.getUserName(stats.getReporterMID());		
%>
			<tr bgcolor="<%= StringEscapeUtils.escapeHtml("" + (highlight)) %>">
				<td><%= StringEscapeUtils.escapeHtml("" + (stats.getTime())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (sysBP == -1?"":sysBP)) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (diasBP == -1?"":diasBP)) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (gluLvl == -1?"":gluLvl)) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (reporterName)) %></td>
			</tr>
<%
		}
	}
%>
	</table>
	<br />
	<br />
	<table class="fTable" align="center">
	<tr>
		<th colspan="7">Patient Weight/Pedometer Statistics</th>
	</tr>
	<tr class="subHeader">
		<th width="20%">Date</th>
		<th>Height</th>
		<th>Weight</th>
		<th>Pedometer Reading</th>
		<th>Reporter</th>
	</tr>
<%
	List<RemoteMonitoringDataBean> patientExternalStats = listAction.getPatientDataByDate(patientMID, startDate, endDate);
	if(patientExternalStats.isEmpty()){
%>
		<tr bgcolor="#ff6666">
			<td><b>No Information Provided</b></td>
			<td><b></b></td>
			<td><b></b></td>
			<td><b></b></td>
			<td><b></b></td>
		</tr>
<%
	}
	highlight = "";
	reporterName = "";
	for(RemoteMonitoringDataBean stats : patientExternalStats){
		highlight = "";
		float height = stats.getHeight();
		float weight = stats.getWeight();
		int pedoReading = stats.getPedometerReading();
		
		if (stats.getWeight() != -1) {
			RemoteMonitoringDataBean prevTemp = null;
			List<RemoteMonitoringDataBean> weightBeanList = listAction.getPatientDataByType(stats.getPatientMID(), "weight");

			for(RemoteMonitoringDataBean curTemp : weightBeanList) {
				if (prevTemp == null) {
					prevTemp = curTemp;
					continue;
				}
				if (stats.equals(prevTemp)) {
					break;
				}
				if (prevTemp.getTime().before(stats.getTime()) && (stats.getTime().before(curTemp.getTime())) || stats.getTime().equals(curTemp.getTime())) {
					break;
				} else {
					prevTemp = curTemp;
				}
			}

			// Highlighting only partially implemented here; commented out.
			//if (prevTemp != null && Math.abs((prevTemp.getWeight() - stats.getWeight()) / stats.getWeight()) >= .05) {
			//	highlight = "#ffff00";
			//} 
		}
		
		if (height != -1 || weight != -1 || pedoReading != -1) {
			reporterName = authDAO.getUserName(stats.getReporterMID());		
%>
			<tr bgcolor="<%= StringEscapeUtils.escapeHtml("" + (highlight)) %>">
				<td><%= StringEscapeUtils.escapeHtml("" + (stats.getTime())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (height == -1?"":height )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (weight == -1?"":weight )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (pedoReading == -1?"":pedoReading )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (reporterName)) %></td>
			</tr>
<%
		}
	}
%>
	</table>
	<br />
<%
} else{
%>

<form action="viewAdditionalInfo.jsp" id=datebuttons style="display: inline;" method="post">
<input type="hidden" name="sortBy" value="date"></input>
<div align=center>
<table class="fTable" align="center">
	<tr class="subHeader">
		<td>Start Date:</td>
		<td>
			<input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>
		<td>End Date:</td>
		<td>
			<input name="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (endDate)) %>">
			<input type=button value="Select Date" onclick="displayDatePicker('endDate');">
		</td>
	</tr>
</table>
<br />
<input type="submit" name="submit" value="Get Records" onclick="javascript:sortBy();">
</div>
</form>
<%
}
%>

<script type='text/javascript'>
function sortBy() {
	document.getElementsByName('sortBy')[0].value = "date";
	document.forms[0].submit.click();
}
</script>

<%@include file="/footer.jsp"%>