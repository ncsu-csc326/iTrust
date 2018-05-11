<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.EditHealthHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Basic Health Record";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Basic Health History" />
<%
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/editBasicHealth.jsp");
   	return;
}
//else {
//	session.removeAttribute("pid");
//}
EditHealthHistoryAction action = new EditHealthHistoryAction(prodDAO,loggedInMID.longValue(), pidString);
long pid = action.getPid();
String patientName = action.getPatientName();
String confirm = "";
if ("true".equals(request.getParameter("formIsFilled"))) {
	try { 
		confirm = action.addHealthRecord(pid, new BeanBuilder<HealthRecordForm>().build(request.getParameterMap(), new HealthRecordForm()));
		loggingAction.logEvent(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, loggedInMID.longValue(), pid, "");
	} catch(FormValidationException e){
%>
		<div align=center>
			<span class="iTrustError"><%e.printHTML(pageContext.getOut());%></span>
		</div>
		<br />
<%
	}
}
List<HealthRecord> records = action.getAllHealthRecords(pid);
HealthRecord mostRecent = (records.size() > 0) ? records.get(0) : new HealthRecord(); //for the default values
%>

<script type="text/javascript">
function showAddRow(){
	document.getElementById("addRow").style.display="inline";
	document.getElementById("addRowButton").style.display="none";
	document.forms[0].height.focus();
}
</script>

<%
if (!"".equals(confirm)) {
%>
	<div align=center>
		<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span>
	</div>
<%
} else {
	loggingAction.logEvent(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, loggedInMID.longValue(), pid, "");
}
%>

<br />
<div align=center>
	<table align="center" class="fTable">
		<tr>
			<th colspan="11">Basic Health History</th>
		</tr>
		<tr class = "subHeader">
			<td>Height<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Height" id="viewHeightChart">View Chart</a>)</td>
			<td>Weight<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=Weight" id="viewWeightChart">View Chart</a>)</td>
			<td>BMI<br />(<a href="/iTrust/auth/hcp-uap/healthDataChart.jsp?dataType=BMI" id="viewBmiChart">View Chart</a>)</td>
			<td>Smokes?</td>
			<td>Blood Pressure</td>
			<td>HDL</td>
			<td>LDL</td>
			<td>Triglycerides</td>
			<td>Total Cholesterol</td>
			<td>Last Recorded</td>
			<td>By Personnel</td>
		</tr>
	<%
	//(weight in pounds * 703)/(height in inches * height in inches)
	for (HealthRecord hr : records) {
		DecimalFormat df = new DecimalFormat("00.00");
		String bmiString = "Invalid height value of 0!";
		if(hr.getHeight() != 0) {
			bmiString = df.format((hr.getWeight()*703)/(hr.getHeight()*hr.getHeight()));
		}
	%>
		<tr>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getHeight())) %>in</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getWeight())) %>lbs</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (bmiString)) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.isSmoker() ? "Y" : "N")) %></td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getBloodPressure())) %> mmHg</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolHDL())) %> mg/dL</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolLDL())) %> mg/dL</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getCholesterolTri())) %> mg/dL</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getTotalCholesterol())) %> mg/dL</td>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (hr.getDateRecorded())) %></td>
<%
		PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
%>
			<td align=center><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
		</tr>
	<%
	}
	%>
	</table>
	<br />
	<a href="javascript:showAddRow();" id="addRowButton" style="text-decoration:none;" >
		<input id="addRowButton" type=button value="Add Record" onClick="showAddRow();"> 
	</a>
</div>
<br />
<div id="addRow" style="display: none;" align=center>
<form action="editBasicHealth.jsp" id="editHealth" name="editHealth" method="post">
<input type="hidden" name="formIsFilled" value="true">
<table class="fTable" align="center">
	<tr>
		<th colspan="2" style="background-color:silver;">Record Information</th>
	</tr>	
	<tr>
		<td class="subHeader">Height (in.):</td>
		<td ><input name="height"
			value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getHeight())) %>" style="width: 50px" type="text"
			maxlength="5"></td>
	</tr>
	<tr>
		<td class="subHeader">Weight (lbs.):</td>
		<td ><input name="weight"
			value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getWeight())) %>" style="width: 50px" type="text"
			maxlength="5"></td>
	</tr>
	<tr>
		<td class="subHeader">Smoker?:</td>
		<td >
		
		<select name="isSmoker" id="isSmoker" style="font-size:10px;">
                	<option value=""> -- Please Select a Smoking Status -- </option>
                    <option value="1">1 - Current every day smoker</option>
               		<option value="2">2 - Current some day smoker</option>
               		<option value="3">3 - Former smoker</option>
               		<option value="4">4 - Never smoker</option>
               		<option value="5">5 - Smoker, current status unknown</option>
               		<option value="9">9 - Unknown if ever smoked</option>
        </select>
		</td>
	</tr>
	<tr>
		<td class="subHeader">Blood Pressure (mmHg):</td>
		<td >
			<input name="bloodPressureN" value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getBloodPressureN())) %>" style="width: 40px" maxlength="3" type="text" /> 
			/ <input name="bloodPressureD" value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getBloodPressureD())) %>" style="width: 40px" maxlength="3" type="text" />
		</td>
	</tr>
	<tr>
		<td class="subHeader">Cholesterol (mg/dL):</td>
		<td >
		<table>
			<tr>
				<td style="text-align: right">HDL:</td>
				<td><input name="cholesterolHDL" value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolHDL())) %>" 
				style="width: 38px" maxlength="3" type="text"></td>
			</tr>
			<tr>
				<td style="text-align: right">LDL:</td>
				<td>
					<input name="cholesterolLDL" value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolLDL())) %>" style="width: 38px" maxlength="3" type="text">
				</td>
			</tr>
			<tr>
				<td style="text-align: right">Tri:</td>
				<td>
					<input name="cholesterolTri" value="<%= StringEscapeUtils.escapeHtml("" + (mostRecent.getCholesterolTri())) %>" style="width: 38px" maxlength="3" type="text">
			    </td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br />
<input type="submit" value="Add Record">
</form>
</div>

<br />
<br />
<br />

<%@include file="/footer.jsp" %>
