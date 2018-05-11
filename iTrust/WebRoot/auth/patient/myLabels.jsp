<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.LabelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewExerciseEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditExerciseEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewSleepEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditSleepEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FoodEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ExerciseEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.SleepEntryBean"%>


<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="java.lang.IllegalArgumentException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>


<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - My Labels";
%>

<%@include file="/header.jsp"%>

<% 
	LabelAction action = new LabelAction(prodDAO, loggedInMID);
	LabelBean newLabelBean = new LabelBean();
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	
	boolean deleteEntry = request.getParameter("deleteEntry") != null
	&& request.getParameter("deleteEntry").equals("true");
	
	boolean editEntry = request.getParameter("editEntry") != null
	&& request.getParameter("editEntry").equals("true");

	boolean formIsFilled = request.getParameter("formIsFilled") != null
		&& request.getParameter("formIsFilled").equals("true");
	
	boolean applyToFood = request.getParameter("foodDiary") != null
			&& request.getParameter("foodDiary").equals("true");

	boolean applyToExercise = request.getParameter("exerciseDiary") != null
			&& request.getParameter("exerciseDiary").equals("true");
	
	boolean applyToSleep = request.getParameter("sleepDiary") != null
			&& request.getParameter("sleepDiary").equals("true");

	if(formIsFilled) {
		try {
			String labelName = request.getParameter("createLabelName");
			String labelColor = request.getParameter("createLabelColor");
		
			if (labelName != null && !labelName.equals("")) {
				newLabelBean.setLabelName(labelName);
			}
			if (labelColor != null && !labelColor.equals("")) {
				newLabelBean.setLabelColor(labelColor);
			}
		
			action.addLabel(newLabelBean);
			loggingAction.logEvent(TransactionType.LABEL_ADD, 
					loggedInMID.longValue(), loggedInMID.longValue(), "");
		} catch (FormValidationException e) {
			%>
						<div align=center>
							<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
						</div>
			<%
					} catch (NumberFormatException d) {
			%>
						<div align=center>
							<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
						</div>
			<%
					} catch (IllegalArgumentException b) {
			%>
						<div align=center>
							<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(b.getMessage())%></span>
						</div>
			<%
					}
	}
			
			String entryID = request.getParameter("entryID"); //entryID for adding/deleteing
			String labelEntryID = request.getParameter("labelEntry"); //entryID for applying labels
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
		try {
			int numDeleted = action.deleteLabel(entryIDLong);
			if (numDeleted == 1) {
		loggingAction.logEvent(TransactionType.DELETE_LABEL, 
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
			}  else if (editEntry) {
				LabelBean editLabel = new LabelBean();
				editLabel.setPatientID(loggedInMID);
				try {
				String labelName = request.getParameter("label");
				String labelColor = request.getParameter("color");
				
				//now make sure none are null (need both checks here)
				if (labelName != null && !labelName.equals("")) {
					editLabel.setLabelName(labelName);
				}
				if (labelColor != null && !labelColor.equals("")) {
					editLabel.setLabelColor(labelColor);
				}
				if (entryID != null && !entryID.equals("")) {
					editLabel.setEntryID(Long.parseLong(entryID));
				}
				String message = "";
				int numUpdated = action.editLabel(editLabel);
				if (numUpdated == 1) {
					loggingAction.logEvent(TransactionType.EDIT_LABEL,
					loggedInMID.longValue(), loggedInMID.longValue(), "");
					message = "Congratulations! You edited your label!";
				} else if (numUpdated == 0) {
					message = "Sorry. No labels were updated.";
				} else if (numUpdated == -1) {
					message = "You cannot edit this label. It does not "
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
			} else if (applyToFood) {
				ViewFoodEntryAction viewAction = new ViewFoodEntryAction(prodDAO, loggedInMID);
				EditFoodEntryAction editAction = new EditFoodEntryAction(prodDAO, loggedInMID.longValue());
				FoodEntryBean applyLabel = new FoodEntryBean();
				List<FoodEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<FoodEntryBean>();			
				listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(startDate, 
						endDate, loggedInMID.longValue());
				int numUpdated = 0;
				try {
					for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
						applyLabel = listOfEntriesRequiringLabelUpdate.get(i);
						applyLabel.setLabelID(Long.parseLong(labelEntryID));
						numUpdated += editAction.editEntry(applyLabel);
					}
				
					String message = "";
					if (numUpdated == listOfEntriesRequiringLabelUpdate.size()) {
						loggingAction.logEvent(TransactionType.EDIT_LABEL,
						loggedInMID.longValue(), loggedInMID.longValue(), "");
						message = "Congratulations! You applied a label to your diary entry!!";
					} else if (numUpdated == 0) {
						message = "Sorry. No labels were updated.";
					} else if (numUpdated == -1) {
						message = "You cannot edit this label. It does not "
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
			} else if (applyToExercise) {
				ViewExerciseEntryAction viewAction = new ViewExerciseEntryAction(prodDAO, loggedInMID);
				EditExerciseEntryAction editAction = new EditExerciseEntryAction(prodDAO, loggedInMID.longValue());
				ExerciseEntryBean applyLabel = new ExerciseEntryBean();
				List<ExerciseEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<ExerciseEntryBean>();			
				listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(startDate, 
						endDate, loggedInMID.longValue());
				int numUpdated = 0;
				try {
					for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
						applyLabel = listOfEntriesRequiringLabelUpdate.get(i);
						applyLabel.setLabelID(Long.parseLong(labelEntryID));
						numUpdated += editAction.editEntry(applyLabel);
					}
				
					String message = "";
					if (numUpdated == listOfEntriesRequiringLabelUpdate.size()) {
						loggingAction.logEvent(TransactionType.EDIT_LABEL,
						loggedInMID.longValue(), loggedInMID.longValue(), "");
						message = "Congratulations! You applied a label to your diary entry!!";
					} else if (numUpdated == 0) {
						message = "Sorry. No labels were updated.";
					} else if (numUpdated == -1) {
						message = "You cannot edit this label. It does not "
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
			} else if (applyToSleep) {
				ViewSleepEntryAction viewAction = new ViewSleepEntryAction(prodDAO, loggedInMID);
				EditSleepEntryAction editAction = new EditSleepEntryAction(prodDAO, loggedInMID.longValue());
				SleepEntryBean applyLabel = new SleepEntryBean();
				List<SleepEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<SleepEntryBean>();			
				listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(startDate, 
						endDate, loggedInMID.longValue());
				int numUpdated = 0;
				try {
					for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
						applyLabel = listOfEntriesRequiringLabelUpdate.get(i);
						applyLabel.setLabelID(Long.parseLong(labelEntryID));
						numUpdated += editAction.editEntry(applyLabel);
					}
				
					String message = "";
					if (numUpdated == listOfEntriesRequiringLabelUpdate.size()) {
						loggingAction.logEvent(TransactionType.EDIT_LABEL,
						loggedInMID.longValue(), loggedInMID.longValue(), "");
						message = "Congratulations! You applied a label to your diary entry!!";
					} else if (numUpdated == 0) {
						message = "Sorry. No labels were updated.";
					} else if (numUpdated == -1) {
						message = "You cannot edit this label. It does not "
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
	%>
	
<div align=center> 
<%
	List<LabelBean> labels = action.getLabels(loggedInMID);
%>
	<h3>My Labels</h3>
	<%
		if (labels.size() > 0 ) {
	%>
	<div style="margin-left: 5px;">
		<form id="beanForm" action="myLabels.jsp" method="post">
			<input type="hidden" id="beanID" name="entryID" value="-1"> <input
				type="hidden" id="deleteInput" name="deleteEntry" value="false">
			<input type="hidden" id="editInput" name="editEntry" value="false">
			<input type="hidden" id="labelID" name="label" value="">
			<input type="hidden" id="colorID" name="color" value="">
			<table class="fTable" border=1 align="center">
				<tr>
					<th></th>
					<th>Label Name</th>
					<th>Label Color</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
				<%
					int index = 0;
														for(LabelBean b : labels) {
				%>
				<tr id="<%=StringEscapeUtils.escapeHtml("tableRow" + index)%>">

					<td><input type="button" value="Submit"
						onclick="submitEdit(<%=StringEscapeUtils.escapeHtml("" + b.getEntryID())%>, 
								<%=StringEscapeUtils.escapeHtml("" + index)%>)"
						hidden id="<%=StringEscapeUtils.escapeHtml("SubmitID:" + index)%>">
					</td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getLabelName()))%>"
						disabled size="20"
						name="<%=StringEscapeUtils.escapeHtml("Label:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("LabelID:" + index)%>">
					</td>

					<td><input type="color"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getLabelColor()))%>"
						disabled
						name="<%=StringEscapeUtils.escapeHtml("Color:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("ColorID:" + index)%>"></td>

					<td><input type="button" value="Edit Entry"
						onclick="editBean(<%=StringEscapeUtils.escapeHtml("" + index)%>)"></td>
						
					<td><input type="button" value="Delete Label"
						onclick="deleteBean(<%=StringEscapeUtils.escapeHtml("" + b.getEntryID())%>,
									<%=StringEscapeUtils.escapeHtml("" + index)%>)"></td>
				</tr>
				<%
					index ++;
				%>
				<%
														}
				} else {
			// There are no entries at all.
				%>
					<div>
					<i>You have no Labels.</i>
					</div>
				<%
				}
				%>
			</table>
	
	
	<h3>Create a Label</h3>
	<form action="myLabels.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<input type="text" id="createLabelNameID" name="createLabelName" placeholder="Label Name"> 
		<input type="color" value="#FFFFFF" id="createLabelColorID" name="createLabelColor"> 
		<input type="submit" value="Create Label">
	</form>
		
	<h3>Add a Label to your Diaries</h3>
	Select a diary:
	<select name="diaries" id="diaries">
		<option value="food">Food</option>>
		<option value="exercise">Exercise</option>>
		<option value="sleep">Sleep</option>>
	</select>
	<br>
	Select a label:
	<select name="labels" id="labels">
<%
		for (LabelBean label : labels) {
%>
		<option value="<%=label.getEntryID()%>">
			<%=label.getLabelName()%> <!-- name displayed in drop down box -->
		</option>
<%
		}
%>
	</select>
	
	<form id="filterForm" action="myLabels.jsp" method="post">
		<input type="hidden" id="foodDiaryID" name="foodDiary" value="false">
		<input type="hidden" id="exerciseDiaryID" name="exerciseDiary" value="false">
		<input type="hidden" id="sleepDiaryID" name="sleepDiary" value="false">
		<input type="hidden" id="labelEntryID" name="labelEntry" value="false">
		<table>
			<tr class="subHeader">
				<td>Start Date:</td>
				<td><input name="startDate"
					value="<%=StringEscapeUtils.escapeHtml((startDate == null ? "" : startDate))%>"
					size="10"> <input type="button" value="Select Date"
					onclick="displayDatePicker('startDate');"></td>
				<td>End Date:</td>
				<td><input name="endDate"
					value="<%=StringEscapeUtils.escapeHtml((endDate == null ? "" : endDate))%>"
					size="10"> <input type="button" value="Select Date"
					onclick="displayDatePicker('endDate');"></td>
				<td><input type="button" name="btn_filter"
					value="Apply Label" onclick="applyLabel()"></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
function deleteBean(entry, rowIndex) {
	var row = "tableRow";
	row = row.concat(rowIndex);
	var tableRow = document.getElementById(row);
	var background = tableRow.style.backgroundColor;
	var msg = "Are you sure you want to delete this label?";
	tableRow.style.backgroundColor = "red";
	var r = confirm(msg);
	if (r == true) { //only submit the form if they confirm
		document.getElementById("deleteInput").value="true";
		//document.getElementById("editInput").value="false";
		document.getElementById("beanID").value=entry;
		document.getElementById("beanForm").submit();
	} else {
		tableRow.style.backgroundColor = background;
	}
	
}

function editBean(rowIndex) {
	var ids = ["LabelID:", "ColorID:"]
	var buttonName = "SubmitID:".concat(rowIndex);
	document.getElementById(buttonName).hidden=false;
	for (i = 0; i < 2; i++) {
		var inputID = ids[i].concat(rowIndex);
		document.getElementById(inputID).disabled=false;
	}
	document.getElementById("editInput").value="true";
	
}

function submitEdit(entry, rowIndex) {
	//have an array of all the different ids
	//(not the best solution at all, but I can't think
	//of anything better at this time)
	var formIDs = ["labelID", "colorID"];
	
	var ids = ["LabelID:", "ColorID:"]
	
	for (i = 0; i < 2; i++) {
		var inputID = ids[i].concat(rowIndex);
		var formParam = document.getElementById(formIDs[i]);
		formParam.value = document.getElementById(inputID).value;
	}
	document.getElementById("deleteInput").value = "false";
	document.getElementById("editInput").value = "true";
	document.getElementById("beanID").value = entry;
	document.getElementById("beanForm").submit();
}

function applyLabel() {
	switch (document.getElementById("diaries").value) {
	case "food":
		document.getElementById("foodDiaryID").value = "true";
		break;
	case "exercise":
		document.getElementById("exerciseDiaryID").value = "true";
		break;
	case "sleep":
		document.getElementById("sleepDiaryID").value = "true";
		break;
	}
	var labelSelector = document.getElementById("labels");
	var labelValue = labelSelector.options[labelSelector.selectedIndex].value;
	document.getElementById("labelEntryID").value = labelValue;
	document.getElementById("filterForm").submit();
}
</script>

<%@include file="/footer.jsp"%>
