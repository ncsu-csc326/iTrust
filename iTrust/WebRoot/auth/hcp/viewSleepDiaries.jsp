<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewSleepEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.RequiredProceduresBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.SleepEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.SleepType"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.AuthDAO"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Patient Sleep Diaries";
%>

<%@include file="/header.jsp"%>

<itrust:patientNav thisTitle="Sleep Diaries" />

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/viewSleepDiaries.jsp");
		return;
	}
	/* Get a patient's respective sleep diary */
	long pid = Long.parseLong(pidString);
	String PatientName = authDAO.getUserName(pid);
	ViewSleepEntryAction action = new ViewSleepEntryAction(prodDAO,
	loggedInMID);
	LabelAction labelAction = new LabelAction(prodDAO, loggedInMID);
	LabelBean labelBean = new LabelBean();
	
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");

	// If they only gave us one date, use that for the range.
	if (startDate != null && (endDate == null || endDate.isEmpty())) {
		endDate = startDate;
	} else if (endDate != null && (startDate == null || startDate.isEmpty())) {
		startDate = endDate;
	}
	
	// Are we viewing within a specific range?
	boolean dateRange = startDate != null && endDate != null;

	List<SleepEntryBean> sleepBeans = new ArrayList<SleepEntryBean>();
	List<SleepEntryBean> sleepBeansTotals = new ArrayList<SleepEntryBean>();
	try {
		if (dateRange) {
			sleepBeans = action.getBoundedDiary(
			request.getParameter("startDate"),
			request.getParameter("endDate"), pid);
			sleepBeansTotals = action.getBoundedDiaryTotals(
			request.getParameter("startDate"),
			request.getParameter("endDate"), pid);
		} else {
			sleepBeans = action.getDiary(pid);
			sleepBeansTotals = action.getDiaryTotals(pid);
		}
		loggingAction.logEvent(TransactionType.HCP_VIEW_SLEEP_DIARY,
		loggedInMID.longValue(), pid, "");
	} catch (Exception e) {
%>
<div align=center>
	<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
</div>
<%
	}
%>

<div align="center">
<br />
<form id="filterForm" action="viewSleepDiaries.jsp" method="post">
	<table>
		<tr class="subHeader">
			<td>Start Date:</td>
			<td><input onchange="singleDate();" name="startDate" id="startDate"
				value="<%=StringEscapeUtils.escapeHtml((startDate == null ? "" : startDate))%>"
				size="10"> <input type="button" value="Select Date"
				onclick="displayDatePicker('startDate');"></td>
			<td>End Date:</td>
			<td><input onchange="singleDate();" name="endDate" id="endDate"
				value="<%=StringEscapeUtils.escapeHtml((endDate == null ? "" : endDate))%>">
				<input type="button" value="Select Date"
				onclick="displayDatePicker('endDate');"></td>
			<td><input onclick="singleDate();" type="submit" name="btn_filter"
				value="Filter Entries"></td>
		</tr>
	</table>
</form>
	<br>
	<h2><%=selectedPatientName + "'s Sleep Diary"%></h2>
	<%
		if (sleepBeans.size() > 0 && sleepBeansTotals.size() > 0) {
	%>
	<div style="margin-left: 5px;">
		<!-- Lower-case IDs are for edits. Each line's ACTUAL ID starts with an upper-case letter. -->
		<form id="beanForm" action="viewSleepDiary.jsp" method="post">
			<input type="hidden" id="beanID" name="entryID" value="-1"> <input
				type="hidden" id="deleteInput" name="deleteEntry" value="false">
			<input type="hidden" id="editInput" name="editEntry" value="false">
			<input type="hidden" id="dateID" name="strDate" value="">
			<input type="hidden" id="sleepTypeID" name="strType" value="">
			<input type="hidden" id="hoursID" name="strHours" value="">
			<table class="fTable" border=1 align="center">
				<tr>
					<th>Label</th>
					<th>Date</th>
					<th>Type of Rest</th>
					<th>Hours Slept</th>
				</tr>
				<%
					int index = 0;
					for(SleepEntryBean b : sleepBeans) {
						labelBean = labelAction.getLabel(b.getLabelID());
				%>
				<tr id="<%=StringEscapeUtils.escapeHtml("tableRow" + index)%>">

					<td><%=StringEscapeUtils.escapeHtml(""
							+ (labelBean != null ? labelBean.getLabelName() : "None")) %></td>
					
					<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getStrDate()))%></td>
					
					<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getSleepType().getName()))%></td>
					
					<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getHoursSlept()))%></td>
				</tr>
				<%
					index ++;
				%>
				<%
					}
				%>
			</table>
		</form>
		<br>
		<hr>
		<table class="fTable" border=1 align="center">
			<caption>
				<h2>Daily Totals</h2>
			</caption>
			<tr>
				<th>Date</th>
				<th>Total Hours</th>
			</tr>
			<%
				int counter = 0;
				for(SleepEntryBean b : sleepBeansTotals) {
			%>
			<td><%=StringEscapeUtils.escapeHtml("" + (b.getStrDate()))%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + (b.getHoursSlept()))%></td>
			</tr>
			<%
				counter++;
			%>
			<%
				}
			%>
		</table>

	</div>
	<%
		} else if (dateRange) {
			// There are entries because the range parameters obscures them.
	%>
	<div>
		<i>There are no entries in the specified range.</i>
	</div>
	<%
		} else {
	%>
	<div>
		<i>The selected patient's sleep diary is empty. If you were expecting entries please try again later!</i>
	</div>
	<%
		}
	%>
	<br /> <a
		href="/iTrust/auth/getPatientID.jsp?forward=
		/iTrust/auth/hcp/viewSleepDiaries.jsp">
		Select a different patient's Sleep Diary</a>
</div>

<script type="text/javascript">
function singleDate() {
	var start = document.getElementById("startDate");
	var end = document.getElementById("endDate");
	
	if (start.value != null && (end.value == null || end.value == "")) {
		end.value = start.value;
	} else if (end.value != null && (start.value == null || start.value == "")) {
		start.value = end.value;
	}
}
</script>

<%@include file="/footer.jsp"%>
