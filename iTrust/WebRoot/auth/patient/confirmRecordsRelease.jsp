<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.RequestRecordsReleaseAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RecordsReleaseBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Records Release Request";

String currentMID = request.getParameter("currentMID");

RequestRecordsReleaseAction releaseAction = new RequestRecordsReleaseAction(prodDAO, Long.parseLong(currentMID));
List<RecordsReleaseBean> releaseHistory = (List<RecordsReleaseBean>)session.getAttribute("releaseHistory");
RecordsReleaseBean rec = releaseHistory.get(Integer.parseInt(request.getParameter("index")));

String justification = (String)request.getAttribute("releaseJustification");
if(justification == null)
	justification = "";

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

if(Long.parseLong(currentMID)==(loggedInMID.longValue())){
	loggingAction.logEvent(TransactionType.PATIENT_VIEW_RELEASE_REQUEST, loggedInMID.longValue(), Long.parseLong(currentMID), "");
}
else if(Long.parseLong(currentMID)!=(loggedInMID.longValue())){
	loggingAction.logEvent(TransactionType.PATIENT_REQUEST_DEPEDENT_RECORDS, loggedInMID.longValue(), Long.parseLong(currentMID), "");
}
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
<span style="font-size: 12pt"><b>Patient name: </b> <%=releaseAction.getPatientName()%></span><br />
<br />
<b>Release hospital: </b> <%=releaseAction.getHospitalName(rec.getReleaseHospitalID())%><br />
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


<%@include file="/footer.jsp"%>