<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO" %>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Monitor Patients";
%>

<%@include file="/header.jsp" %>

<%
loggingAction.logEvent(TransactionType.TELEMEDICINE_DATA_VIEW, loggedInMID.longValue(), 0, "Viewed monitored patients");

session.removeAttribute("patientList");

ViewMyRemoteMonitoringListAction action = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> data = action.getPatientsData();
session.setAttribute("patientList", data);

PatientDAO patDAO = prodDAO.getPatientDAO();
PersonnelDAO persDAO = prodDAO.getPersonnelDAO();
%>

<%@page import="java.sql.Time"%><br />
<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="7">Patient Physiologic Statistics</th>
		</tr>
		<tr class="subHeader">
			<th>Patient</th>
			<th width="20%">Date</th>
			<th>Systolic Blood Pressure</th>
			<th>Diastolic Blood Pressure</th>
			<th>Glucose Level</th>
			<th>Reporter</th>
		</tr>
<%
	String highlight = "";
	String reporterName = "";
	int index = 0;
	for(RemoteMonitoringDataBean bean : data){
		if (bean.getReporterMID() == 0) {
			highlight = "#ff6666";
			%>
			<tr bgcolor="<%= StringEscapeUtils.escapeHtml("" + (highlight)) %>">
			<td ><a href="/iTrust/auth/hcp/viewAdditionalInfo.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (action.getPatientName(bean.getPatientMID()) + " (MID " + bean.getPatientMID() + ")")) %></a></td>
					<td><b>No Information Provided</b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
			</tr>
			<%
		} else {
			highlight = "";
			int sysBP = bean.getSystolicBloodPressure();
			int diasBP = bean.getDiastolicBloodPressure();
			int gluLvl = bean.getGlucoseLevel();
		
			if (gluLvl != -1 || diasBP != -1 || sysBP != -1) {
				//Keep track if the table is empty
				if(bean.getTime() == null) {
					highlight = "#ff6666";
				} else if(((bean.getSystolicBloodPressure() != -1) && (bean.getSystolicBloodPressure() < 90 || bean.getSystolicBloodPressure() > 140))
						|| ((bean.getDiastolicBloodPressure() != -1) && (bean.getDiastolicBloodPressure() < 60 || bean.getDiastolicBloodPressure() > 90))
						|| ((bean.getGlucoseLevel() != -1 ) && (bean.getGlucoseLevel() < 70 || bean.getGlucoseLevel() > 150))) {
					highlight = "#ffff00";
					reporterName = authDAO.getUserName(bean.getReporterMID());
				} else {
					reporterName = authDAO.getUserName(bean.getReporterMID());
				}
				
				reporterName = authDAO.getUserName(bean.getReporterMID());		
			%>
				<tr bgcolor="<%= StringEscapeUtils.escapeHtml("" + (highlight)) %>">
				<td ><a href="/iTrust/auth/hcp/viewAdditionalInfo.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (action.getPatientName(bean.getPatientMID()) + " (MID " + bean.getPatientMID() + ")")) %></a></td>
						<td><%= StringEscapeUtils.escapeHtml("" + (bean.getTime())) %></td>
						<td><%= StringEscapeUtils.escapeHtml("" + (sysBP == -1?"":sysBP)) %></td>
						<td><%= StringEscapeUtils.escapeHtml("" + (diasBP == -1?"":diasBP)) %></td>
						<td><%= StringEscapeUtils.escapeHtml("" + (gluLvl == -1?"":gluLvl)) %></td>
						<td><%= StringEscapeUtils.escapeHtml("" + (reporterName)) %></td>
					</tr>
			<%
			}
		}
		index++;
	}

	if(index == 0) {
		%>
				<tr bgcolor="#ff6666">
					<td><b>No Information Provided</b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
				</tr>
		<%
	}
%>
	</table>
	<br />
	<br />
	<table class="fTable" align="center">
	<tr>
		<th colspan="7">Patient External Statistics</th>
	</tr>
	<tr class="subHeader">
		<th>Patient</th>
		<th width="20%">Date</th>
		<th>Height</th>
		<th>Weight</th>
		<th>Pedometer Reading</th>
		<th>Reporter</th>
	</tr>
<%
	index = 0;
	for(RemoteMonitoringDataBean bean : data){
		if(bean.getReporterMID() == 0) {
			highlight = "#ff6666";
			%>
			<tr bgcolor="<%= StringEscapeUtils.escapeHtml("" + (highlight)) %>">
				<td ><a href="/iTrust/auth/hcp/viewAdditionalInfo.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (action.getPatientName(bean.getPatientMID()) + " (MID " + bean.getPatientMID() + ")")) %></a></td>
				<td><b>No Information Provided</b></td>
				<td><b></b></td>
				<td><b></b></td>
				<td><b></b></td>
				<td><b></b></td>
			</tr>
			<%
		} else {
			highlight = "";
			reporterName = "";
			float height = bean.getHeight();
			float weight = bean.getWeight();
			int pedoReading = bean.getPedometerReading();
			
			//Check for highlighting due to %5 change from immediately previous weight record.					
			if(bean.getWeight() != -1) {
				RemoteMonitoringDataBean prevTemp = null;
				List<RemoteMonitoringDataBean> weightBeanList = action.getPatientDataByType(bean.getPatientMID(), "weight");

				for(RemoteMonitoringDataBean curTemp : weightBeanList) {
					if (prevTemp == null) {
						prevTemp = curTemp;
						continue;
					}
					if (bean.equals(prevTemp)) {
						break;
					}
					if (prevTemp.getTime().before(bean.getTime()) && (bean.getTime().before(curTemp.getTime())) || bean.getTime().equals(curTemp.getTime())) {
						break;
					} else {
						prevTemp = curTemp;
					}
				}
	
				if (prevTemp != null && Math.abs((prevTemp.getWeight() - bean.getWeight()) / bean.getWeight()) >= .05) {
					highlight = "#ffff00";
				} 
			} //else if(bean.getPedometerReading() == -1 && bean.getHeightReading() == -1) {
			else if (pedoReading == -1 && height == -1) {
				highlight = "#ff6666";
			}
			
			if (weight != -1 || pedoReading != -1 || height != -1) {
				reporterName = authDAO.getUserName(bean.getReporterMID());
	%>
				<tr bgcolor="<%= StringEscapeUtils.escapeHtml("" + (highlight)) %>">
					<td ><a href="/iTrust/auth/hcp/viewAdditionalInfo.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (action.getPatientName(bean.getPatientMID()) + " (MID " + bean.getPatientMID() + ")")) %></a></td>
					<td><%= StringEscapeUtils.escapeHtml("" + (bean.getTime())) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + (height == -1?"":height )) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + (weight == -1?"":weight )) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + (pedoReading == -1?"":pedoReading )) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + (reporterName)) %></td>
				</tr>
	<%
			}
		}
		index++;
	}
	
	if(index == 0){
		%>
				<tr bgcolor="#ff6666">
					<td><b>No Information Provided</b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
					<td><b></b></td>
				</tr>
		<%
	}
%>
	</table>
<br />

<%@include file="/footer.jsp" %>