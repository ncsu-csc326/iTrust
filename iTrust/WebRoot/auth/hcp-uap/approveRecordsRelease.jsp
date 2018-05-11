<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewRecordsReleaseAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.RecordsReleaseBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Records Release Request";

ViewRecordsReleaseAction viewAction = new ViewRecordsReleaseAction(prodDAO, loggedInMID);
session.setAttribute("viewAction", viewAction);

List<RecordsReleaseBean> requests = (List<RecordsReleaseBean>)session.getAttribute("recRequests");
int index = Integer.parseInt(request.getParameter("index"));
RecordsReleaseBean rec = requests.get(index);

int status = rec.getStatus();
String color = "black";
if(status == 0){
	color = "orange";
}
else if(status == 1){
	color = "#4CC417";
}
else if(status == 2){
	color = "red";
}

String justification = rec.getJustification();
if(justification == null)
	justification = "";

%>

<%@include file="/header.jsp" %>

<div>
<br />
<b><i>Status: </i></b><span style="color: <%=color%>"><b><i><%=rec.getStatusStr()%></i></b></span><br />
</div>
<br />
<hr align="left" width="50%">

<div>
<br />
<span style="font-size: 12pt"><b>Patient name: </b> <%=viewAction.getPatientName(rec.getPid()) %></span><br />
<br />
<b>Release hospital: </b> <%=viewAction.getHospitalName(rec.getReleaseHospitalID())%><br />
<br />
<b>Recipient doctor information:</b>
<dl>
	<dd>First name: <%=rec.getDocFirstName()%></dd>
	<dd>Last name: <%=rec.getDocLastName()%></dd>
	<dd>Phone number: <%=rec.getDocPhone()%></dd>
	<dd>Email address: <%=rec.getDocEmail()%></dd>
</dl>
<b>Recipient hospital information:</b>
<dl>
	<dd>Hospital: <%=rec.getRecHospitalName()%></dd>
	<dd>Hospital address: <%=rec.getRecHospitalAddress()%></dd>
</dl>

<b>Release justification:</b>
<dl>
	<dd><%=justification%></dd>
</dl>
</div>

<%
if ("true".equals(request.getParameter("approved"))) {
	viewAction.approveRequest(rec);
	if(userRole.equals("hcp"))
		loggingAction.logEvent(TransactionType.HCP_RELEASE_APPROVAL, loggedInMID.longValue(), rec.getPid(), "");
	else if(userRole.equals("uap"))
		loggingAction.logEvent(TransactionType.UAP_RELEASE_APPROVAL, loggedInMID.longValue(), rec.getPid(), "");	
	response.sendRedirect("/iTrust/auth/hcp-uap/approveRecordsRelease.jsp?index=" + index);
	return;
}
if ("true".equals(request.getParameter("denied"))) {
	viewAction.denyRequest(rec);
	if(userRole.equals("hcp"))
		loggingAction.logEvent(TransactionType.HCP_RELEASE_DENIAL, loggedInMID.longValue(), rec.getPid(), "");
	else if(userRole.equals("uap"))
		loggingAction.logEvent(TransactionType.UAP_RELEASE_DENIAL, loggedInMID.longValue(), rec.getPid(), "");
	response.sendRedirect("/iTrust/auth/hcp-uap/approveRecordsRelease.jsp?index=" + index);
	return;
}

if(status == 0){
%>
<br />
<hr align="left" width="50%">
<br />
<table id="statusButtons" class="display fTable">
	<tbody>
	<tr>
	<form action="approveRecordsRelease.jsp?index=<%=index%>" method="post" id="approveButton">
		<input type="hidden" name="approved" value="true" />
		<input type=submit value="Approve" id="Approve" style="width:100px; display:inline;"></input>
	</form>
			&nbsp;&nbsp;&nbsp;
	<form action="approveRecordsRelease.jsp?index=<%=index%>" method="post" id="denyButton">
		<input type="hidden" name="denied" value="true" />
		<input type="submit" value="Deny" id="Deny" style="width:100px; display:inline;"></input>
	</form>
	</tr>
	</tbody>
</table>
<%
} else if (status == 1) {
%>
<br />
<hr align="left" width="50%">
<br />
<a href="/iTrust/auth/hcp-uap/RecordsReleaseXMLServlet?index=<%=index%>">Download Medical Records</a>
<%
}
%>

<%@include file="/footer.jsp"%>