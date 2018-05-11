<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TelemedicineBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
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
	response.sendRedirect("../uap/getPatientMonitorList.jsp?forward=addTelemedicineExternalData.jsp");
   	return;
}
	AddRemoteMonitoringDataAction action = new AddRemoteMonitoringDataAction(
	prodDAO, loggedInMID.longValue(),Long.parseLong(pidString));
	
	List<TelemedicineBean> tBeans = action.getTelemedicineBean(Long.parseLong(pidString));
	// Default to allow none.
	if (tBeans.size() == 0) {
		TelemedicineBean b = new TelemedicineBean();
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
	boolean pedometerSubmitted = !(request.getParameter("pedometerReading") == null
									|| "".equals(request.getParameter("pedometerReading")));
	
	float height = heightSubmitted ? Float.parseFloat(request.getParameter("height")) : -1.0f;
	float weight = weightSubmitted ? Float.parseFloat(request.getParameter("weight")) : -1.0f;
	int pedometer = pedometerSubmitted ? Integer.parseInt(request.getParameter("pedometerReading")) : -1;
	
	RemoteMonitoringDataBean rmdBean = new RemoteMonitoringDataBean();
	rmdBean.setHeight(height);
	rmdBean.setWeight(weight);
	rmdBean.setPedometerReading(pedometer);
	
	action.addRemoteMonitoringData(rmdBean);
	loggingAction.logEvent(TransactionType.TELEMEDICINE_DATA_REPORT, loggedInMID, Long.parseLong(pidString), "");
%>
		<div align=center>
			<span class="iTrustMessage">Information Successfully Added</span>
		</div>
<%
	//session.removeAttribute("pid");
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
	<tr>
	<%
	if (reportHeight || reportWeight || reportPedometer) {
	%>
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
		<td valign=top>
		
		</td>
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
<%
}
%>
<br />
<br />
</div>
</form>

<%@include file="/footer.jsp"%>
