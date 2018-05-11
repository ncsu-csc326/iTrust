<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.RequestRecordsReleaseAction"%>

<%@include file="/global.jsp" %>


<%
pageTitle = "iTrust - Records Release Request";

String currentMID = request.getParameter("currentMID");

RequestRecordsReleaseAction releaseAction = new RequestRecordsReleaseAction(prodDAO, Long.parseLong(currentMID));

String justification = (String)request.getAttribute("releaseJustification");
if(justification == null)
	justification = "";
%>

<%@include file="/header.jsp" %>

<div>
<br />
<span align=center style="color: #4CC417; font-size: 14pt"><b><i>Request successfully sent</i></b></span><br /><br />
<b><i>Status: </i></b><span style="color: orange"><b><i><%=(String)request.getAttribute("status")%></i></b></span><br />
</div>
<br />
<hr align="left" width="50%">

<div>
<br />
<span style="font-size: 12pt"><b>Patient name: </b> <%=releaseAction.getPatientName()%></span><br />
<br />
<b>Release hospital: </b> <%=releaseAction.getHospitalName((String)request.getAttribute("releaseHospital"))%><br />
<br />
<b>Recipient doctor information:</b>
<dl>
	<dd>First name: <%=(String)request.getAttribute("recFirstName")%></dd>
	<dd>Last name: <%=(String)request.getAttribute("recLastName")%></dd>
	<dd>Phone number: <%=(String)request.getAttribute("recPhone")%></dd>
	<dd>Email address: <%=(String)request.getAttribute("recEmail")%></dd>
</dl>
<b>Recipient hospital information:</b>
<dl>
	<dd>Hospital: <%=(String)request.getAttribute("recHospitalName")%></dd>
	<dd>Hospital address: <%=(String)request.getAttribute("recHospitalAddress")%></dd>
</dl>

<b>Release justification:</b>
<dl>
	<dd><%=justification%></dd>
</dl>
</div>

<%@include file="/footer.jsp"%>