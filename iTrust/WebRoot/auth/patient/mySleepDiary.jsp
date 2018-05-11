<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.SleepEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewSleepEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditSleepEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.DeleteSleepEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabelAction"%>
<%@page import="edu.ncsu.csc.itrust.enums.SleepType"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="java.lang.IllegalArgumentException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>


<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Sleep Diary";
%>

<%@include file="/header.jsp"%>

<div align=center>
	<h2>My Sleep Diary</h2>
	<%
		Date today = new Date();
			
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");

			// If they only gave us one date, use that for the range.
			if (startDate != null && (endDate == null || endDate.isEmpty())) {
				endDate = startDate;
			} else if (endDate != null && (startDate == null || startDate.isEmpty())) {
				startDate = endDate;
			}
			
			List<SleepEntryBean> sleepBeans = new ArrayList<SleepEntryBean>();
			List<SleepEntryBean> sleepBeansTotals = new ArrayList<SleepEntryBean>();
			// Create a new action for viewing all of the sleep diary stuff
			ViewSleepEntryAction action = new ViewSleepEntryAction(prodDAO, loggedInMID.longValue());
			LabelAction labelAction = new LabelAction(prodDAO, loggedInMID);
			List<LabelBean> labelBeans = new ArrayList<LabelBean>();
			labelBeans = labelAction.getLabels(loggedInMID);
			
			boolean deleteEntry = request.getParameter("deleteEntry") != null
			&& request.getParameter("deleteEntry").equals("true");
			
			boolean editEntry = request.getParameter("editEntry") != null
			&& request.getParameter("editEntry").equals("true");
			
			String entryID = request.getParameter("entryID");
			long entryIDLong = -1; //initialize to invalid entryID
			if (entryID != null && !entryID.equals("")) {
		try {
			entryIDLong = Long.parseLong(entryID);
		} catch (NumberFormatException d) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
	</div>
	<%
		}
			}
			if (deleteEntry) {
				DeleteSleepEntryAction deleteAction = new DeleteSleepEntryAction(prodDAO, loggedInMID.longValue());
				try {
					int numDeleted = deleteAction.deleteEntry(entryIDLong);
					if (numDeleted == 1) {
						loggingAction.logEvent(TransactionType.DELETE_SLEEP_ENTRY, 
						loggedInMID.longValue(), loggedInMID.longValue(), "");
					} else if (numDeleted == 0) {
	%>
	<div align=center>
		<span> <%=StringEscapeUtils.escapeHtml("No entries deleted")%></span>
	</div>
	<%
					}
				} catch (ITrustException d) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
	</div>
	<%
				}
			} else if (editEntry) {
				EditSleepEntryAction editAction = new EditSleepEntryAction(prodDAO, loggedInMID.longValue());
				SleepEntryBean newEntry = new SleepEntryBean();
				newEntry.setEntryID(entryIDLong);
				newEntry.setPatientID(loggedInMID.longValue());
				try {
				//can't use BeanBuilder.build because not all are strings
				//so we get all of them in order
				String labelEntry = request.getParameter("labelEntry");
				String strDate = request.getParameter("strDate");
				String strType = request.getParameter("strType");
				String strHours = request.getParameter("strHours");
				
				//now make sure none are null (need both checks here)
				if (labelEntry != null && !labelEntry.equals("")) {
					newEntry.setLabelID(Long.parseLong(labelEntry));
				}
				if (strDate != null && !strDate.equals("")) {
					newEntry.setStrDate(strDate);
				}
				if (strType != null && !strType.equals("")) {
					newEntry.setSleepType(strType);
				}
				if (strHours != null && !strHours.equals("")) {
					newEntry.setHoursSlept(Double.parseDouble(strHours));
				}
				
				String message = "";
				int numUpdated = editAction.editEntry(newEntry);
				if (numUpdated == 1) {
					//if editing an entry is successful, change label for all entries on that day
					SleepEntryBean labelUpdaterBean = new SleepEntryBean();
					List<SleepEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<SleepEntryBean>();			
					listOfEntriesRequiringLabelUpdate = action.getBoundedDiary(strDate, 
							strDate, loggedInMID.longValue());
					for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
						labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(i);
						try {
							labelUpdaterBean.setLabelID(Long.parseLong(labelEntry));
						} catch (NumberFormatException nfe) {
							continue;
						}
						editAction.editEntry(labelUpdaterBean);
					}
					
					loggingAction.logEvent(TransactionType.PATIENT_EDIT_SLEEP_ENTRY,
					loggedInMID.longValue(), loggedInMID.longValue(), "");
					message = "Congratulations! You edited your sleep entry for the date " 
					+ newEntry.getStrDate() + ".";
				} else if (numUpdated == 0) {
					message = "Sorry. No sleep entries were updated.";
				} else if (numUpdated == -1) {
					message = "You cannot edit this sleep entry. It does not "
					+ "belong to you.";
				}
	%>
	<div align=center>
		<span><%=StringEscapeUtils.escapeHtml(message)%></span>
	</div>
	<%
		} catch (Exception e) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
	</div>
	<%
		}
		}
			SleepType[] SleepTypes = SleepType.values();
			loggingAction.logEvent(TransactionType.PATIENT_VIEW_SLEEP_DIARY, 
			loggedInMID.longValue(), loggedInMID.longValue(), "");

			// Are we viewing within a specific range?
			boolean dateRange = startDate != null && endDate != null;
			
			try {
		if (dateRange) {
			sleepBeans = action.getBoundedDiary(request.getParameter("startDate"), request.getParameter("endDate"), loggedInMID.longValue());
			sleepBeansTotals = action.getBoundedDiaryTotals(request.getParameter("startDate"), request.getParameter("endDate"), loggedInMID.longValue());
		} else {
			sleepBeans = action.getDiary(loggedInMID.longValue());
			sleepBeansTotals = action.getDiaryTotals(loggedInMID.longValue());
		}
			} catch(Exception e) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
	</div>
	<%
		}
	%>
	<form id="filterForm" action="mySleepDiary.jsp" method="post">
		<table>
			<tr class="subHeader">
				<td>Start Date:</td>
				<td><input onchange="singleDate();" name="startDate" id="startDate"
					value="<%=StringEscapeUtils.escapeHtml((startDate == null ? "" : startDate))%>"
					size="10"> <input type="button" value="Select Date"
					onclick="displayDatePicker('startDate');"></td>
				<td>End Date:</td>
				<td><input onchange="singleDate();" name="endDate" id="endDate"
					value="<%=StringEscapeUtils.escapeHtml((endDate == null ? "" : endDate))%>"
					size="10">
					<input type="button" value="Select Date"
					onclick="displayDatePicker('endDate');"></td>
				<td><input onclick="singleDate();" type="submit" name="btn_filter"
					value="Filter Entries"></td>
			</tr>
		</table>
	</form>
	<br />
	<%
		if (sleepBeans.size() > 0 && sleepBeansTotals.size() > 0) {
	%>
	<div style="margin-left: 5px;">
		<!-- Lower-case IDs are for edits. Each line's ACTUAL ID starts with an upper-case letter. -->
		<form id="beanForm" action="mySleepDiary.jsp" method="post">
			<input type="hidden" id="beanID" name="entryID" value="-1"> <input
				type="hidden" id="deleteInput" name="deleteEntry" value="false">
			<input type="hidden" id="editInput" name="editEntry" value="false">
			<input type="hidden" id="labelEntryID" name="labelEntry" value="">
			<input type="hidden" id="dateID" name="strDate" value="">
			<input type="hidden" id="sleepTypeID" name="strType" value="">
			<input type="hidden" id="hoursID" name="strHours" value="">
			<table class="fTable" border=1 align="center">
				<tr>
					<th></th>
					<th>Label</th>
					<th>Date</th>
					<th>Type of Sleep</th>
					<th>Hours Slept</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
				<%
					int index = 0;
					for(SleepEntryBean b : sleepBeans) {
				%>
				<tr id="<%=StringEscapeUtils.escapeHtml("tableRow" + index)%>">

					<td><input type="button" value="Submit"
						onclick="submitEdit(<%=StringEscapeUtils.escapeHtml("" + b.getEntryID())%>, 
								<%=StringEscapeUtils.escapeHtml("" + index)%>)"
						hidden id="<%=StringEscapeUtils.escapeHtml("SubmitID:" + index)%>">
					</td>
				<%
				String color = "";
				for (LabelBean label : labelBeans) {
					if (label.getEntryID() == b.getLabelID()) {
						color = label.getLabelColor();
					}
				}
				%>	
					<td bgcolor=<%=color %>><select disabled
						name="<%=StringEscapeUtils.escapeHtml("LabelEntry:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("LabelEntryID:" + index)%>">
							<option value=0>None</option>
							<%
								for (LabelBean label : labelBeans) {
							%>
							<option value="<%=label.getEntryID()%>"
								<%if (label.getEntryID() == b.getLabelID()) {%>
								selected <%}%>>
								<%=label.getLabelName()%>
							</option>
							<%
								}
							%>

					</select></td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getStrDate()))%>"
						disabled size="8"
						name="<%=StringEscapeUtils.escapeHtml("Date:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("DateID:" + index)%>">
					</td>
					
					<td><select disabled
						name="<%=StringEscapeUtils.escapeHtml("SleepType:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("SleepTypeID:" + index)%>">
							<option value="">Select:</option>
							<%
							for (SleepType st: SleepTypes) {
							%>
								<option value="<%=StringEscapeUtils.escapeHtml(st.getName())%>"
									<%if (st.getName().equals(b.getSleepType().getName())) {%>
									selected <%}%>>
									<%=st.getName()%>
								</option>
							<%
							}
							%>
					</select></td>
					
					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getHoursSlept()))%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Hours:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("HoursID:" + index)%>">
					</td>

					<td><input type="button" value="Edit Entry"
						onclick="editBean(<%=StringEscapeUtils.escapeHtml("" + index)%>)">
					</td>
					<td><input type="button" value="Delete Entry"
						onclick="deleteBean(<%=StringEscapeUtils.escapeHtml("" + b.getEntryID())%>,
									<%=StringEscapeUtils.escapeHtml("" + index)%>)">
					</td>
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
				counter ++;
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
		<i>You have no entries in the specified range.</i>
	</div>
	<%
		} else {
			// There are no entries at all.
	%>
	<div>
		<i>You have no Sleep Diary entries.</i>
	</div>
	<%
		}
	%>
	<br /> <br /> <a href="/iTrust/auth/patient/addSleepEntry.jsp">Add
		an entry to your Sleep Diary.</a>
</div>
<script type="text/javascript">

function deleteBean(entry, rowIndex) {
	var row = "tableRow";
	row = row.concat(rowIndex);
	var tableRow = document.getElementById(row);
	var background = tableRow.style.backgroundColor;
	var msg = "Are you sure you want to delete this entry?";
	tableRow.style.backgroundColor = "red";
	var r = confirm(msg);
	if (r == true) { //only submit the form if they confirm
		document.getElementById("deleteInput").value="true";
		document.getElementById("editInput").value="false";
		document.getElementById("beanID").value=entry;
		document.getElementById("beanForm").submit();
	} else {
		tableRow.style.backgroundColor = background;
	}
}

function singleDate() {
	var start = document.getElementById("startDate");
	var end = document.getElementById("endDate");
	
	if (start.value != null && (end.value == null || end.value == "")) {
		end.value = start.value;
	} else if (end.value != null && (start.value == null || start.value == "")) {
		start.value = end.value;
	}
}

function editBean(rowIndex) {
	var ids = ["LabelEntryID:", "DateID:", "SleepTypeID:", "HoursID:"];
	var buttonName = "SubmitID:".concat(rowIndex);
	document.getElementById(buttonName).hidden = false;
	
	for (i = 0; i < 4; i++) {
		var inputID = ids[i].concat(rowIndex);
		document.getElementById(inputID).disabled = false;
	}
	
	document.getElementById("editInput").value="true";
}

function submitEdit(entry, rowIndex) {
	var formIDs = ["labelEntryID", "dateID", "sleepTypeID", "hoursID"];
	var ids = ["LabelEntryID:", "DateID:", "SleepTypeID:", "HoursID:"];
	
	for (i = 0; i < 4; i++) {
		var inputID = ids[i].concat(rowIndex);
		var formParam = document.getElementById(formIDs[i]);
		formParam.value = document.getElementById(inputID).value;
	}
	
	document.getElementById("deleteInput").value = "false";
	document.getElementById("editInput").value = "true";
	document.getElementById("beanID").value = entry;
	document.getElementById("beanForm").submit();
}

</script>

<%@include file="/footer.jsp"%>
