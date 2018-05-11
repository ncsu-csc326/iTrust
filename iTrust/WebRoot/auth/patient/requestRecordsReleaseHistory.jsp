<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.RequestRecordsReleaseAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RecordsReleaseBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Records Release Request History";

String currentMID = loggedInMID.toString();
String loggedInName = authDAO.getUserName(loggedInMID.longValue());

RequestRecordsReleaseAction action = new RequestRecordsReleaseAction(prodDAO, loggedInMID);
List<PatientBean> dependentList = action.getDependents();


String depIndex = request.getParameter("selectedPatient");
if(depIndex != null && !depIndex.equals("-1")){
	currentMID = String.valueOf(dependentList.get(Integer.parseInt(depIndex)).getMID());
	action = new RequestRecordsReleaseAction(prodDAO, Long.parseLong(currentMID));
}

List<RecordsReleaseBean> releaseHistory = action.getAllPatientReleaseRequests();

String isRepresentee = "false";
if(currentMID.equals(loggedInMID.toString())){
	isRepresentee = "false";
}
else{
	isRepresentee = "true";
}

%>

<%@include file="/header.jsp" %>

<br /><br />

<div>
<p align="center"><i>Select to view an old records release request in the table below or choose to submit a new request form</i></p> 
</div>
<br />
<form action="requestRecordsReleaseHistory.jsp" method="post" id="dependentForm" align="center">
<span style="font-size: 12pt"><b>View as: </b></span><select name="selectedPatient">
 	<option value="-1"><%=loggedInName%></option>
 	<%
 	for(int i = 0; i < dependentList.size(); i++){
 	%>
 		<option value=<%=String.valueOf(i)%> <%=currentMID.equals(String.valueOf(dependentList.get(i).getMID())) ? "selected" : ""%>><%=dependentList.get(i).getFullName()%></option>
 		
 	<% 
 	}
 	%>
</select>
	<input type="submit" value="Update" id="submitDep"></input>
</form>

<br /><br />

<div align=center>
	<table id="requestHistory" class="fTable">
	<thead>	
		<tr>
		<th colspan="4" style="text-align: center;"><%=action.getPatientName()%>'s Requested Records Release History</th>
		</tr>	
		<tr>
			<th align="center" width="200"><div align="center">Request Date</div></th>
			<th align="center" with="200"><div align="center">Requesting Patient</div></th>
			<th align="center" width="200"><div align="center">Request Status</div></th>
			<th width="100"><div align="center">View</div></th>
		</tr>
	</thead>
	<tbody>
		<%
		session.setAttribute("releaseHistory", releaseHistory);
		int index = 0;
		for (RecordsReleaseBean rec : releaseHistory) {	
		%>
		<input type="hidden" value=<%=index%> name="index" id="index"></input>	
		<tr>
			<td align="center"><%=rec.getDateRequestedStr()%></td>
			<td align="center"><%=action.getPatientName()%></td>
			<td align="center"><%=rec.getStatusStr()%></td>	
			<td align="center"><a href="/iTrust/auth/patient/confirmRecordsRelease.jsp?index=<%=index%>&currentMID=<%=currentMID%>">View</a></td>
		</tr>
		<%
		index++;
		}
		%>	
		<th colspan="4" style="text-align: center;">
		<form action="requestRecordsRelease.jsp" method="post" id="submitRequest">
			<input type="hidden" value=<%=isRepresentee%> id="isRepresentee" name="isRepresentee"></input>
			<input type="hidden" value=<%=currentMID%> id="currentMID" name="currentMID"></input>
			<input type=submit value="Submit New Request" id="submitReq" style="width:200px">
		</form>
		</th>			
	</tbody>
</table>
</div>

<br />




<%@include file="/footer.jsp"%>

