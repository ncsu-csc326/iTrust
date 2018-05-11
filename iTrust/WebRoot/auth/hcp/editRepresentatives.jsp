<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException" %>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Manage Representatives";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Edit Representatives" />
<br />
<%
	/* Require a Patient ID first */
boolean active = false;
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/editRepresentatives.jsp");
   	return;
} else if (authDAO.isDependent(Long.parseLong(pidString))) {
%>
	<div align=center>
		<h3 class="iTrustError">
		<%=authDAO.getUserName(Long.parseLong(pidString))%> is a dependent. Dependent users cannot represent others.
		</h3>
	</div>
	
<br /><br /><br />
<itrust:patientNav thisTitle="Edit Representatives" />
<%
	return;
}
	

EditRepresentativesAction action = null;
List<PatientBean> representees = null;
long pid = 0L;
try {
	action = new EditRepresentativesAction(prodDAO,loggedInMID.longValue(), pidString);
	pid=action.getPid();
	if(!action.checkIfPatientIsActive(pid))
	{%>
		<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml("This user is not active") %></span>
		</div>
		<br />
	<% }
	String representee = request.getParameter("UID_repID");
	String confirm = "";
	if(representee!=null && !representee.equals("")){
		confirm = action.addRepresentee(representee);
		loggingAction.logEvent(TransactionType.HEALTH_REPRESENTATIVE_DECLARE, loggedInMID, Long.parseLong(representee), "Represented by: " + pid);
	}
	String removeId = request.getParameter("removeId");
	if(removeId!=null && !removeId.equals("")){
		confirm = action.removeRepresentee(removeId);
		loggingAction.logEvent(TransactionType.HEALTH_REPRESENTATIVE_UNDECLARE, loggedInMID, Long.parseLong(removeId), "Represented by: " + pid);
	}

	if(!"".equals(confirm)){
%>
	<div align=center>
		<span class="iTrustMessage"><%=StringEscapeUtils.escapeHtml("" + (confirm))%></span>
	</div>
	<br />
<%
	}
} catch(ITrustException ite) {
%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(ite.getMessage()) %></span>
	</div>
	<br />
<%
} finally {
	representees = action.getRepresented(pid);
}
%>

<form method="post" name="mainForm">
<input type="hidden" name="pid" value="<%= StringEscapeUtils.escapeHtml("" + (pid )) %>">
<input type="hidden" id="removeId" name="removeId" value="">
<script type="text/javascript">
	function removeRep(repMID) {
		document.getElementById("removeId").value = repMID;
		document.mainForm.submit();
	}
</script>
<table class="fTable" align="center">
	<tr>
		<th colspan="5" style="text-align: center;"><%=action.getRepresentativeName() %>'s Representees</th>
	</tr>
	<tr class="subHeader">
		<td>Name</td>
		<td style="width: 30px">Action</td>
	</tr>
<%
	if(representees.size() ==0) { 
%>
		<tr><td  colspan="5" style="text-align: center;">No representatives specified</td></tr>
<%
	} 
	else { 
	    for(PatientBean p : representees){
%>
				<tr>
					<td ><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
					<td ><a href="javascript:removeRep('<%= StringEscapeUtils.escapeHtml("" + (p.getMID())) %>')">Remove</a></td>
				</tr>
<% 
	    }
	}
%>
	<tr>
		<td colspan="3">
			<table>
				<tr>
				<td><b>Patient:</b></td>
				<td style="width: 150px; border: 1px solid Gray;">
					<input name="UID_repID" value="" type="hidden">
					<span id="NAME_repID" name="NAME_repID">Not specified</span>
				</td>
				<%
				if(action.checkIfPatientIsActive(pid))
				{%>
					<td>
						<%@include file="/util/getUserFrame.jsp" %>
						<input type="button" onclick="getUser('repID');" value="Find User" >
					</td>
				<%}
				else
				{%>
					<td>
						<%@include file="/util/getUserFrame.jsp" %>
						<input type="button" onclick="getUser('repID');" value="Find User" disabled >
					</td>
				<% }%>
				
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
<% if(action.checkIfPatientIsActive(pid))
{
active = true; %>
<div align=center>
<input name="action" type="submit" value="Represent" style="width:125px">
</div>
<%} else{ 
active = false;
%>
<div align=center>
<input name="action" type="submit" value="Represent" style="width:125px" disabled>
</div>
<%} %>
</form>

<br /><br /><br />
<itrust:patientNav thisTitle="Edit Representatives" />

<%@include file="/footer.jsp" %>
