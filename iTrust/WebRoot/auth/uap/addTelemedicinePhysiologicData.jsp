<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.TelemedicineBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddRemoteMonitoringDataAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%> 

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Report Status";
%>

<%@include file="/header.jsp"%>

<%
	String switchString = "";
if (request.getParameter("switch") != null) {
	switchString = request.getParameter("switch");
}

String patientString = "";
if (request.getParameter("patient") != null) {
	patientString = request.getParameter("patient");
}

String pidString;
if (switchString.equals("true"))
	pidString = "";
else if (!patientString.equals("")) {
	int patientIndex = Integer.parseInt(patientString);
	List<Long> patients = (List<Long>) session.getAttribute("patients");
	pidString = "" + patients.get(patientIndex);
	session.removeAttribute("patients");
	session.removeAttribute("patient");
	session.setAttribute("pid", pidString);
} else
	pidString = (String)session.getAttribute("pid");

if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("../uap/getPatientMonitorList.jsp?forward=addTelemedicinePhysiologicData.jsp");
   	return;
}
	AddRemoteMonitoringDataAction action = new AddRemoteMonitoringDataAction(
	prodDAO, loggedInMID.longValue(),Long.parseLong(pidString));
	
	List<TelemedicineBean> tBeans = action.getTelemedicineBean(Long.parseLong(pidString));
	// Default to allow none.
	if (tBeans.size() == 0) {
		TelemedicineBean b = new TelemedicineBean();
		b.setSystolicBloodPressureAllowed(false);
		b.setDiastolicBloodPressureAllowed(false);
		b.setGlucoseLevelAllowed(false);
		tBeans.add(b);
	}

	/* Update information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");
	
	if (formIsFilled) {
		try {
	boolean glucoseSubmitted = !(request.getParameter("glucoseLevel") == null
			|| "".equals(request.getParameter("glucoseLevel")));
	boolean systolicSubmitted = !(request.getParameter("systolicBloodPressure") == null
			|| "".equals(request.getParameter("systolicBloodPressure")));
	boolean diastolicSubmitted = !(request.getParameter("diastolicBloodPressure") == null
			|| "".equals(request.getParameter("diastolicBloodPressure")));
	
	if (diastolicSubmitted != systolicSubmitted) {
%>
				<div align=center>
					<span class="iTrustError">Systolic and Diastolic Blood Pressure must be submitted together.</span>
				</div>
				<%
					} else {
						int glucoseLevel = glucoseSubmitted ? Integer.parseInt(request.getParameter("glucoseLevel")) : -1;
						int systolicBP = systolicSubmitted ? Integer.parseInt(request.getParameter("systolicBloodPressure")) : -1;
						int diastolicBP = diastolicSubmitted ? Integer.parseInt(request.getParameter("diastolicBloodPressure")) : -1;
						
						RemoteMonitoringDataBean rmdBean = new RemoteMonitoringDataBean();
						rmdBean.setGlucoseLevel(glucoseLevel);
						rmdBean.setSystolicBloodPressure(systolicBP);
						rmdBean.setDiastolicBloodPressure(diastolicBP);
						
						action.addRemoteMonitoringData(rmdBean);
						loggingAction.logEvent(TransactionType.TELEMEDICINE_DATA_REPORT, loggedInMID, Long.parseLong(pidString), "");
				%>
			<div align=center>
				<span class="iTrustMessage">Information Successfully Added</span>
			</div>
	<%
		//session.removeAttribute("pid");
		}
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
		boolean reportBloodPressure = false;
		boolean reportGlucose = false;
		for (TelemedicineBean tb : tBeans) {
			if (tb.isSystolicBloodPressureAllowed() && tb.isDiastolicBloodPressureAllowed())
				reportBloodPressure = true;
			if (tb.isGlucoseLevelAllowed())
				reportGlucose = true;
		}
%>

<form action="addTelemedicinePhysiologicData.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true">
<br />
<table cellspacing=0 align=center cellpadding=0>
	<tr>
	<%
	if (reportBloodPressure || reportGlucose) {
	%>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
		<%
		if (reportBloodPressure) {
		%>
			<tr>
				<td class="subHeaderVertical">Systolic Blood Pressure:</td>
				<td><input name="systolicBloodPressure" value="" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Diastolic Blood Pressure:</td>
				<td><input name="diastolicBloodPressure" value="" type="text"></td>
			</tr>
		<%
		}
		if (reportGlucose) {
		%>
			<tr>
				<td class="subHeaderVertical">Glucose Level:</td>
				<td><input name="glucoseLevel" value="" type="text"></td>
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
if (reportBloodPressure || reportGlucose) {
%>
	<input type="submit" name="action" style="font-weight: bold;" value="Report">
<%
} else {
%>
	You are not authorized to report any physiologic telemedicine data.
	<br />
	<input type="submit" name="action" style="font-weight: bold;" value="Report" disabled>
<%
}}
%>
<br />
<br />
</div>
</form>

<%@include file="/footer.jsp"%>
