<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.EditMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TelemedicineBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Patient List";
%>

<%@include file="/header.jsp" %>

<%
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
String addOrRemove = "Add";
if (pidString == null || 1 > pidString.length() || "false".equals(request.getParameter("confirmAction"))) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/editPatientList.jsp");
   	return;
}
	
EditMonitoringListAction action = new EditMonitoringListAction(prodDAO,loggedInMID.longValue());
long pid = Long.parseLong(pidString);
String patientName = action.getPatientName(pid);
if (action.isPatientInList(pid)) {
	addOrRemove = "Remove";
}
String confirm = "";
boolean conf_bool = false;

if ("true".equals(request.getParameter("confirmAction"))) {
	if (request.getParameter("fSubmit").equals("Update Permissions")) {
		String[] checkBoxes = new String[] {
				request.getParameter("bloodPressure"),
				request.getParameter("glucose"),
				request.getParameter("height"),
				request.getParameter("weight"),
				request.getParameter("pedometer")};
		boolean[] permissions = new boolean[5];
		for (int i = 0; i < 5; i++) {
			permissions[i] = checkBoxes[i] != null && checkBoxes[i].equals("on");
		}
		
		TelemedicineBean tBean = new TelemedicineBean();
		tBean.setSystolicBloodPressureAllowed(permissions[0]);
		tBean.setDiastolicBloodPressureAllowed(permissions[0]);
		tBean.setGlucoseLevelAllowed(permissions[1]);
		tBean.setHeightAllowed(permissions[2]);
		tBean.setWeightAllowed(permissions[3]);
		tBean.setPedometerReadingAllowed(permissions[4]);
		
		conf_bool = action.removeFromList(pid);
		boolean conf_bool2 = action.addToList(pid, tBean);
		if(conf_bool && conf_bool2) {
			confirm = patientName + "'s Permissions Changed";
			loggingAction.logEvent(TransactionType.PATIENT_LIST_EDIT, loggedInMID, pid, "");
		}
		session.removeAttribute("pid");
	} else if (request.getParameter("fSubmit").contains("Telemedicine Permissions")) {
%>
<form action="editPatientList.jsp" method="post">
	<input type="hidden" name="confirmAction" value="true"></input>
	<h3>Please indicate which data <%= StringEscapeUtils.escapeHtml("" + (patientName)) %> should be allowed to enter:</h3>
	<input type="checkbox" name="bloodPressure" />Blood Pressure<br />
	<input type="checkbox" name="glucose" />Glucose Level<br />
	<input type="checkbox" name="height" />Height<br />
	<input type="checkbox" name="weight" />Weight<br />
	<input type="checkbox" name="pedometer" />Pedometer<br />
	<input type="submit" name="fSubmit" value="Update Permissions">
</form>
<%
	} else {
		if(addOrRemove.equals("Add")) {
			String[] checkBoxes = new String[] {
										request.getParameter("bloodPressure"),
										request.getParameter("glucose"),
										request.getParameter("height"),
										request.getParameter("weight"),
										request.getParameter("pedometer")};
			boolean[] permissions = new boolean[5];
			for (int i = 0; i < 5; i++) {
				permissions[i] = checkBoxes[i] != null && checkBoxes[i].equals("on");
			}
			
			TelemedicineBean tBean = new TelemedicineBean();
			tBean.setSystolicBloodPressureAllowed(permissions[0]);
			tBean.setDiastolicBloodPressureAllowed(permissions[0]);
			tBean.setGlucoseLevelAllowed(permissions[1]);
			tBean.setHeightAllowed(permissions[2]);
			tBean.setWeightAllowed(permissions[3]);
			tBean.setPedometerReadingAllowed(permissions[4]);
	
			conf_bool = action.addToList(pid, tBean);
			if(conf_bool) {
				confirm = "Patient " + patientName + " Added";
				loggingAction.logEvent(TransactionType.PATIENT_LIST_ADD, loggedInMID, pid, "");
			}
		} else {
			conf_bool = action.removeFromList(pid);
			if(conf_bool) {
				confirm = "Patient " + patientName + " Removed";
				loggingAction.logEvent(TransactionType.PATIENT_LIST_REMOVE, loggedInMID, pid, "");
			}
		}
		session.removeAttribute("pid");
	}
}

if (!"".equals(confirm)) {
%>
	<div align=center>
		<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span>
	</div>
<%
} else if (!(request.getParameter("fSubmit") != null && request.getParameter("fSubmit").contains("Change"))) {
%>

<br />

<form action="editPatientList.jsp" method="post">
	<input type="hidden" name="confirmAction" value="true"></input>
<%
if (addOrRemove.equals("Add")) {
%>
	<h3>Please indicate which data <%= StringEscapeUtils.escapeHtml("" + (patientName)) %> should be allowed to enter:</h3>
	<input type="checkbox" name="bloodPressure" />Blood Pressure<br />
	<input type="checkbox" name="glucose" />Glucose Level<br />
	<input type="checkbox" name="height" />Height<br />
	<input type="checkbox" name="weight" />Weight<br />
	<input type="checkbox" name="pedometer" />Pedometer<br />
<%
}
%>
	<input type="submit" name="fSubmit" value="<%=addOrRemove %> <%= StringEscapeUtils.escapeHtml("" + (patientName )) %>">
<%
if (!addOrRemove.equals("Add")) {
%>
	<input type="submit" name="fSubmit" value="Change <%= StringEscapeUtils.escapeHtml("" + (patientName )) %>'s Telemedicine Permissions">
<%
}
%>
	<input type="submit" name="fSubmit" value="Choose Different Patient" onClick="javascript:differentPatient();">
</form>

<script type="text/javascript">

function differentPatient() {
	document.getElementsByName("confirmAction")[0].value="false";
	document.forms[0].submit();
}

</script>

<%
}
%>
<br />
<br />
<br />

<%@include file="/footer.jsp" %>
