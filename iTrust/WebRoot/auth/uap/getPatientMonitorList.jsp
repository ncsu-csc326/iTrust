<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.TelemedicineBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddRemoteMonitoringDataAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Monitored Patients";
loggingAction.logEvent(TransactionType.PATIENT_LIST_VIEW, loggedInMID.longValue(), 0, "Viewed monitored patients");
%>

<%@include file="/header.jsp" %>

<%
ViewMyRemoteMonitoringListAction action = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> data = action.getPatientDataWithoutLogging();
%>
<br />

<form action="getPatientMonitorList.jsp" method="post" name="myform">
	<table class="fTable" align="center">
		<tr>
			<th colspan="3">Patients</th>
		</tr>
		<tr class="subHeader">
			<th>Patient</th>
		</tr>
		<%
			List<Long> patients = new ArrayList<Long>();
			int index = 0;
			List<TelemedicineBean> tBeans;
			boolean isMonitored = false;
			AddRemoteMonitoringDataAction rmdAction = new AddRemoteMonitoringDataAction(prodDAO, loggedInMID.longValue(), 1);
			for (RemoteMonitoringDataBean bean : data) {
				if (!patients.contains(bean.getPatientMID())){
					tBeans = rmdAction.getTelemedicineBean(bean.getPatientMID());
					isMonitored = false;
					for (TelemedicineBean tb : tBeans) {
						if (tb.isDiastolicBloodPressureAllowed()
								|| tb.isGlucoseLevelAllowed()
								|| tb.isHeightAllowed()
								|| tb.isPedometerReadingAllowed()
								|| tb.isSystolicBloodPressureAllowed()
								|| tb.isWeightAllowed()) {
							isMonitored = true;
							break;
						}
					}
					if (isMonitored) {
						patients.add(bean.getPatientMID());
		%>
		<tr>
			<td >
				<a href="<%=request.getParameter("forward") %>?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">
					<%= StringEscapeUtils.escapeHtml("" + (action.getPatientName(bean.getPatientMID()))) %>	
				</a>
				</td>
		</tr>
		<%
					index++;
					}
				}
			}
			session.setAttribute("patients", patients);
		%>
	</table>
</form>
<br />
<br />

<%@include file="/footer.jsp" %>
