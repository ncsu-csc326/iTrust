<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddRemoteMonitoringDataAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TelemedicineBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="java.util.List"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Report Status";
%>

<%@include file="/header.jsp"%>

<%
	/* Require a Patient ID first */
long patientMID;
boolean representativeReporting = false;
String reportingFor = (String)request.getParameter("reportingFor");
if(reportingFor == null || 1 > reportingFor.length() || reportingFor.equals("null")) {
	patientMID = loggedInMID.longValue(); //If self-reporting
} else {
	patientMID = Long.parseLong(reportingFor);
	representativeReporting = true;
}

	AddRemoteMonitoringDataAction action = new AddRemoteMonitoringDataAction(
	prodDAO, loggedInMID.longValue(), patientMID);
	
	List<TelemedicineBean> tBeans = action.getTelemedicineBean(patientMID);
	// Default to allow none.
	if (tBeans.size() == 0) {
		TelemedicineBean b = new TelemedicineBean();
		b.setSystolicBloodPressureAllowed(false);
		b.setDiastolicBloodPressureAllowed(false);
		b.setGlucoseLevelAllowed(false);
		b.setHeightAllowed(false);
		b.setWeightAllowed(false);
		b.setPedometerReadingAllowed(false);
		tBeans.add(b);
	}

	/* Update information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");
	
	if (formIsFilled) {
		try {
	boolean heightSubmitted = !(request.getParameter("height") == null || "".equals(request.getParameter("height")));
	boolean weightSubmitted = !(request.getParameter("weight") == null || "".equals(request.getParameter("weight")));
	boolean pedometerSubmitted = !(request.getParameter("pedometerReading") == null ||
									"".equals(request.getParameter("pedometerReading")));
	
	float height = heightSubmitted ? Float.parseFloat(request.getParameter("height")) : -1.0f;
	float weight = weightSubmitted ? Float.parseFloat(request.getParameter("weight")) : -1.0f;
	int pedometer = pedometerSubmitted ? Integer.parseInt(request.getParameter("pedometerReading")) : -1;
	
	RemoteMonitoringDataBean rmdBean = new RemoteMonitoringDataBean();
	rmdBean.setHeight(height);
	rmdBean.setWeight(weight);
	rmdBean.setPedometerReading(pedometer);
	
	action.addRemoteMonitoringData(rmdBean);
	
	loggingAction.logEvent(TransactionType.TELEMEDICINE_DATA_REPORT, loggedInMID, patientMID, "");
%>
		<div align=center>
			<span class="iTrustMessage">Information Successfully Added</span>
		</div>
<%
	} catch (FormValidationException e) {
	formIsFilled = false;
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
			</div>
<%
	} catch(NumberFormatException e) {
	formIsFilled = false;
%>
			<div align=center>
				<span class="iTrustError">Invalid entry: <%=StringEscapeUtils.escapeHtml(e.getMessage())%>. Please enter a whole number.</span>
			</div>
<%
	} catch(ITrustException e) {
	formIsFilled = false;
%>
			<div align=center>
				<span class="iTrustError">Invalid entry: <%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
			</div>
<%	
		}
	}
	if(!formIsFilled) {
		// If conflict exists with multiple HCPs, allow the input.
		boolean reportHeight = false;
		boolean reportWeight = false;
		boolean reportPedometer = false;
		for (TelemedicineBean tb : tBeans) {
			if (tb.isHeightAllowed())
				reportHeight = true;
			if (tb.isWeightAllowed())
				reportWeight = true;
			if (tb.isPedometerReadingAllowed())
				reportPedometer = true;
		}
%>

<form action="addTelemedicineExternalData.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true">
	<input type="hidden" name="reportingFor" value="<%= StringEscapeUtils.escapeHtml("" + ((String)request.getParameter("reportingFor"))) %>">
	<br />
	<table cellspacing=0 align=center cellpadding=0>
		<tr><th>
		<%= StringEscapeUtils.escapeHtml("" + ((patientMID == loggedInMID.longValue()) ? "Self-Reporting":"Report for " + action.getPatientName(patientMID) )) %>
		</th></tr>
	<%
	if (reportHeight || reportWeight || reportPedometer) {
	%>
		<tr>
			<td valign=top>
				<table class="fTable" align=center style="width: 350px;">
				<%
				if (reportHeight) {
				%>
					<tr>
						<td class="subHeaderVertical">Height:</td>
						<td><input name="height" value="" type="text"></td>
					</tr>
				<%
				}
				if (reportWeight) {
				%>
					<tr>
						<td class="subHeaderVertical">Weight:</td>
						<td><input name="weight" value="" type="text"></td>
					</tr>
				<%
				}
				if (reportPedometer) {
				%>
					<tr>
						<td class="subHeaderVertical">Pedometer Reading:</td>
						<td><input name="pedometerReading" value="" type="text"></td>
					</tr>
				<%
				}
				%>
				</table>
			</td>
			<td width="15px">&nbsp;</td>
			<td valign=top></td>
		</tr>
	<%
	}
	%>
	</table>
	<br />
	<div align="center">
	<%
	if (reportHeight || reportWeight || reportPedometer) {
	%>
		<input type="submit" name="action" style="font-weight: bold;" value="Report">
	<%
	}
	else {
	%>
		You are not authorized to report any external telemedicine data.
		<br />
		<input type="submit" name="action" style="font-weight: bold;" value="Report" disabled>
	<%
	}
	%>
		<br />
		<br />
		<br />
		<%
		if(!representativeReporting) {
		%>
		<table class="fTable" align=center>
			<th>Patient Representative Reporting</th>
				<%
					EditRepresentativesAction repsAction = new EditRepresentativesAction(
							prodDAO, 0L, Long.toString(patientMID));
					List<PatientBean> patients = repsAction.getRepresented(patientMID);
					if(patients.size() == 0) {
				%>
						<tr class="subHeader">
							<th>No Patients Represented</th>
						</tr>
				<%
					} else {
				%>
						<tr class="subHeader">
							<th>Patient</th>
						</tr>
				<%
					}
					for(PatientBean p : patients) {
				%>
						<tr><td><a href="#" onclick="javascript:document.getElementsByName('formIsFilled')[0].value='false';document.getElementsByName('reportingFor')[0].value='<%= StringEscapeUtils.escapeHtml("" + (p.getMID())) %>';document.forms[0].submit();"><%= StringEscapeUtils.escapeHtml("" + (p.getFullName() )) %></a></td></tr>
				<%
					}
				%>
		</table>
	</div>
</form>
	<%
		}
	}
	%>
<br />
<br />

<%@include file="/footer.jsp"%>
