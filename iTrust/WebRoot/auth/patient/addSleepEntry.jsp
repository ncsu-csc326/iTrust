<%@page errorPage="auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.AddSleepEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.action.EditSleepEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.action.ViewSleepEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.beans.SleepEntryBean" %>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder" %>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>
<%@page import="java.lang.IllegalArgumentException" %>
<%@page import="java.lang.Double" %>
<%@page import="java.lang.NumberFormatException" %>
<%@page import="java.lang.IllegalArgumentException" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.ncsu.csc.itrust.enums.SleepType" %>

<%@include file="/global.jsp" %>

<%
pageTitle="Add a Sleep Entry";
%>

<%@include file="/header.jsp"%>

<%
	SleepEntryBean newEntry = new SleepEntryBean();
	AddSleepEntryAction addAction = new AddSleepEntryAction(prodDAO, loggedInMID.longValue());
	Date date = new Date();
	String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(date);
	
	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");

	SleepType[] SleepTypes = SleepType.values();
	
	if (formIsFilled) {
		try {
			//can't use BeanBuilder.build because not all are strings
			//so we get all of them in order
			String strDate = request.getParameter("strDate");
			String strType = request.getParameter("strType");
			String strHours = request.getParameter("strHours");
			Long labelEntry = null;
			
			//now make sure none are null (need both checks here)
			if (strDate != null && !strDate.equals("")) {
				newEntry.setStrDate(strDate);
			}
			if (strType != null && !strType.equals("")) {
				newEntry.setSleepType(strType);
			}
			if (strHours != null && !strHours.equals("")) {
				newEntry.setHoursSlept(Double.parseDouble(strHours));
			}
			//Get label information for other entries on that date
			SleepEntryBean labelUpdaterBean = new SleepEntryBean();
			List<SleepEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<SleepEntryBean>();		
			ViewSleepEntryAction viewAction = new ViewSleepEntryAction(prodDAO, loggedInMID.longValue());
			EditSleepEntryAction editAction = new EditSleepEntryAction(prodDAO, loggedInMID.longValue());
			listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(strDate, 
					strDate, loggedInMID.longValue());
			if (listOfEntriesRequiringLabelUpdate.size() > 0) {
				labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(0);
				labelEntry = labelUpdaterBean.getLabelID();
			}
			//now add it to the sleep diary
			addAction.addEntry(newEntry);
			loggingAction.logEvent(TransactionType.SLEEP_DIARY_ADD, 
					loggedInMID.longValue(), loggedInMID.longValue(), "");
			
			//if add was successful, then apply that day's label to all entries
			if (labelEntry != null) {
				listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(strDate, 
						strDate, loggedInMID.longValue());
				for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
					labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(i);
					labelUpdaterBean.setLabelID(labelEntry);
					editAction.editEntry(labelUpdaterBean);
				}
			}
			//iff adding was successful, then send them to their sleep diary
			response.sendRedirect("mySleepDiary.jsp");
			
		} catch (Exception e) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
			</div>
<%
		}
	}
%>

<div align=center>
	<h2>Add a Sleep Entry</h2>

	<form action="addSleepEntry.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<table class="fTable" cellspacing=0 align=center cellpadding=0>
			<tr>
				<td class="subHeaderVertical">Date Slept:</td>
				<td><input id="selectDate" name="strDate" type="text" 
				value="<%=StringEscapeUtils.escapeHtml(dateStr) %>"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Type of Sleep:</td>
				<td><select id="selectType" name="strType">
<% 					for (SleepType st: SleepTypes) {
%>
						<option value="<%= 
							StringEscapeUtils.escapeHtml(st.getName()) %>">
						<%= st.getName() %> </option>
<%
					}
%>
					
				</select>
			</tr>
			<tr>
				<td class="subHeaderVertical">Hours Slept:</td>
				<td><input name="strHours" type="text"> </td>
			</tr>
		</table>
		<br>
		<div align="center">
			<input type="submit" style="font-weight:bold;"
				value="Add entry to Sleep Diary"> 
		</div>
	</form>
	<br>
	<div align="center">
		<a href="mySleepDiary.jsp">Cancel entry and return to your Sleep Diary.</a>
	</div>


</div>

<%@include file="/footer.jsp"%>